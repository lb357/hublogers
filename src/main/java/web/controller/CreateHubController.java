package web.controller;

import com.sun.net.httpserver.HttpExchange;
import model.domain.Hub;
import service.ContentService;
import model.TransactionResult;
import web.ViewRenderer;

import java.io.IOException;
import java.util.Map;

public class CreateHubController extends Controller {
    public CreateHubController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        sendHtml(exchange,
                ViewRenderer.fromResource("base").renderNav(isAuthenticated(exchange)).renderBase(
                        "hub/create-hub"
                ).get()
        );
    }

    @Override
    public void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException {
        if (isAuthenticated(exchange)) {
            TransactionResult<Hub> hubTransactionResult = ContentService.createHub(getAuthToken(exchange), decodeString(bodyQuery.get("hubname")), decodeString(bodyQuery.get("description")));
            if (hubTransactionResult.isSuccess()) {
                redirect(exchange, "/profile");
            } else {
                redirectToError(exchange, hubTransactionResult.getMessage());
            }
        } else {
            redirectToError(exchange, "Переданы некорректные параметры");
        }
    }
}
