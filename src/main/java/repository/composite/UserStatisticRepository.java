package repository.composite;

import model.common.TransactionResult;
import model.composite.UserStatistic;
import repository.Database;

import java.sql.*;
import java.util.ArrayList;

public class UserStatisticRepository {
    public static TransactionResult<ArrayList<UserStatistic>> getUsersStatistic() {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT u.id, u.username, (SELECT COUNT(*) FROM posts p WHERE p.author_id = u.id) AS post_count, (SELECT COUNT(*) FROM hubs h WHERE h.creator_id = u.id) AS hub_count, (SELECT COUNT(*) FROM votes v WHERE v.voter_id = u.id) AS vote_count, (SELECT COUNT(*) FROM sessions s WHERE s.user_id = u.id) AS active_session_count FROM users u;",
                    Statement.RETURN_GENERATED_KEYS
            );
            ResultSet resultSet = statement.executeQuery();
            ArrayList<UserStatistic> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new UserStatistic(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getInt(5),
                        resultSet.getInt(6)
                ));
            }
            return new TransactionResult<>(data);
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<UserStatistic> getUserStatistic(int id) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT u.id, u.username, (SELECT COUNT(*) FROM posts p WHERE p.author_id = u.id) AS post_count, (SELECT COUNT(*) FROM hubs h WHERE h.creator_id = u.id) AS hub_count, (SELECT COUNT(*) FROM votes v WHERE v.voter_id = u.id) AS vote_count, (SELECT COUNT(*) FROM sessions s WHERE s.user_id = u.id) AS active_session_count FROM users u WHERE u.id = ?;",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new TransactionResult<>(new UserStatistic(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getInt(5),
                        resultSet.getInt(6)
                ));
            } else {
                return new TransactionResult<>("Пользователь не найден", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }
}
