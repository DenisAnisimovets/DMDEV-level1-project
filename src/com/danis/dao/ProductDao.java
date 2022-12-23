package com.danis.dao;

import com.danis.entity.Product;
import com.danis.exception.DaoException;
import com.danis.util.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDao implements Dao<Integer, Product> {

    public ProductDao() {
    }

    private static final ProductDao INSTANCE = new ProductDao();

    public static ProductDao getInstance() {
        return INSTANCE;
    }

    private static final String SAVE_SQL = """
            INSERT INTO internet_shop.public.products (name, description, price, quantity)
            VALUES (?, ?, ?, ?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE internet_shop.public.products
            SET name = ?,
                description = ?,
                price = ?,
                quantity = ?
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                name,
                description,
                price,
                quantity
            FROM internet_shop.public.products
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE products.id = ?
            """;

    private static final String FIND_BY_NAME_SQL = FIND_ALL_SQL + """
            WHERE products.name = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM products
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
    public Product save(Product product) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            populatePreparedStatement(product, preparedStatement);

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                product.setId(generatedKeys.getInt("id"));
            }
            return product;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Product product) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            populatePreparedStatement(product, preparedStatement);
            preparedStatement.setInt(5, product.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void populatePreparedStatement(Product product, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, product.getName());
        preparedStatement.setString(2, product.getDescription());
        preparedStatement.setInt(3, product.getPrice());
        preparedStatement.setInt(4, product.getQuantity());
    }

    @Override
    public Optional<Product> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);

            var resultSet = preparedStatement.executeQuery();
            Product product = null;
            if (resultSet.next()) {
                product = buildProduct(resultSet);
            }

            return Optional.ofNullable(product);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Product> findByName(String name) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_NAME_SQL)) {
            preparedStatement.setString(1, name);

            var resultSet = preparedStatement.executeQuery();
            Product product = null;
            if (resultSet.next()) {
                product = buildProduct(resultSet);
            }

            return Optional.ofNullable(product);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Product> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(buildProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Product buildProduct(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getInt("price"),
                resultSet.getInt("quantity")
        );
    }
}
