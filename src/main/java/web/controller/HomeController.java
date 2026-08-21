package web.controller;

import com.sun.net.httpserver.HttpExchange;
import web.ViewRenderer;

import java.io.IOException;
import java.util.Map;

public class HomeController extends Controller {
    public HomeController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> query) throws IOException {
        sendHTML(exchange, ViewRenderer.loadViewResource("index.html"));
    }
}
