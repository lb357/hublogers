package web.controller;

import com.sun.net.httpserver.HttpExchange;
import model.DataModel;
import model.domain.User;
import model.composite.MetaHub;
import model.composite.MetaPost;
import service.ContentService;
import service.TransactionResult;
import web.StatusCode;
import web.ViewRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class UserController extends Controller {
    public UserController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        Integer id = parseIntegerQueryField(urlQuery, "id");
        User user;
        if (id!=null) {
            TransactionResult<User> userTransactionResult = ContentService.getUser(id);
            if (assertAction(exchange, userTransactionResult)) return;
            user = userTransactionResult.getData();
        } else {
            redirectToError(exchange, "Некорректный id пользователя");
            return;
        }

        TransactionResult<Integer> maxPostPageTransactionResult = ContentService.userPostPages(id);
        if (assertAction(exchange, maxPostPageTransactionResult)) return;
        int maxPostPage = maxPostPageTransactionResult.getData();

        TransactionResult<Integer> maxHubPageTransactionResult = ContentService.userHubPages(id);
        if (assertAction(exchange, maxHubPageTransactionResult)) return;
        int maxHubPage = maxHubPageTransactionResult.getData();

        int postPage = getPage(maxPostPage, urlQuery, "post-page");
        int hubPage = getPage(maxHubPage, urlQuery, "hub-page");

        TransactionResult<ArrayList<MetaPost>> postsTransactionResult = ContentService.getUserPosts(id, postPage);
        if (assertAction(exchange, postsTransactionResult)) return;
        ArrayList<MetaPost> posts = postsTransactionResult.getData();

        TransactionResult<ArrayList<MetaHub>> hubsTransactionResult = ContentService.getUserHubs(id, hubPage);
        if (assertAction(exchange, hubsTransactionResult)) return;
        ArrayList<MetaHub> hubs = hubsTransactionResult.getData();


        sendHtml(exchange,
                ViewRenderer.fromResource("base").renderNav(isAuthenticated(exchange)).renderBase(
                        "user/user"
                ).renderModel(user).renderString(
                        "max-post-page", Integer.toString(maxPostPage+1)
                ).renderString(
                        "current-post-page", Integer.toString(postPage+1)
                ).renderResourceModelList(
                        "post-result",
                        (DataModel model) -> ViewRenderer.fromResource(
                                "post/post-model"
                        ).renderResource(
                                "vote/votes-display"
                        ).renderIfString(
                                "hub-attribute",
                                ((MetaPost)model).getHub() == null,
                                "hidden",
                                ""
                        ).renderModel(
                                model
                        ).get(),
                        posts
                ).renderString(
                        "max-hub-page", Integer.toString(maxHubPage+1)
                ).renderString(
                        "current-hub-page", Integer.toString(hubPage+1)
                ).renderResourceModelList(
                        "hub-result",
                        (DataModel model) -> ViewRenderer.fromResource("hub/hub-model").renderModel(model).get(),
                        hubs
                ).get()
        );

    }

    @Override
    public void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException {
        sendStatusCode(exchange, StatusCode.METHOD_NOT_ALLOWED);
    }
}
