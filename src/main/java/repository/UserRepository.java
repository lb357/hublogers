package repository;

import model.TransactionResult;
import model.User;

import java.sql.*;

public class UserRepository {
    public static TransactionResult<User> createUser(String username, String email, String passwordHash) {
        if (username==null||username.isBlank()||email==null||email.isBlank()||passwordHash==null||passwordHash.isBlank()) {
            throw new IllegalArgumentException("Некорректные аргументы");
        }
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?) returning *;",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, passwordHash);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return new TransactionResult<>(new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)
                ));
            } else {
                throw new SQLException("Из базы данных не получен ответ");
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<User> readUser(String email, String password) {
        if (email==null||email.isBlank()||password==null||password.isBlank()) {
            throw new IllegalArgumentException("Некорректные аргументы");
        }
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email=?;");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)
                );
                if (user.checkPassword(password)) {
                    return new TransactionResult<>(user);
                } else {
                    return new TransactionResult<>("Неверный пароль", 0);
                }
            } else {
                return new TransactionResult<>("Пользователь не найден", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<User> readUser(int id) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id, username, status FROM users WHERE id=?;");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)
                );
                return new TransactionResult<>(user);
            } else {
                return new TransactionResult<>("Пользователь не найден", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }


    public static TransactionResult<Integer> updateUser(int id, String status) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET status=? WHERE id=?;");
            statement.setString(1, status);
            statement.setInt(2, id);
            int count = statement.executeUpdate();
            if (count > 0) {
                return new TransactionResult<>(id);
            } else {
                return new TransactionResult<>("Обновление не выполнено", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<Integer> deleteUser(int id) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE from users WHERE id=?;");
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


}
