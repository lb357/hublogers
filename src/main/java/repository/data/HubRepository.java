package repository.data;

import model.domain.Hub;
import repository.Database;

import java.sql.*;
import java.util.ArrayList;

public class HubRepository {
    public static Hub createHub(int creatorId, String hubname, String description) throws SQLException {
                try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO hubs (creator_id, hubname, description) VALUES (?, ?, ?) returning *;",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, creatorId);
            statement.setString(2, hubname);
            statement.setString(3, description);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return new Hub(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
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

    public static Hub readHub(int id) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM hubs WHERE id=?;");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Hub(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
            } else {
                return null;
            }
        }
    }


    public static Hub updateHub(int id, String description) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE hubs SET description=? WHERE id=? RETURNING *;");
            statement.setString(1, description);
            statement.setInt(2, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Hub(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
            } else {
                return null;
            }
        }
    }

    public static int deleteHub(int id) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE from hubs WHERE id=?;");
            statement.setInt(1, id);
            return statement.executeUpdate();
        }
    }

    public static ArrayList<Hub> getHubs() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM hubs;");
            ArrayList<Hub> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new Hub(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                ));
            }
            return data;
        }
    }
}
