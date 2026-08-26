package web.controller;

import com.sun.net.httpserver.HttpExchange;
import web.StatusCode;
import web.ViewRenderer;

import java.io.IOException;
import java.util.Map;

public class HomeController extends Controller {
    public HomeController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        sendHtml(exchange,
                ViewRenderer.fromResource("base").renderNav(isAuthenticated(exchange)).renderBase(
                        "home"
                ).get()
        );
    }

    @Override
    public void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException {
        sendStatusCode(exchange, StatusCode.METHOD_NOT_ALLOWED);
    }
}
