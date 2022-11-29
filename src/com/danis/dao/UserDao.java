package com.danis.dao;

import com.danis.entity.User;
import com.danis.exception.DaoException;
import com.danis.util.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<Integer, User>{
    public UserDao() {
    }

    private static final UserDao INSTANCE = new UserDao();
    /*private static final String DELETE_SQL = """
            DELETE FROM users
            WHERE id = ?
            """;
    */
    private static final String SAVE_SQL = """
            INSERT INTO internet_shop.public.users (username, first_name, last_name, email, password, city, isBlocked) 
            VALUES (?, ?, ?, ?, ?, ?, ?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE internet_shop.public.users
            SET username = ?,
                first_name = ?,
                last_name = ?,
                email = ?,
                password = ?,
                city = ?,
                isBlocked = ?                
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                username,
                first_name,
                last_name,
                email,
                password,
                city,
                isBlocked
            FROM internet_shop.public.users
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE users.id = ?
            """;
    private static final String UPDATE_DELETE = """
            UPDATE internet_shop.public.users
            SET isBlocked = TRUE                
            WHERE id = ?
            """;

    @Override
    public boolean delete(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_DELETE)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public User save(User user) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            populatePreparedStatement(user, preparedStatement);

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt("id"));
            }
            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(User user) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            populatePreparedStatement(user, preparedStatement);
            preparedStatement.setInt(8, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void populatePreparedStatement(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getFirstName());
        preparedStatement.setString(3, user.getLastName());
        preparedStatement.setString(4, user.getEmail());
        preparedStatement.setString(5, user.getPassword());
        preparedStatement.setString(6, user.getCity());
        preparedStatement.setBoolean(7, user.isBlocked());
    }

    @Override
    public Optional<User> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);

            var resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = buildUser(resultSet);
            }

            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("city"),
                resultSet.getBoolean("isBlocked")
        );
    }
}
