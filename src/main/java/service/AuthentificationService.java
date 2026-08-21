package service;

import model.domain.Session;
import model.domain.User;
import repository.data.SessionRepository;
import repository.data.UserRepository;
import util.AdminOutput;
import util.SecureStringGenerator;

import java.sql.SQLException;

public class AuthentificationService {
    public static TransactionResult<Session> signupUser(String username, String email, String password) {
        if (username != null) {
            if (!username.matches("^[a-zA-Z0-9_]+")) {
                return TransactionResult.failResponse("Имя пользователя должно содержать только латинские буквы, цифры, подчеркивания");
            }
            if (username.length() < 4 || username.length() > 16) {
                return TransactionResult.failResponse("Имя пользователя должно содержать от 4 до 16 символов");
            }
        } else {
            return TransactionResult.failResponse("Отсутствует имя пользователя");
        }

        if (email != null) {
            if (!email.matches("^.*@.*")) {
                return TransactionResult.failResponse("Некорректный адрес электронной почты");
            }
        } else {
            return TransactionResult.failResponse("Отсутствует адрес электронной почты");
        }

        if (password != null) {
            if (password.length() < 6) {
                return TransactionResult.failResponse("Пароль должен содержать хотя бы 6 символов");
            }
        } else {
            return TransactionResult.failResponse("Отсутствует пароль");
        }

        try {
            User user = UserRepository.createUser(
                    username,
                    email,
                    SecureStringGenerator.getSHA256String(password)
            );
            if (user != null) {
                Session session = SessionRepository.createSession(SecureStringGenerator.getTokenString(), user.getId());
                return TransactionResult.successResponse(session);
            } else {
                return TransactionResult.failResponse("Пользователь с таким именем уже существует");
            }
        } catch (SQLException e){
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<Session> loginUser(String email, String password) {
        try {
            if (email == null || password == null) {
                return TransactionResult.failResponse("Отсутствует имя пользователя или неверный пароль");
            }

            User user = UserRepository.readUser(
                    email,
                    password
            );
            if (user != null) {
                Session session =  SessionRepository.createSession(
                        SecureStringGenerator.getTokenString(),
                        user.getId()
                );
                return TransactionResult.successResponse(session);
            } else {
                return TransactionResult.failResponse("Неизвестное имя пользователя или неверный пароль");
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<Void> logoutUser(String authToken) {
        try {
            if (authToken != null) {
                int count = SessionRepository.deleteSession(authToken);
                if (count > 0) {
                    return TransactionResult.successResponse();
                } else {
                    return TransactionResult.failResponse("Токен не был удален");
                }
            } else {
                return TransactionResult.failResponse("Неизвестный токен");
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }

    public static TransactionResult<User> authUser(String authToken) {
        if (authToken == null || authToken.isBlank()) {
            return TransactionResult.failResponse("Отсутствует токен");
        }
        try {
            Integer id = SessionRepository.getUserIdByAuthToken(authToken);
            if (id != null) {
                User user = UserRepository.readUser(id);
                if (user != null) {
                    return TransactionResult.successResponse(user);
                } else {
                    return TransactionResult.failResponse("Неизвестный пользователь");
                }
            } else {
                return TransactionResult.failResponse("Токен невалиден (возможно истек)");
            }
        } catch (SQLException e) {
            AdminOutput.error(e);
            return TransactionResult.errorResponse();
        }
    }
}
