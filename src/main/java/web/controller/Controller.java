package web.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class Controller implements HttpHandler {
    protected final String path;

    protected Controller(String path) {
        this.path = path;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            if (exchange.getRequestURI().getPath().equals(path)) {
                Map<String, String> queryMap = new HashMap<>();
                String queryString = exchange.getRequestURI().getQuery();
                if (queryString != null){
                    for (String kwarg : queryString.split("&")) {
                        String[] entries = kwarg.split("=");
                        queryMap.put(entries[0], entries[entries.length - 1]);
                    }
                }
                get(exchange, queryMap);
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
        exchange.close();
    }

    public abstract void get(HttpExchange exchange, Map<String, String> query) throws IOException;

    public void sendHTML(HttpExchange exchange, String html) throws IOException {
        byte[] body = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream output = exchange.getResponseBody()){
            output.write(body);
        }
    }

    public void sendCSS(HttpExchange exchange, String css) throws IOException {
        byte[] body = css.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/css; charset=UTF-8");
        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream output = exchange.getResponseBody()){
            output.write(body);
        }
    }

    public void setAuthToken(HttpExchange exchange, String authToken) throws IOException {
        exchange.getResponseHeaders().add("Set-Cookie", "authToken=%s; Path=/; HttpOnly".formatted(authToken));
    }

    public String getAuthToken(HttpExchange exchange) throws IOException {
        String header = exchange.getRequestHeaders().getFirst("Cookie");
        String cookieField = "authToken=";
        if (header == null) return "";
        for (String cookie: header.split(";")) {
            if (cookie.startsWith(cookieField)) {
                return cookie.substring(cookieField.length());
            }
        }
        return "";
    }

    public void deleteAuthToken(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Set-Cookie", "authToken=; Path=/; Max-Age=0; HttpOnly");
    }

    public String getPath() {
        return path;
    }

    public Integer parseIntegerQueryField(Map<String, String> query, String field) {
        if (field == null || query == null) return null;
        if (!query.containsKey(field)) return null;
        try {
            return Integer.parseInt(query.get(field));
        } catch (NumberFormatException e) {
            return null;
        }

    }
}
