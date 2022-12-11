package com.danis.dao;


import com.danis.entity.ProductOrder;
import com.danis.exception.DaoException;
import com.danis.util.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductOrderDao implements DaoTablePart<Integer, Integer, ProductOrder> {

    public ProductOrderDao() {
    }

    private static final com.danis.dao.ProductOrderDao INSTANCE = new com.danis.dao.ProductOrderDao();

    public static com.danis.dao.ProductOrderDao getInstance() {
        return INSTANCE;
    }

    private static final String SAVE_SQL = """
            INSERT INTO internet_shop.public.product_orders (order_id, product_id, quantity, price, sum) 
            VALUES (?, ?, ?, ?, ?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE internet_shop.public.product_orders
            SET order_id = ?,
                product_id = ?,
                quantity = ?,        
                price = ?,
                sum = ?                  
            WHERE product_orders.order_id = ? AND product_orders.product_id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT order_id,
                product_id,
                quantity,        
                price,
                sum
            FROM internet_shop.public.product_orders
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE product_orders.order_id = ? AND product_orders.product_id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE FROM product_orders
            WHERE  product_orders.order_id = ? AND product_orders.product_id = ?
            """;

    @Override
    public boolean delete(Integer order_id, Integer product_id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, order_id);
            preparedStatement.setInt(2, product_id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public ProductOrder save(ProductOrder productOrder) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            populatePreparedStatement(productOrder, preparedStatement);

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                productOrder.setOrder_id(generatedKeys.getInt("order_id"));
                productOrder.setProduct_id(generatedKeys.getInt("product_id"));
            }
            return productOrder;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(ProductOrder productOrder) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            populatePreparedStatement(productOrder, preparedStatement);
            preparedStatement.setInt(6, productOrder.getOrder_id());
            preparedStatement.setInt(7, productOrder.getProduct_id());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void populatePreparedStatement(ProductOrder productOrder, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, productOrder.getOrder_id());
        preparedStatement.setInt(2, productOrder.getProduct_id());
        preparedStatement.setInt(3, productOrder.getQuantity());
        preparedStatement.setInt(4, productOrder.getPrice());
        preparedStatement.setInt(5, productOrder.getSum());
    }

    @Override
    public Optional<ProductOrder> findById(Integer order_id, Integer product_id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, order_id);
            preparedStatement.setInt(2, product_id);

            var resultSet = preparedStatement.executeQuery();
            ProductOrder productOrder = null;
            if(resultSet.next()) {
                productOrder = buildOrder(resultSet);
            }

            return Optional.ofNullable(productOrder);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<ProductOrder> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<ProductOrder> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(buildOrder(resultSet));
            }
            return orders;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private ProductOrder buildOrder(ResultSet resultSet) throws SQLException {
        return new ProductOrder(
                resultSet.getInt("order_id"),
                resultSet.getInt("product_id"),
                resultSet.getInt("quantity"),
                resultSet.getInt("price"),
                resultSet.getInt("sum"));
    }
}

