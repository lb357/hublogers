package repository.data;

import model.data.Post;
import model.common.TransactionResult;
import repository.Database;

import java.sql.*;
import java.util.ArrayList;

public class PostRepository {
    public static TransactionResult<Post> createPost(int authorId, Integer hubId, String label, String content) {
        if (label == null || label.isBlank() || content == null || content.isBlank()) {
            throw new IllegalArgumentException("Пустые аргументы");
        }
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO posts (author_id, hub_id, label, content) VALUES (?, ?, ?, ?) returning *;",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, authorId);
            statement.setInt(2, hubId);
            statement.setString(3, label);
            statement.setString(4, content);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return new TransactionResult<>(new Post(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getTimestamp(6)
                ));
            } else {
                throw new SQLException("Из базы данных не получен ответ");
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<Post> readPost(int id) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts WHERE id=?;",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new TransactionResult<>(new Post(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getTimestamp(6)
                ));
            } else {
                return new TransactionResult<>("Пользователь не найден", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }


    public static TransactionResult<Post> updatePost(int id, String content) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE posts SET content=?, creation_time=NOW() WHERE id=? RETURNING *;",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, content);
            statement.setInt(2, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new TransactionResult<>(new Post(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getTimestamp(6)
                ));
            } else {
                return new TransactionResult<>("Обновление не выполнено", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<Integer> deletePost(int id) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE from posts WHERE id=?;");
            statement.setInt(1, id);
            int count = statement.executeUpdate();
            if (count > 0) {
                return new TransactionResult<>(id);
            } else {
                return new TransactionResult<>("Удаление не выполнено", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }


    public static TransactionResult<ArrayList<Post>> getPosts() {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts;",
                    Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Post> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new Post(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getTimestamp(6)
                ));
            }
            return new TransactionResult<>(data);
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }
}
