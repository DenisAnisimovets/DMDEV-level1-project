package com.danis.dao;

import com.danis.entity.Bucket;
import com.danis.exception.DaoException;
import com.danis.util.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BucketDao implements Dao<Integer, Bucket> {

    public BucketDao() {
    }

    private static final BucketDao INSTANCE = new BucketDao();

    public static BucketDao getInstance() {
        return INSTANCE;
    }

    private static final String SAVE_SQL = """
            INSERT INTO internet_shop.public.bucket (user_id, product_id, quantity) 
            VALUES (?, ?, ?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE internet_shop.public.bucket
            SET user_id = ?,
                product_id = ?,
                quantity = ?                          
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                user_id,
                product_id,
                quantity
            FROM internet_shop.public.bucket
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE internet_shop.public.bucket.id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE FROM internet_shop.public.bucket
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
    public Bucket save(Bucket bucket) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            populatePreparedStatement(bucket, preparedStatement);

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                bucket.setId(generatedKeys.getInt("id"));
            }
            return bucket;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Bucket bucket) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            populatePreparedStatement(bucket, preparedStatement);
            preparedStatement.setInt(4, bucket.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void populatePreparedStatement(Bucket bucket, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, bucket.getUserId());
        preparedStatement.setInt(2, bucket.getProductId());
        preparedStatement.setInt(3, bucket.getQuantity());
    }

    @Override
    public Optional<Bucket> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);

            var resultSet = preparedStatement.executeQuery();
            Bucket bucket = null;
            if (resultSet.next()) {
                bucket = buildBucket(resultSet);
            }

            return Optional.ofNullable(bucket);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Bucket> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Bucket> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(buildBucket(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Bucket buildBucket(ResultSet resultSet) throws SQLException {
        return new Bucket(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("product_id"),
                resultSet.getInt("quantity")
        );
    }
}
