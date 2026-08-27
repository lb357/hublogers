package repository.composite;

import model.composite.UserStatistic;
import model.domain.User;
import repository.Database;

import java.sql.*;
import java.util.ArrayList;

public class UserStatisticRepository {
    public static ArrayList<UserStatistic> getUsersStatistic() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT u.id, u.username, u.status, (SELECT COUNT(*) FROM posts p WHERE p.author_id = u.id) AS post_count, (SELECT COUNT(*) FROM hubs h WHERE h.creator_id = u.id) AS hub_count, (SELECT COUNT(*) FROM votes v WHERE v.voter_id = u.id) AS vote_count, (SELECT COUNT(*) FROM sessions s WHERE s.user_id = u.id) AS active_session_count FROM users u;"
            );
            ArrayList<UserStatistic> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(
                    new UserStatistic(
                        new User(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3)
                        ),
                        resultSet.getInt(4),
                        resultSet.getInt(5),
                        resultSet.getInt(6),
                        resultSet.getInt(7)
                    )
                );
            }
            return data;
        }
    }
}
