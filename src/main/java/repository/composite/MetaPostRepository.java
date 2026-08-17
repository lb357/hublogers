package repository.composite;

import model.common.DateTime;
import model.composite.MetaPost;
import model.data.Hub;
import model.data.Post;
import model.data.User;
import repository.Database;

import java.sql.*;
import java.util.ArrayList;

public class MetaPostRepository {
    private static final int pageSize = 4;

    public static MetaPost readMetaPost(int id)  throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT p.id as post_id, p.author_id, p.hub_id, p.label, p.content, p.creation_time, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND v.vote) as likes, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND NOT v.vote) as dislikes, u.username, u.status, h.creator_id, h.hubname, h.description FROM posts p JOIN users u ON u.id=p.author_id LEFT JOIN hubs h ON h.id=p.hub_id WHERE p.id=?;"
            );
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new MetaPost(
                    new Post(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3)!=0 ? resultSet.getInt(3) : null,
                        resultSet.getString(4),
                        resultSet.getString(5),
                        new DateTime(resultSet.getTimestamp(6))
                    ),
                    new User(
                        resultSet.getInt(2),
                        resultSet.getString(9),
                        resultSet.getString(10)
                    ),
                    resultSet.getInt(3)!=0 ? new Hub(
                        resultSet.getInt(3),
                        resultSet.getInt(11),
                        resultSet.getString(12),
                        resultSet.getString(13)
                    ) : null,
                    resultSet.getInt(7),
                    resultSet.getInt(8)
                );
            } else {
                return null;
            }
        }
    }

    public static ArrayList<MetaPost> getLastMetaPosts(int offset, int limit) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT p.id as post_id, p.author_id, p.hub_id, p.label, p.content, p.creation_time, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND v.vote) as likes, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND NOT v.vote) as dislikes, u.username, u.status, h.creator_id, h.hubname, h.description FROM posts p JOIN users u ON u.id=p.author_id LEFT JOIN hubs h ON h.id=p.hub_id ORDER BY creation_time DESC OFFSET ? LIMIT ?;"
            );
            statement.setInt(1, offset);
            statement.setInt(2, limit);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<MetaPost> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(
                    new MetaPost(
                            new Post(
                                    resultSet.getInt(1),
                                    resultSet.getInt(2),
                                    resultSet.getInt(3)!=0 ? resultSet.getInt(3) : null,
                                    resultSet.getString(4),
                                    resultSet.getString(5),
                                    new DateTime(resultSet.getTimestamp(6))
                            ),
                            new User(
                                    resultSet.getInt(2),
                                    resultSet.getString(9),
                                    resultSet.getString(10)
                            ),
                            resultSet.getInt(3)!=0 ? new Hub(
                                    resultSet.getInt(3),
                                    resultSet.getInt(11),
                                    resultSet.getString(12),
                                    resultSet.getString(13)
                            ) : null,
                            resultSet.getInt(7),
                            resultSet.getInt(8)
                    )
                );
            }
            return data;
        }
    }


    public static ArrayList<MetaPost> getTopMetaPosts(int offset, int limit) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM (SELECT p.id as post_id, p.author_id, p.hub_id, p.label, p.content, p.creation_time, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND v.vote) as likes, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND NOT v.vote) as dislikes, u.username, u.status, h.creator_id, h.hubname, h.description FROM posts p JOIN users u ON u.id=p.author_id LEFT JOIN hubs h ON h.id=p.hub_id) ORDER BY (likes-dislikes) DESC OFFSET ? LIMIT ?;"
            );
            statement.setInt(1, offset);
            statement.setInt(2, limit);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<MetaPost> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(
                        new MetaPost(
                                new Post(
                                        resultSet.getInt(1),
                                        resultSet.getInt(2),
                                        resultSet.getInt(3)!=0 ? resultSet.getInt(3) : null,
                                        resultSet.getString(4),
                                        resultSet.getString(5),
                                        new DateTime(resultSet.getTimestamp(6))
                                ),
                                new User(
                                        resultSet.getInt(2),
                                        resultSet.getString(9),
                                        resultSet.getString(10)
                                ),
                                resultSet.getInt(3)!=0 ? new Hub(
                                        resultSet.getInt(3),
                                        resultSet.getInt(11),
                                        resultSet.getString(12),
                                        resultSet.getString(13)
                                ) : null,
                                resultSet.getInt(7),
                                resultSet.getInt(8)
                        )
                );
            }
            return data;
        }
    }

    public static Integer metaPostCount() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM posts;");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Поле count в ответе из базы данных обязано существовать, однако отсутствует");
            }
        }
    }


    public static ArrayList<MetaPost> findMetaPosts(String query, int offset, int limit) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT p.id as post_id, p.author_id, p.hub_id, p.label, p.content, p.creation_time, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND v.vote) as likes, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND NOT v.vote) as dislikes, u.username, u.status, h.creator_id, h.hubname, h.description FROM posts p JOIN users u ON u.id=p.author_id LEFT JOIN hubs h ON h.id=p.hub_id WHERE p.label LIKE ? ORDER BY creation_time DESC OFFSET ? LIMIT ?;"
            );
            statement.setString(1, "%" + query + "%");
            statement.setInt(2, offset);
            statement.setInt(3, limit);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<MetaPost> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(
                        new MetaPost(
                                new Post(
                                        resultSet.getInt(1),
                                        resultSet.getInt(2),
                                        resultSet.getInt(3)!=0 ? resultSet.getInt(3) : null,
                                        resultSet.getString(4),
                                        resultSet.getString(5),
                                        new DateTime(resultSet.getTimestamp(6))
                                ),
                                new User(
                                        resultSet.getInt(2),
                                        resultSet.getString(9),
                                        resultSet.getString(10)
                                ),
                                resultSet.getInt(3)!=0 ? new Hub(
                                        resultSet.getInt(3),
                                        resultSet.getInt(11),
                                        resultSet.getString(12),
                                        resultSet.getString(13)
                                ) : null,
                                resultSet.getInt(7),
                                resultSet.getInt(8)
                        )
                );
            }
            return data;
        }
    }

    public static Integer findMetaPostCount(String query) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) FROM posts WHERE label LIKE ?;"
            );
            statement.setString(1, "%" + query + "%");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Поле count в ответе из базы данных обязано существовать, однако отсутствует");
            }
        }
    }

    public static ArrayList<MetaPost> getHubMetaPosts(int hubId, int offset, int limit) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT p.id as post_id, p.author_id, p.hub_id, p.label, p.content, p.creation_time, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND v.vote) as likes, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND NOT v.vote) as dislikes, u.username, u.status, h.creator_id, h.hubname, h.description FROM posts p JOIN users u ON u.id=p.author_id LEFT JOIN hubs h ON h.id=p.hub_id WHERE p.hub_id = ? ORDER BY creation_time DESC OFFSET ? LIMIT ?;"
            );
            statement.setInt(1, hubId);
            statement.setInt(2, offset);
            statement.setInt(3, limit);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<MetaPost> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(
                        new MetaPost(
                                new Post(
                                        resultSet.getInt(1),
                                        resultSet.getInt(2),
                                        resultSet.getInt(3)!=0 ? resultSet.getInt(3) : null,
                                        resultSet.getString(4),
                                        resultSet.getString(5),
                                        new DateTime(resultSet.getTimestamp(6))
                                ),
                                new User(
                                        resultSet.getInt(2),
                                        resultSet.getString(9),
                                        resultSet.getString(10)
                                ),
                                resultSet.getInt(3)!=0 ? new Hub(
                                        resultSet.getInt(3),
                                        resultSet.getInt(11),
                                        resultSet.getString(12),
                                        resultSet.getString(13)
                                ) : null,
                                resultSet.getInt(7),
                                resultSet.getInt(8)
                        )
                );
            }
            return data;
        }
    }

    public static Integer hubMetaPostCount(int hubId) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) FROM posts WHERE hub_id = ?;"
            );
            statement.setInt(1, hubId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Поле count в ответе из базы данных обязано существовать, однако отсутствует");
            }
        }
    }

    public static ArrayList<MetaPost> getUserMetaPosts(int userId, int offset, int limit) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT p.id as post_id, p.author_id, p.hub_id, p.label, p.content, p.creation_time, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND v.vote) as likes, (SELECT COUNT(*) FROM votes v WHERE v.post_id=p.id AND NOT v.vote) as dislikes, u.username, u.status, h.creator_id, h.hubname, h.description FROM posts p JOIN users u ON u.id=p.author_id LEFT JOIN hubs h ON h.id=p.hub_id WHERE p.author_id = ? ORDER BY creation_time DESC OFFSET ? LIMIT ?;"
            );
            statement.setInt(1, userId);
            statement.setInt(2, offset);
            statement.setInt(3, limit);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<MetaPost> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(
                        new MetaPost(
                                new Post(
                                        resultSet.getInt(1),
                                        resultSet.getInt(2),
                                        resultSet.getInt(3)!=0 ? resultSet.getInt(3) : null,
                                        resultSet.getString(4),
                                        resultSet.getString(5),
                                        new DateTime(resultSet.getTimestamp(6))
                                ),
                                new User(
                                        resultSet.getInt(2),
                                        resultSet.getString(9),
                                        resultSet.getString(10)
                                ),
                                resultSet.getInt(3)!=0 ? new Hub(
                                        resultSet.getInt(3),
                                        resultSet.getInt(11),
                                        resultSet.getString(12),
                                        resultSet.getString(13)
                                ) : null,
                                resultSet.getInt(7),
                                resultSet.getInt(8)
                        )
                );
            }
            return data;
        }
    }

    public static Integer userMetaPostCount(int userId) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT COUNT(*) FROM posts WHERE author_id = ?;"
            );
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Поле count в ответе из базы данных обязано существовать, однако отсутствует");
            }
        }
    }

    public static ArrayList<MetaPost> getLastMetaPosts(int page) throws SQLException {
        return getLastMetaPosts(page*pageSize, pageSize);
    }

    public static ArrayList<MetaPost> findMetaPosts(String query, int page) throws SQLException {
        return findMetaPosts(query, page*pageSize, pageSize);
    }

    public static ArrayList<MetaPost> getHubMetaPosts(int hubId, int page) throws SQLException {
        return getHubMetaPosts(hubId, page*pageSize, pageSize);
    }

    public static ArrayList<MetaPost> getUserMetaPosts(int userId, int page) throws SQLException {
        return getUserMetaPosts(userId, page*pageSize, pageSize);
    }

    public static ArrayList<MetaPost> getTopMetaPosts(int page) throws SQLException {
        return getTopMetaPosts(page*pageSize, pageSize);
    }

    public static int getPageSize(){
        return pageSize;
    }
}
