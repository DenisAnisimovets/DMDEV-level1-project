package com.danis.dao;

import com.danis.entity.Bucket;
import com.danis.entity.Order;
import com.danis.exception.DaoException;
import com.danis.util.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDao implements Dao<Integer, Order> {

    public OrderDao() {
    }

    private static final OrderDao INSTANCE = new OrderDao();

    public static OrderDao getInstance() {
        return INSTANCE;
    }

    private static final String SAVE_SQL = """
            INSERT INTO internet_shop.public.orders (user_id, sum, isActive) 
            VALUES (?, ?, ?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE internet_shop.public.orders
            SET user_id = ?,
                sum = ?,
                isActive = ?                          
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                user_id,
                sum,
                isActive
            FROM internet_shop.public.orders
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE internet_shop.public.orders.id = ?
            """;
    private static final String UPDATE_DELETE = """
            UPDATE internet_shop.public.orders
            SET isActive = FALSE                
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
    public Order save(Order order) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            populatePreparedStatement(order, preparedStatement);

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setId(generatedKeys.getInt("id"));
            }
            return order;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Order order) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            populatePreparedStatement(order, preparedStatement);
            preparedStatement.setInt(4, order.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void populatePreparedStatement(Order order, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, order.getUser_id());
        preparedStatement.setInt(2, order.getSum());
        preparedStatement.setBoolean(3, order.isActive());
    }

    @Override
    public List<Order> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(buildOrder(resultSet));
            }
            return orders;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Order> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);

            var resultSet = preparedStatement.executeQuery();
            Order order = null;
            if (resultSet.next()) {
                order = buildOrder(resultSet);
            }

            return Optional.ofNullable(order);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Order buildOrder(ResultSet resultSet) throws SQLException {
        return new Order(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("sum"),
                resultSet.getBoolean("isActive")
        );
    }
}
