package web.controller;

import com.sun.net.httpserver.HttpExchange;
import model.TransactionResult;
import model.composite.MetaPost;
import model.domain.Post;
import model.domain.User;
import service.AuthentificationService;
import service.ContentService;
import web.ViewRenderer;

import java.io.IOException;
import java.util.Map;

public class EditPostController extends Controller {
    public EditPostController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        Integer id = parseIntegerQueryField(urlQuery, "id");

        TransactionResult<MetaPost> postTransactionResult = ContentService.getPost(id);
        if (assertAction(exchange, postTransactionResult)) return;
        MetaPost post = postTransactionResult.getData();

        TransactionResult<User> userTransactionResult = AuthentificationService.authUser(getAuthToken(exchange));
        if (assertAction(exchange, userTransactionResult)) return;
        User user = userTransactionResult.getData();

        if (!user.getId().equals(post.getAuthor().getId())){
            redirectToError(exchange, "Не автор");
            return;
        }

        sendHtml(exchange,
                ViewRenderer.fromResource("base").renderNav(isAuthenticated(exchange)).renderBase(
                        "post/edit-post"
                ).renderModel(post).get()
        );

    }

    @Override
    public void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException {
        Integer id = parseIntegerQueryField(bodyQuery, "id");

        TransactionResult<Post> postTransactionResult = ContentService.updatePost(
                getAuthToken(exchange),
                id,
                decodeString(bodyQuery.get("content"))
        );
        if (assertAction(exchange, postTransactionResult)) return;
        redirect(exchange, "/post?id=%s".formatted(id));
    }
}
