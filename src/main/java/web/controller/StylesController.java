package web.controller;

import com.sun.net.httpserver.HttpExchange;
import web.ViewResource;

import java.io.IOException;
import java.util.Map;

public class StylesController extends Controller{
    public StylesController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        sendCss(exchange, ViewResource.loadCss("styles").getData());
    }

    @Override
    public void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException {
        sendCss(exchange, ViewResource.loadCss("styles").getData());
    }
}
