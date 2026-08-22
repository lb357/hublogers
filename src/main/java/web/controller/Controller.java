package web.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import web.StatusCode;

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
        if (exchange.getRequestURI().getPath().equals(path)) {
            if (method.equalsIgnoreCase("GET")) {
                get(exchange, parseQuery(exchange.getRequestURI().getQuery()));
            } else if (method.equalsIgnoreCase("POST")) {
                String body = new String(
                        exchange.getRequestBody().readAllBytes(),
                        StandardCharsets.UTF_8
                );
                post(exchange, parseQuery(body));
            } else {
                sendStatusCode(exchange, StatusCode.METHOD_NOT_ALLOWED);
            }

        }
        exchange.close();
    }

    public Map<String, String> parseQuery(String queryString) {
        Map<String, String> queryMap = new HashMap<>();
        if (queryString != null) {
            for (String kwarg : queryString.split("&")) {
                try {
                    String[] entries = kwarg.split("=");
                    queryMap.put(entries[0], entries[entries.length - 1]);
                } catch (IndexOutOfBoundsException ignored) {}
            }
        }
        return queryMap;
    }

    public abstract void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException;
    public abstract void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException;

    public void sendHTML(HttpExchange exchange, String html) throws IOException {
        byte[] body = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        sendStatusCode(exchange, StatusCode.OK, body.length);
        try (OutputStream output = exchange.getResponseBody()){
            output.write(body);
        }
    }

    public void sendCSS(HttpExchange exchange, String css) throws IOException {
        byte[] body = css.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/css; charset=UTF-8");
        sendStatusCode(exchange, StatusCode.OK, body.length);
        try (OutputStream output = exchange.getResponseBody()){
            output.write(body);
        }
    }

    public void sendStatusCode(HttpExchange exchange, StatusCode statusCode, int length) throws IOException {
        exchange.sendResponseHeaders(statusCode.getCode(), length);
    }

    public void sendStatusCode(HttpExchange exchange, StatusCode statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode.getCode(), -1);
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
