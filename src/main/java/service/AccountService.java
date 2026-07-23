package service;

import model.common.TransactionResult;
import model.composite.UserStatistic;
import model.data.Session;
import model.data.User;
import repository.composite.UserStatisticRepository;
import repository.data.SessionRepository;
import repository.data.UserRepository;
import util.SecureStringGenerator;

public class AccountService {
    public static TransactionResult<Session> signupUser(String username, String email, String password) {
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return new TransactionResult<>("Имя должно содержать только латинские буквы, цифры, подчеркивания", 0);
        }
        if (!username.matches("^\\\\S+@\\\\S+\\\\.\\\\S+$")) {
            return new TransactionResult<>("Некорректный email", 0);
        }
        if (password.length()<6) {
            return new TransactionResult<>("Пароль должен содержать хотя бы 6 символов", 0);
        }
        TransactionResult<User> userTransactionResult = UserRepository.createUser(
                username,
                email,
                SecureStringGenerator.getSHA256String(password)
        );
        if (userTransactionResult.isSuccess()) {
            return SessionRepository.createSession(
                    SecureStringGenerator.getTokenString(),
                    userTransactionResult.getData().getId()
            );
        } else {
            return userTransactionResult.transferFail();
        }
    }

    public static TransactionResult<Session> loginUser(String email, String password) {
        TransactionResult<User> userTransactionResult = UserRepository.readUser(
                email,
                password
        );
        if (userTransactionResult.isSuccess()) {
            return SessionRepository.createSession(
                    SecureStringGenerator.getTokenString(),
                    userTransactionResult.getData().getId()
            );
        } else {
            return userTransactionResult.transferFail();
        }
    }

    public static TransactionResult<String> logoutUser(String authToken) {
        return SessionRepository.deleteSession(authToken);
    }

    public static TransactionResult<User> authUser(String authToken) {
        TransactionResult<Integer> sessionTransactionResult = SessionRepository.getUserIdByAuthToken(authToken);
        if (sessionTransactionResult.isSuccess()) {
            return UserRepository.readUser(sessionTransactionResult.getData());
        } else {
            return sessionTransactionResult.transferFail();
        }
    }

    public static TransactionResult<UserStatistic> statistic(String authToken) {
        TransactionResult<User> userTransactionResult = authUser(authToken);
        if (userTransactionResult.isSuccess()){
            return UserStatisticRepository.getUserStatistic(userTransactionResult.getData().getId());
        } else {
            return userTransactionResult.transferFail();
        }
    }
}
