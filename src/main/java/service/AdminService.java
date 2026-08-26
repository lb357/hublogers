package service;

import config.AdminConfig;
import model.TransactionResult;
import model.composite.UserStatistic;
import model.domain.*;
import repository.composite.UserStatisticRepository;
import repository.data.*;
import util.AdminOutput;

import java.sql.SQLException;
import java.util.ArrayList;

public class AdminService {
    private static final AdminConfig config = new AdminConfig();

    public static String loadAdminKey(){
        config.load();
        return config.getAdminKey();
    }

    public static boolean checkAdminKey(String key) {
        return key.equals(config.getAdminKey());
    }

    public static TransactionResult<ArrayList<Hub>> getAllHubs (String key) {
        if (checkAdminKey(key)) {
            try {
                return TransactionResult.successResponse(HubRepository.getHubs());
            } catch (SQLException e) {
                AdminOutput.error(e);
                return TransactionResult.errorResponse();
            }
        } else {
            return TransactionResult.failResponse("Неверный админ-код");
        }
    }

    public static TransactionResult<ArrayList<Post>> getAllPosts (String key) {
        if (checkAdminKey(key)) {
            try {
                return TransactionResult.successResponse(PostRepository.getPosts());
            } catch (SQLException e) {
                AdminOutput.error(e);
                return TransactionResult.errorResponse();
            }
        } else {
            return TransactionResult.failResponse("Неверный админ-код");
        }
    }

    public static TransactionResult<ArrayList<Session>> getAllSessions(String key) {
        if (checkAdminKey(key)) {
            try {
                return TransactionResult.successResponse(SessionRepository.getSessions());
            } catch (SQLException e) {
                AdminOutput.error(e);
                return TransactionResult.errorResponse();
            }
        } else {
            return TransactionResult.failResponse("Неверный админ-код");
        }
    }

    public static TransactionResult<ArrayList<User>> getAllUsers (String key) {
        if (checkAdminKey(key)) {
            try {
                return TransactionResult.successResponse(UserRepository.getUsers());
            } catch (SQLException e) {
                AdminOutput.error(e);
                return TransactionResult.errorResponse();
            }
        } else {
            return TransactionResult.failResponse("Неверный админ-код");
        }
    }

    public static TransactionResult<ArrayList<Vote>> getAllVotes (String key) {
        if (checkAdminKey(key)) {
            try {
                return TransactionResult.successResponse(VoteRepository.getVotes());
            } catch (SQLException e) {
                AdminOutput.error(e);
                return TransactionResult.errorResponse();
            }
        } else {
            return TransactionResult.failResponse("Неверный админ-код");
        }
    }

    public static TransactionResult<ArrayList<UserStatistic>> getAllUsersStatistic(String key) {
        if (checkAdminKey(key)) {
            try {
                return TransactionResult.successResponse(UserStatisticRepository.getUsersStatistic());
            } catch (SQLException e) {
                AdminOutput.error(e);
                return TransactionResult.errorResponse();
            }
        } else {
            return TransactionResult.failResponse("Неверный админ-код");
        }
    }

    public static TransactionResult<Void> deletePost(String key, int postId) {
        if (checkAdminKey(key)) {
            try {
                int count = PostRepository.deletePost(postId);
                if (count > 0) {
                    return TransactionResult.successResponse();
                } else {
                    return TransactionResult.failResponse("Пост не был удалён");
                }
            } catch (SQLException e) {
                AdminOutput.error(e);
                return TransactionResult.errorResponse();
            }
        } else {
            return TransactionResult.failResponse("Неверный админ-код");
        }
    }

    public static TransactionResult<Void> deleteUser(String key, int userId) {
        if (checkAdminKey(key)) {
            try {
                int count = UserRepository.deleteUser(userId);
                if (count > 0) {
                    return TransactionResult.successResponse();
                } else {
                    return TransactionResult.failResponse("Пост не был удалён");
                }
            } catch (SQLException e) {
                AdminOutput.error(e);
                return TransactionResult.errorResponse();
            }
        } else {
            return TransactionResult.failResponse("Неверный админ-код");
        }
    }

    public static TransactionResult<Void> deleteHub(String key, int hubId) {
        if (checkAdminKey(key)) {
            try {
                int count = HubRepository.deleteHub(hubId);
                if (count > 0) {
                    return TransactionResult.successResponse();
                } else {
                    return TransactionResult.failResponse("Пост не был удалён");
                }
            } catch (SQLException e) {
                AdminOutput.error(e);
                return TransactionResult.errorResponse();
            }
        } else {
            return TransactionResult.failResponse("Неверный админ-код");
        }
    }

}
