package repository.data;

import model.data.Vote;
import repository.Database;

import java.sql.*;
import java.util.ArrayList;

public class VoteRepository {
    public static Vote votePostByUser(int postId, int voterId, boolean vote) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            Vote currentVote = getVote(postId, voterId);
            ResultSet resultSet;
            PreparedStatement statement;

            if (currentVote.get() != null) {
                if (currentVote.get() == vote) {
                    statement = connection.prepareStatement("DELETE from votes WHERE post_id=? AND voter_id=?;");
                    statement.setInt(1, postId);
                    statement.setInt(2, voterId);
                    int count = statement.executeUpdate();
                    if (count > 0) {
                        return new Vote(postId, voterId, null);
                    } else {
                        throw new SQLException(
                                "Ожидалось удаление существующей записи, однако этого не произошло"
                        );
                    }
                } else {
                    statement = connection.prepareStatement(
                            "UPDATE votes SET vote = ? WHERE post_id = ? AND voter_id = ? RETURNING *;"
                    );
                    statement.setBoolean(1, vote);
                    statement.setInt(2, postId);
                    statement.setInt(3, voterId);
                }
            } else {
                statement = connection.prepareStatement(
                        "INSERT INTO votes (post_id, voter_id, vote) VALUES (?, ?, ?) returning *;",
                        Statement.RETURN_GENERATED_KEYS
                );
                statement.setInt(1, postId);
                statement.setInt(2, voterId);
                statement.setBoolean(3, vote);
            }
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return new Vote(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getBoolean(3)
                );
            } else {
                throw new SQLException(
                        "Ожидалось создание новой или изменение существующей записи, однако этого не произошло"
                );
            }
        }
    }

    public static Vote getVote(int postId, int voterId) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM votes WHERE post_id=? AND voter_id=?;");
            statement.setInt(1, postId);
            statement.setInt(2, voterId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Vote(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    resultSet.getBoolean(3)
                );
            } else {
                return new Vote(
                    postId,
                    voterId,
                    null
                );
            }
        }
    }

    public static Integer getLikes(int postId) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM votes WHERE post_id=? AND vote;");
            statement.setInt(1, postId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return null;
            }
        }
    }

    public static Integer getDislikes(int postId) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM votes WHERE post_id=? AND NOT vote;");
            statement.setInt(1, postId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return null;
            }
        }
    }


    public static ArrayList<Vote> getVotes() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM votes;");
            ArrayList<Vote> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new Vote(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getBoolean(3)
                ));
            }
            return data;
        }
    }
}
