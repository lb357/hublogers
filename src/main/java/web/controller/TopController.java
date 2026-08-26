package web.controller;

import com.sun.net.httpserver.HttpExchange;
import model.DataModel;
import model.composite.MetaPost;
import service.ContentService;
import service.TransactionResult;
import web.StatusCode;
import web.ViewRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class TopController extends Controller {
    public TopController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        TransactionResult<Integer> maxPageTransactionResult = ContentService.getPostPages();
        if (assertAction(exchange, maxPageTransactionResult)) return;

        int maxPage = maxPageTransactionResult.getData();
        int page = getPage(maxPage, urlQuery);

        TransactionResult<ArrayList<MetaPost>> postsTransactionResult = ContentService.getTopPosts(page);
        if (assertAction(exchange, postsTransactionResult)) return;
        ArrayList<MetaPost> posts = postsTransactionResult.getData();



        sendHtml(exchange,
                ViewRenderer.fromResource("base").renderNav(isAuthenticated(exchange)).renderBase(
                        "post/top-posts"
                ).renderString(
                        "max-page", Integer.toString(maxPage+1)
                ).renderString(
                        "current-page", Integer.toString(page+1)
                ).renderResourceModelList(
                        "result",
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
                ).get()
        );
    }

    @Override
    public void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException {
        sendStatusCode(exchange, StatusCode.METHOD_NOT_ALLOWED);
    }
}
