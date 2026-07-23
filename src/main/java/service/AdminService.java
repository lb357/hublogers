package service;

import model.common.TransactionResult;
import model.data.*;
import repository.data.*;

import java.util.ArrayList;

public class AdminService {
    private static String adminKey;

    public static String generateAdminKey(){
        adminKey = util.SecureStringGenerator.getTokenString();
        return adminKey;
    }

    public static TransactionResult<ArrayList<Hub>> getAllHubs (String key) {
        if (key.equals(adminKey)) {
            return HubRepository.getHubs();
        } else {
            return new TransactionResult<>("Неверный админ-код", 0);
        }
    }

    public static TransactionResult<ArrayList<Post>> getAllPosts (String key) {
        if (key.equals(adminKey)) {
            return PostRepository.getPosts();
        } else {
            return new TransactionResult<>("Неверный админ-код", 0);
        }
    }

    public static TransactionResult<ArrayList<Session>> getAllSession (String key) {
        if (key.equals(adminKey)) {
            return SessionRepository.getSessions();
        } else {
            return new TransactionResult<>("Неверный админ-код", 0);
        }
    }

    public static TransactionResult<ArrayList<User>> getAllUsers (String key) {
        if (key.equals(adminKey)) {
            return UserRepository.getUsers();
        } else {
            return new TransactionResult<>("Неверный админ-код", 0);
        }
    }

    public static TransactionResult<ArrayList<Vote>> getAllVotes (String key) {
        if (key.equals(adminKey)) {
            return VoteRepository.getVotes();
        } else {
            return new TransactionResult<>("Неверный админ-код", 0);
        }
    }
}
