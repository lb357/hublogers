package repository.data;

import model.common.TransactionResult;
import model.data.Vote;
import repository.Database;

import java.sql.*;
import java.util.ArrayList;

public class VoteRepository {
    public static TransactionResult<Vote> votePostByUser(int postId, int voterId, boolean vote) {
        try (Connection connection = Database.getConnection()) {
            TransactionResult<Vote> currentVote = getVote(postId, voterId);
            if (currentVote.isSuccess()) {
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE votes SET vote = ? WHERE post_id = ? AND voter_id = ? RETURNING *;",
                        Statement.RETURN_GENERATED_KEYS
                );
                statement.setBoolean(1, vote);
                statement.setInt(2, postId);
                statement.setInt(3, voterId);
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    return new TransactionResult<>(new Vote(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getBoolean(3)
                    ));
                } else {
                    throw new SQLException("Из базы данных не получен ответ");
                }
            } else {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO votes (post_id, voter_id, vote) VALUES (?, ?, ?) returning *;",
                        Statement.RETURN_GENERATED_KEYS
                );
                statement.setInt(1, postId);
                statement.setInt(2, voterId);
                statement.setBoolean(3, vote);
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    return new TransactionResult<>(new Vote(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getBoolean(3)
                    ));
                } else {
                    throw new SQLException("Из базы данных не получен ответ");
                }
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<Vote> getVote(int postId, int voterId) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM votes WHERE post_id=? AND voter_id=?;",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, postId);
            statement.setInt(2, voterId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new TransactionResult<>(new Vote(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getBoolean(3)
                ));
            } else {
                return new TransactionResult<>("Пользователь/пост не найден", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<Integer> getLikes(int postId) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM votes WHERE post_id=? AND vote;",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, postId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new TransactionResult<>(resultSet.getInt(1));
            } else {
                return new TransactionResult<>("Пост не найден", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<Integer> getDislikes(int postId) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM votes WHERE post_id=? AND NOT vote;",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, postId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new TransactionResult<>(resultSet.getInt(1));
            } else {
                return new TransactionResult<>("Пост не найден", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }



    public static TransactionResult<Integer> unvotePostByUser(int postId, int voterId) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE from votes WHERE post_id=? AND voter_id=?;");
            statement.setInt(1, postId);
            statement.setInt(2, voterId);
            int count = statement.executeUpdate();
            if (count > 0) {
                return new TransactionResult<>(postId);
            } else {
                return new TransactionResult<>("Удаление не выполнено", 0);
            }
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }

    public static TransactionResult<ArrayList<Vote>> getVotes() {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM votes;",
                    Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Vote> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(new Vote(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getBoolean(3)
                ));
            }
            return new TransactionResult<>(data);
        } catch (SQLException e) {
            System.out.printf("%s / %s (%d)", e.getMessage(), e.getSQLState(), e.getErrorCode());
            return new TransactionResult<>(e.getSQLState(), e.getErrorCode());
        }
    }
}
