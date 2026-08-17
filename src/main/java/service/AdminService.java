package service;

import model.common.TransactionResult;
import model.composite.MetaPost;
import model.composite.UserStatistic;
import model.data.*;
import repository.composite.UserStatisticRepository;
import repository.data.*;
import util.AdminOutput;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminService {
    private static String adminKey;

    public static String loadAdminKey(){
        boolean loaded = false;
        try (FileReader fileReader = new FileReader("admin.key")){
            Scanner scanner = new Scanner(fileReader);
            adminKey = scanner.nextLine();
            loaded = true;
        }
        catch (FileNotFoundException ignored) {}
        catch (IOException e) {
            AdminOutput.error(e);
        }
        if (!loaded) {
            adminKey = util.SecureStringGenerator.getTokenString();
            try (FileWriter fileWriter = new FileWriter("admin.key")) {
                fileWriter.write(adminKey);
            } catch (IOException e) {
                AdminOutput.error(e);
            }
        }
        return adminKey;
    }

    public static boolean checkAdminKey(String key) {
        return key.equals(adminKey);
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

    public static TransactionResult<ArrayList<Session>> getAllSession (String key) {
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
