package repository.data;

import model.data.Session;
import model.common.TransactionResult;
import repository.Database;

import java.sql.*;
import java.util.ArrayList;

public class SessionRepository {
    public static TransactionResult<Session> createSession(String authToken, int user_id) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO sessions (token, user_id) VALUES (?, ?) returning *; ",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, authToken);
            statement.setInt(2, user_id);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return new TransactionResult<>(new Session(
                        resultSet.getString(1),
                        resultSet.getInt(2),
                        resultSet.getTimestamp(3)
                ));
            } else {
                throw new SQLException("Из базы данных не получен ответ");
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<Integer> getUserIdByAuthToken(String authToken) {
        if (authToken==null||authToken.isBlank()) {
            throw new IllegalArgumentException("Пустые аргументы");
        }
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM sessions WHERE auth_time < NOW() - INTERVAL '30 DAY'; SELECT * FROM sessions WHERE auth_token=?;",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, authToken);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new TransactionResult<>(resultSet.getInt(2));
            } else {
                return new TransactionResult<>("Токен не найден (возможно устарел)", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<String> deleteSession(String authToken) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE from sessions WHERE auth_token=?;");
            statement.setString(1, authToken);
            int count = statement.executeUpdate();
            if (count > 0) {
                return new TransactionResult<>(authToken);
            } else {
                return new TransactionResult<>("Удаление не выполнено", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<ArrayList<Session>> getSessions() {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM sessions;",
                    Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Session> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new Session(
                        resultSet.getString(1),
                        resultSet.getInt(2),
                        resultSet.getTimestamp(3)
                ));
            }
            return new TransactionResult<>(data);
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }
}
