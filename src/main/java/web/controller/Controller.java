package web.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.domain.User;
import service.AuthentificationService;
import model.TransactionResult;
import web.StatusCode;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class Controller implements HttpHandler {
    protected final String path;

    protected final String ERROR_URL = "/error?message=%s&from=%s";

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
                    queryMap.put(entries[0], entries[1]);
                } catch (IndexOutOfBoundsException ignored) {}
            }
        }
        return queryMap;
    }

    public abstract void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException;
    public abstract void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException;

    public void sendHtml(HttpExchange exchange, String html, StatusCode statusCode) throws IOException {
        byte[] body = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        sendStatusCode(exchange, statusCode, body.length);
        try (OutputStream output = exchange.getResponseBody()){
            output.write(body);
        }
    }

    public void sendHtml(HttpExchange exchange, String html) throws IOException {
        sendHtml(exchange, html, StatusCode.OK);
    }


    public void sendCss(HttpExchange exchange, String css) throws IOException {
        byte[] body = css.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/css; charset=UTF-8");
        sendStatusCode(exchange, StatusCode.OK, body.length);
        try (OutputStream output = exchange.getResponseBody()){
            output.write(body);
        }
    }

    public void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        sendStatusCode(exchange, StatusCode.REDIRECT);
    }

    protected String encodeString(String data) {
        return URLEncoder.encode(data, StandardCharsets.UTF_8).replace("+", "%20");
    }

    protected String decodeString(String data) {
        return URLDecoder.decode(data, StandardCharsets.UTF_8);
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

    public boolean isAuthenticated(HttpExchange exchange) throws IOException {
        String authToken = getAuthToken(exchange);
        if (authToken.isEmpty()) {return false;}
        else {
            TransactionResult<User> transactionResult = AuthentificationService.authUser(getAuthToken(exchange));
            if (transactionResult.isSuccess() && transactionResult.getData() != null) {
                return true;
            } else {
                deleteAuthToken(exchange);
                return false;
            }
        }
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
            return Integer.parseInt(decodeString(query.get(field)));
        } catch (NumberFormatException e) {
            return null;
        }

    }

    public void redirectToError(HttpExchange exchange, String errorMessage) throws IOException {
        redirect(
                exchange,
                ERROR_URL.formatted(
                        encodeString(errorMessage),
                        encodeString(exchange.getRequestURI().getPath())
                )
        );
    }

    public boolean assertAction(HttpExchange exchange, boolean isOk, String errorMessage) throws IOException {
        if (!isOk){
            redirectToError(exchange, errorMessage);
            return true;
        } else {
            return false;
        }
    }

    public boolean assertAction(HttpExchange exchange, TransactionResult<?> transactionResult) throws IOException {
        return assertAction(exchange, transactionResult.isSuccess(), transactionResult.getMessage());
    }

    public int getPage(int maxPage, Map<String, String> urlQuery, String pageField){
        Integer page = parseIntegerQueryField(urlQuery, pageField);
        if (page != null) {
            page--;
        } else {
            page=0;
        }
        return Math.max(0, Math.min(page, maxPage));
    }

    public int getPage(int maxPage, Map<String, String> urlQuery) {
        return getPage(maxPage, urlQuery, "page");
    }
}
