package repository.data;

import model.value.DateTime;
import model.domain.Post;
import repository.Database;

import java.sql.*;
import java.util.ArrayList;

public class PostRepository {
    public static Post createPost(int authorId, Integer hubId, String label, String content) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO posts (author_id, hub_id, label, content) VALUES (?, ?, ?, ?) returning id, creation_time;",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, authorId);
            if (hubId != null) {
                statement.setInt(2, hubId);
            } else {
                statement.setNull(2, Types.INTEGER);
            }

            statement.setString(3, label);
            statement.setString(4, content);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return new Post(
                        resultSet.getInt(1),
                        authorId,
                        hubId,
                        label,
                        content,
                        new DateTime(resultSet.getTimestamp(2))
                );
            } else {
                throw new SQLException("Ожидалось создание новой записи, однако запись не была создана");
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                return null;
            } else {
                throw e;
            }
        }
    }

    public static Post readPost(int id)  throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts WHERE id=?;");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Post(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        new DateTime(resultSet.getTimestamp(6))
                );
            } else {
                return null;
            }
        }
    }


    public static Post updatePost(int id, String content) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE posts SET content=?, creation_time=NOW() WHERE id=? RETURNING *;"
            );
            statement.setString(1, content);
            statement.setInt(2, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Post(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        new DateTime(resultSet.getTimestamp(6))
                );
            } else {
                return null;
            }
        }
    }

    public static int deletePost(int id) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE from posts WHERE id=?;");
            statement.setInt(1, id);
            return statement.executeUpdate();
        }
    }


    public static ArrayList<Post> getPosts() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM posts;");
            ArrayList<Post> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new Post(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        new DateTime(resultSet.getTimestamp(6))
                ));
            }
            return data;
        }
    }
}
