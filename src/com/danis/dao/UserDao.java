package com.danis.dao;

import com.danis.dto.ReadUserDto;
import com.danis.entity.Gender;
import com.danis.entity.Role;
import com.danis.entity.User;
import com.danis.exception.DaoException;
import com.danis.util.ConnectionManager;
import lombok.SneakyThrows;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<Integer, User> {

    public UserDao() {
    }

    private static final UserDao INSTANCE = new UserDao();

    public static UserDao getInstance() {
        return INSTANCE;
    }

    private static final String SAVE_SQL = """
            INSERT INTO internet_shop.public.users (username, role, birthday, gender, email, password, city, image, isBlocked)
            VALUES (?, ?, ?::date, ?, ?, ?, ?, ?, ?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE internet_shop.public.users
            SET username = ?,
                role = ?,
                birthday = ?,
                gender = ?,
                email = ?,
                password = ?,
                city = ? ,
                image = ? ,
                isBlocked = ?
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                username,
                role,
                birthday,
                gender,
                email,
                password,
                city,
                image,
                isBlocked
            FROM internet_shop.public.users
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE users.id = ?
            """;

    private static final String FIND_BY_EMAIL_AND_PASSWORD_SQL = """
            SELECT id,
                username,
                role,
                birthday,
                gender,
                email,
                password,
                city,
                image,
                isBlocked
            FROM  internet_shop.public.users
            WHERE users.email = ? AND users.password = ?
            """;

    private static final String FIND_BY_NAME_SQL = FIND_ALL_SQL + """
            WHERE users.username = ?
            """;

    private static final String UPDATE_DELETE = """
            UPDATE internet_shop.public.users
            SET isBlocked = TRUE
            WHERE id = ?
            """;

    @Override
    @SneakyThrows
    public boolean delete(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_DELETE)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        }
    }

    @Override
    @SneakyThrows
    public User save(User user) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            populatePreparedStatement(user, preparedStatement);

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                user.setId(generatedKeys.getInt("id"));
            }
            return user;
        }
    }

    @Override
    @SneakyThrows
    public void update(User user) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            populatePreparedStatement(user, preparedStatement);
            preparedStatement.setInt(10, user.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public Optional<User> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);

            var resultSet = preparedStatement.executeQuery();
            User user = null;
            if(resultSet.next()) {
                user = buildUser(resultSet);
            }
            return Optional.ofNullable(user);
        }
    }

    @SneakyThrows
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_AND_PASSWORD_SQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            var resultSet = preparedStatement.executeQuery();
            User user = null;
            if(resultSet.next()) {
                user = buildUser(resultSet);
            }
            return Optional.ofNullable(user);
        }
    }

    @SneakyThrows
    public Optional<User> findByName(String name) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_NAME_SQL)) {
            preparedStatement.setString(1, name);

            var resultSet = preparedStatement.executeQuery();
            User user = null;
            if(resultSet.next()) {
                user = buildUser(resultSet);
            }
            return Optional.ofNullable(user);
        }
    }

    private void populatePreparedStatement(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getRole().name());
        preparedStatement.setString(3, user.getBirthday().toString());
        preparedStatement.setString(4, user.getGender().toString());
        preparedStatement.setString(5, user.getEmail());
        preparedStatement.setString(6, user.getPassword());
        preparedStatement.setString(7, user.getCity());
        preparedStatement.setString(8, user.getImage());
        preparedStatement.setBoolean(9, user.isBlocked());
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
        return User.builder().id(resultSet.getInt("id"))
                .username(resultSet.getString("userName"))
                .birthday(resultSet.getObject("birthday", Date.class).toLocalDate())
                .gender(Gender.findByName(resultSet.getString("gender")).orElse(null))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .role(Role.findByName(resultSet.getString("role")).orElse(null))
                .city(resultSet.getString("city"))
                .image(resultSet.getString("image"))
                .isBlocked(resultSet.getBoolean("isBlocked")).build();
    }
}


