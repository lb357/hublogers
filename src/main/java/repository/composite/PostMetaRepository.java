package repository.composite;

import model.common.TransactionResult;
import model.composite.MetaPost;
import repository.Database;

import java.sql.*;
import java.util.ArrayList;

public class PostMetaRepository {
    private static final int pageSize = 3;

    public static TransactionResult<ArrayList<MetaPost>> getMetaPosts(int offset, int limit) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT p.id as post_id, p.author_id, p.hub_id, p.label, p.creation_time, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND v.vote) as likes, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND NOT v.vote) as dislikes, u.username, h.hubname FROM posts p JOIN users u ON u.id=p.author_id JOIN hubs h ON h.id=p.hub_id ORDER BY creation_time DESC OFFSET ? LIMIT ?;",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, offset);
            statement.setInt(2, limit);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<MetaPost> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new MetaPost(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getTimestamp(5),
                        resultSet.getInt(6),
                        resultSet.getInt(7),
                        resultSet.getString(8),
                        resultSet.getString(9)
                ));
            }
            return new TransactionResult<>(data);
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }


    public static TransactionResult<ArrayList<MetaPost>> findMetaPosts(String query, int offset, int limit) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT p.id as post_id, p.author_id, p.hub_id, p.label, p.creation_time, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND v.vote) as likes, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND NOT v.vote) as dislikes, u.username, h.hubname FROM posts p JOIN users u ON u.id=p.author_id JOIN hubs h ON h.id=p.hub_id WHERE p.label LIKE ? ORDER BY creation_time DESC OFFSET ? LIMIT ?;",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, "%"+query+"%");
            statement.setInt(2, offset);
            statement.setInt(3, limit);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<MetaPost> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new MetaPost(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getTimestamp(5),
                        resultSet.getInt(6),
                        resultSet.getInt(7),
                        resultSet.getString(8),
                        resultSet.getString(9)
                ));
            }
            return new TransactionResult<>(data);
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<ArrayList<MetaPost>> hubMetaPosts(int hubId, int offset, int limit) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT p.id as post_id, p.author_id, p.hub_id, p.label, p.creation_time, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND v.vote) as likes, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND NOT v.vote) as dislikes, u.username, h.hubname FROM posts p JOIN users u ON u.id=p.author_id JOIN hubs h ON h.id=p.hub_id WHERE p.hub_id = ? ORDER BY creation_time DESC OFFSET ? LIMIT ?;",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, hubId);
            statement.setInt(2, offset);
            statement.setInt(3, limit);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<MetaPost> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new MetaPost(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getTimestamp(5),
                        resultSet.getInt(6),
                        resultSet.getInt(7),
                        resultSet.getString(8),
                        resultSet.getString(9)
                ));
            }
            return new TransactionResult<>(data);
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<ArrayList<MetaPost>> getMetaPosts(int page) {
        return getMetaPosts(page*pageSize, pageSize);
    }

    public static TransactionResult<ArrayList<MetaPost>> findMetaPosts(String query, int page) {
        return findMetaPosts(query, page*pageSize, pageSize);
    }

    public static TransactionResult<ArrayList<MetaPost>> hubMetaPosts(int hubId, int page) {
        return hubMetaPosts(hubId, page*pageSize, pageSize);
    }
}
