package web.controller;

import com.sun.net.httpserver.HttpExchange;
import model.domain.Session;
import service.AuthentificationService;
import service.TransactionResult;
import web.ViewRenderer;

import java.io.IOException;
import java.util.Map;

public class SignupController extends Controller {
    public SignupController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        sendHtml(exchange,
                ViewRenderer.fromResource("base").renderNav(isAuthenticated(exchange)).renderBase(
                        "auth/signup"
                ).get()
        );
    }

    @Override
    public void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException {
        if (bodyQuery!=null&&bodyQuery.containsKey("username")&&bodyQuery.containsKey("email")&&bodyQuery.containsKey("password")) {
            String username = decodeString(bodyQuery.get("username"));
            String email = decodeString(bodyQuery.get("email"));
            String password = decodeString(bodyQuery.get("password"));

            TransactionResult<Session> sessionTransactionResult = AuthentificationService.signupUser(username, email, password);
            if (assertAction(exchange, sessionTransactionResult)) return;
            Session session = sessionTransactionResult.getData();

            setAuthToken(exchange, session.getAuthToken());
            redirect(exchange, "/");
        } else {
            redirectToError(exchange, "Не были переданы username, email и password для регистрации");
        }
    }
}
