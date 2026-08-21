package repository.composite;

import model.composite.MetaHub;
import model.domain.Hub;
import model.domain.User;
import repository.Database;

import java.sql.*;
import java.util.ArrayList;

public class MetaHubRepository {
    private static final int pageSize = 3;

    public static MetaHub readMetaHub(int id) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT h.id as hub_id, h.creator_id, h.hubname, h.description, u.username, u.status FROM hubs h JOIN users u ON u.id=h.creator_id WHERE h.id=?;");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new MetaHub(
                        new Hub(
                                resultSet.getInt(1),
                                resultSet.getInt(2),
                                resultSet.getString(3),
                                resultSet.getString(4)

                        ),
                        new User(
                                resultSet.getInt(2),
                                resultSet.getString(5),
                                resultSet.getString(6)
                        )
                );
            } else {
                return null;
            }
        }
    }

    public static ArrayList<MetaHub> getMetaHubs(int offset, int limit) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT h.id as hub_id, h.creator_id, h.hubname, h.description, u.username, u.status FROM hubs h JOIN users u ON u.id=h.creator_id OFFSET ? LIMIT ?;");
            statement.setInt(1, offset);
            statement.setInt(2, limit);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<MetaHub> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new MetaHub(
                        new Hub(
                                resultSet.getInt(1),
                                resultSet.getInt(2),
                                resultSet.getString(3),
                                resultSet.getString(4)

                        ),
                        new User(
                                resultSet.getInt(2),
                                resultSet.getString(5),
                                resultSet.getString(6)
                        )
                ));
            }
            return data;
        }
    }

    public static ArrayList<MetaHub> getUserMetaHubs(int creatorId, int offset, int limit) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT h.id as hub_id, h.creator_id, h.hubname, h.description, u.username, u.status FROM hubs h JOIN users u ON u.id=h.creator_id WHERE h.creator_id=? OFFSET ? LIMIT ?;");
            statement.setInt(1, creatorId);
            statement.setInt(2, offset);
            statement.setInt(3, limit);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<MetaHub> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new MetaHub(
                        new Hub(
                                resultSet.getInt(1),
                                resultSet.getInt(2),
                                resultSet.getString(3),
                                resultSet.getString(4)

                        ),
                        new User(
                                resultSet.getInt(2),
                                resultSet.getString(5),
                                resultSet.getString(6)
                        )
                ));
            }
            return data;
        }
    }

    public static ArrayList<MetaHub> getMetaHubs(int page) throws SQLException {
        return getMetaHubs(page*pageSize, pageSize);
    }

    public static ArrayList<MetaHub> getUserMetaHubs(int creatorId, int page) throws SQLException {
        return getUserMetaHubs(creatorId, page*pageSize, pageSize);
    }

    public static Integer metaHubCount() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM hubs;");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Поле count в ответе из базы данных обязано существовать, однако отсутствует");
            }
        }
    }

    public static Integer userMetaHubCount(int creatorId) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT COUNT(*) FROM hubs WHERE creator_id=?;"
            );
            statement.setInt(1, creatorId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Поле count в ответе из базы данных обязано существовать, однако отсутствует");
            }
        }
    }

    public static int getPageSize(){
        return pageSize;
    }
}
