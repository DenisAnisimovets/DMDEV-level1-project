package com.danis.dao;

import com.danis.entity.Role;
import com.danis.exception.DaoException;
import com.danis.util.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleDao implements Dao<Integer, Role> {

    public RoleDao() {
    }

    private static final RoleDao INSTANCE = new RoleDao();

    public static RoleDao getInstance() {
        return INSTANCE;
    }

    private static final String SAVE_SQL = """
            INSERT INTO internet_shop.public.roles (name) 
            VALUES (?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE internet_shop.public.roles
            SET name = ?               
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                name
            FROM internet_shop.public.roles
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE roles.id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE FROM roles
            WHERE id = ?
            """;

    @Override
    public boolean delete(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Role save(Role role) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            populatePreparedStatement(role, preparedStatement);

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                role.setId(generatedKeys.getInt("id"));
            }
            return role;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Role role) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            populatePreparedStatement(role, preparedStatement);
            preparedStatement.setInt(2, role.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void populatePreparedStatement(Role role, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, role.getRolename());
    }

    @Override
    public Optional<Role> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);

            var resultSet = preparedStatement.executeQuery();
            Role role = null;
            if (resultSet.next()) {
                role = buildRole(resultSet);
            }

            return Optional.ofNullable(role);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Role> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Role> roles = new ArrayList<>();
            while (resultSet.next()) {
                roles.add(buildRole(resultSet));
            }
            return roles;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Role buildRole(ResultSet resultSet) throws SQLException {
        return new Role(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}
