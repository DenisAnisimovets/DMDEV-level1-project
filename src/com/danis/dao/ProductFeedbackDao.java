package com.danis.dao;

import com.danis.entity.Product;
import com.danis.entity.ProductFeedback;
import com.danis.exception.DaoException;
import com.danis.util.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductFeedbackDao implements Dao<Integer, ProductFeedback>{

        public ProductFeedbackDao() {
        }

        private static final com.danis.dao.ProductFeedbackDao INSTANCE = new com.danis.dao.ProductFeedbackDao();

        public static com.danis.dao.ProductFeedbackDao getInstance() {
            return INSTANCE;
        }

        private static final String SAVE_SQL = """
            INSERT INTO internet_shop.public.product_feedbacks (created_at, product_id, user_id, feedback) 
            VALUES (?, ?, ?, ?);
            """;
        private static final String UPDATE_SQL = """
            UPDATE internet_shop.public.product_feedbacks
            SET created_at = ?,
                product_id = ?,
                user_id = ?,
                feedback = ?                          
            WHERE id = ?
            """;
        private static final String FIND_ALL_SQL = """
            SELECT id,
                created_at,
                product_id,
                user_id,
                feedback
            FROM internet_shop.public.product_feedbacks
            """;
        private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE product_feedbacks.id = ?
            """;
        private static final String DELETE_SQL = """
            DELETE FROM product_feedbacks
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
        public ProductFeedback save(ProductFeedback productFeedback) {
            try (var connection = ConnectionManager.get();
                 var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
                populatePreparedStatement(productFeedback, preparedStatement);

                preparedStatement.executeUpdate();

                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    productFeedback.setId(generatedKeys.getInt("id"));
                }
                return productFeedback;
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        }

        @Override
        public void update(ProductFeedback productFeedback) {
            try (var connection = ConnectionManager.get();
                 var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
                populatePreparedStatement(productFeedback, preparedStatement);
                preparedStatement.setInt(5, productFeedback.getId());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        }

        private void populatePreparedStatement(ProductFeedback productFeedback, PreparedStatement preparedStatement) throws SQLException {
            preparedStatement.setTimestamp(1, productFeedback.getCreatedAt());
            preparedStatement.setInt(2, productFeedback.getProductId());
            preparedStatement.setInt(3, productFeedback.getUserId());
            preparedStatement.setString(4, productFeedback.getFeedback());
        }

        @Override
        public Optional<ProductFeedback> findById(Integer id) {
            try (var connection = ConnectionManager.get();
                 var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
                preparedStatement.setInt(1, id);

                var resultSet = preparedStatement.executeQuery();
                ProductFeedback productFeedback = null;
                if (resultSet.next()) {
                    productFeedback = buildProductFeedback(resultSet);
                }

                return Optional.ofNullable(productFeedback);
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        }

        @Override
        public List<ProductFeedback> findAll() {
            try (var connection = ConnectionManager.get();
                 var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
                var resultSet = preparedStatement.executeQuery();
                List<ProductFeedback> productFeedbacks = new ArrayList<>();
                while (resultSet.next()) {
                    productFeedbacks.add(buildProductFeedback(resultSet));
                }
                return productFeedbacks;
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        }

        private ProductFeedback buildProductFeedback(ResultSet resultSet) throws SQLException {
            return new ProductFeedback(
                    resultSet.getInt("id"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getInt("product_id"),
                    resultSet.getInt("user_id"),
                    resultSet.getString("feedback")
            );
        }
}
