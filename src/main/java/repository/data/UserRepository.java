package repository.data;

import model.domain.User;
import repository.Database;

import java.sql.*;
import java.util.ArrayList;

public class UserRepository {
    public static User createUser(String username, String email, String passwordHash) throws SQLException {
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
                return new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)
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

    public static User readUser(String email, String password) throws SQLException {
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
                    return user;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public static User readUser(int id) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id, username, status FROM users WHERE id=?;");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)
                );
            } else {
                return null;
            }
        }
    }


    public static User updateUser(int id, String status) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET status=? WHERE id=? RETURNING *;"
            );
            statement.setString(1, status);
            statement.setInt(2, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)
                );
            } else {
                return null;
            }
        }
    }

    public static int deleteUser(int id) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE from users WHERE id=?;");
            statement.setInt(1, id);
            return statement.executeUpdate();
        }
    }


    public static ArrayList<User> getUsers() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users;");
            ArrayList<User> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)
                ));
            }
            return data;
        }
    }
}
