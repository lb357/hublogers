package web.controller.auth;

import com.sun.net.httpserver.HttpExchange;
import model.domain.Session;
import service.AuthentificationService;
import model.TransactionResult;
import web.ViewRenderer;
import web.controller.Controller;

import java.io.IOException;
import java.util.Map;

public class LoginController extends Controller {
    public LoginController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        if (isAuthenticated(exchange)) redirect(exchange, "/profile");
        sendHtml(exchange,
                ViewRenderer.fromResource("base").renderNav(isAuthenticated(exchange)).renderBase(
                        "auth/login"
                ).get()
        );
    }

    @Override
    public void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException {
        if (isAuthenticated(exchange)) redirect(exchange, "/profile");
        String email = decodeString(bodyQuery.get("email"));
        String password = decodeString(bodyQuery.get("password"));

        TransactionResult<Session> sessionTransactionResult = AuthentificationService.loginUser(email, password);
        if (assertAction(exchange, sessionTransactionResult)) return;
        Session session = sessionTransactionResult.getData();

        setAuthToken(exchange, session.getAuthToken());
        redirect(exchange, "/");
    }
}
