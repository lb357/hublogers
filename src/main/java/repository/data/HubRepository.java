package repository.data;

import model.data.Hub;
import model.common.TransactionResult;
import repository.Database;

import java.sql.*;
import java.util.ArrayList;

public class HubRepository {
    public static TransactionResult<Hub> createHub(int creatorId, String hubname, String description) {
        if (hubname == null || hubname.isBlank() || description == null || description.isBlank()) {
            throw new IllegalArgumentException("Пустые аргументы");
        }
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
                return new TransactionResult<>(new Hub(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                ));
            } else {
                throw new SQLException("Из базы данных не получен ответ");
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<Hub> readHub(int id) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM hubs WHERE id=?;",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new TransactionResult<>(new Hub(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                ));
            } else {
                return new TransactionResult<>("Пользователь не найден", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }


    public static TransactionResult<Hub> updateHub(int id, String description) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE hubs SET description=? WHERE id=? RETURNING *;",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, description);
            statement.setInt(2, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new TransactionResult<>(new Hub(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                ));
            } else {
                return new TransactionResult<>("Обновление не выполнено", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<Integer> deleteHub(int id) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE from hubs WHERE id=?;");
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

    public static TransactionResult<ArrayList<Hub>> getHubs() {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM hubs;",
                    Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Hub> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new Hub(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                ));
            }
            return new TransactionResult<>(data);
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }
}
