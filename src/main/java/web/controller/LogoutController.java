package web.controller;

import com.sun.net.httpserver.HttpExchange;
import web.StatusCode;
import web.ViewRenderer;

import java.io.IOException;
import java.util.Map;

public class LogoutController extends Controller {
    public LogoutController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        deleteAuthToken(exchange);
        redirect(exchange, "/");
    }

    @Override
    public void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException {
        sendStatusCode(exchange, StatusCode.METHOD_NOT_ALLOWED);
    }
}
