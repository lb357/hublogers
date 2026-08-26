package cli;

import model.TransactionResult;
import model.domain.User;
import service.AuthentificationService;

public class AuthStorage {
    private static String authToken = "";

    public static boolean isAuthenticated() {
        if (authToken.isBlank()) return false;
        else {
            TransactionResult<User> userTransactionResult = AuthentificationService.authUser(authToken);
            if (userTransactionResult.isSuccess()) {
                return true;
            } else {
                authToken = "";
                return false;
            }
        }
    }

    public static void deleteAuthToken() {
        authToken = "";
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        if (authToken == null || authToken.isBlank()) {
            throw new IllegalArgumentException("Отсутствует authToken");
        }
        AuthStorage.authToken = authToken;
    }

}
