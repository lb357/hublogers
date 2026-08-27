package web.controller.auth;

import com.sun.net.httpserver.HttpExchange;
import service.AuthentificationService;
import web.StatusCode;
import web.controller.Controller;

import java.io.IOException;
import java.util.Map;

public class LogoutController extends Controller {
    public LogoutController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        AuthentificationService.logoutUser(getAuthToken(exchange));
        deleteAuthToken(exchange);
        redirect(exchange, "/");
    }

    @Override
    public void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException {
        sendStatusCode(exchange, StatusCode.METHOD_NOT_ALLOWED);
    }
}
