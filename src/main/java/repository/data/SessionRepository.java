package repository.data;

import model.common.DateTime;
import model.data.Session;
import repository.Database;

import java.sql.*;
import java.util.ArrayList;

public class SessionRepository {
    public static Session createSession(String authToken, int user_id) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO sessions (auth_token, user_id) VALUES (?, ?) returning *; ",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, authToken);
            statement.setInt(2, user_id);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return new Session(
                        resultSet.getString(1),
                        resultSet.getInt(2),
                        new DateTime(resultSet.getTimestamp(3))
                );
            } else {
                throw new SQLException("Ожидалось создание новой записи, однако запись не была создана");
            }
        }
    }

    public static Integer getUserIdByAuthToken(String authToken) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            Statement deleteStatement = connection.createStatement();
            deleteStatement.executeUpdate(
                    "DELETE FROM sessions WHERE auth_time < NOW() - INTERVAL '30 DAY';"
            );

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM sessions WHERE auth_token=?;"
            );
            statement.setString(1, authToken);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(2);
            } else {
                return null;
            }
        }
    }

    public static int deleteSession(String authToken) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE from sessions WHERE auth_token=?;");
            statement.setString(1, authToken);
            return statement.executeUpdate();
        }
    }

    public static ArrayList<Session> getSessions() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM sessions;");
            ArrayList<Session> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new Session(
                        resultSet.getString(1),
                        resultSet.getInt(2),
                        new DateTime(resultSet.getTimestamp(3))
                ));
            }
            return data;
        }
    }
}
