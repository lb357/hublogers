package web.controller.post;

import com.sun.net.httpserver.HttpExchange;
import model.domain.Post;
import service.ContentService;
import model.TransactionResult;
import web.ViewRenderer;
import web.controller.Controller;

import java.io.IOException;
import java.util.Map;

public class CreatePostController extends Controller {
    public CreatePostController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        sendHtml(exchange,
            ViewRenderer.fromResource(
                    "base"
            ).renderNav(
                    isAuthenticated(exchange)
            ).renderBase(
                    "post/create-post"
            ).renderIfString(
                    "hub-id-attribute",
                    urlQuery.containsKey("hub-id"),
                    decodeString(urlQuery.getOrDefault("hub-id", "")),
                    ""
            ).get(

            )
        );
    }

    @Override
    public void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException {
        if (isAuthenticated(exchange)) {
            Integer hubId = parseIntegerQueryField(bodyQuery, "hub-id");
            TransactionResult<Post> postTransactionResult = ContentService.createPost(getAuthToken(exchange), hubId, decodeString(bodyQuery.get("label")), decodeString(bodyQuery.get("content")));
            if (postTransactionResult.isSuccess()) {
                redirect(exchange, "/profile");
            } else {
                redirectToError(exchange, postTransactionResult.getMessage());
            }
        } else {
            redirectToError(exchange, "Переданы некорректные параметры");
        }
    }
}
