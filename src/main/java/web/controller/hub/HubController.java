package web.controller.hub;

import com.sun.net.httpserver.HttpExchange;
import model.DataModel;
import model.composite.MetaHub;
import model.composite.MetaPost;
import service.ContentService;
import model.TransactionResult;
import web.StatusCode;
import web.ViewRenderer;
import web.controller.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class HubController extends Controller {
    public HubController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        ArrayList<MetaPost> posts;

        Integer id = parseIntegerQueryField(urlQuery, "id");
        if (assertAction(exchange, id!=null, "Хаб не найден")) return;
        TransactionResult<MetaHub> metaHubTransactionResult = ContentService.getHub(id);
        if (assertAction(exchange, metaHubTransactionResult)) return;
        TransactionResult<Integer> maxPageTransactionResult = ContentService.hubPostPages(id);
        if (assertAction(exchange, maxPageTransactionResult)) return;

        int maxPage = maxPageTransactionResult.getData();
        int page = getPage(maxPage, urlQuery);

        TransactionResult<ArrayList<MetaPost>> postsTransactionResult = ContentService.getHubPosts(id, page);
        if (assertAction(exchange, postsTransactionResult)) return;
        posts = postsTransactionResult.getData();

        sendHtml(exchange,
                ViewRenderer.fromResource("base").renderNav(isAuthenticated(exchange)).renderBase(
                        "hub/hub"
                ).renderString(
                        "max-page", Integer.toString(maxPage+1)
                ).renderString(
                        "current-page", Integer.toString(page+1)
                ).renderModel(metaHubTransactionResult.getData()).renderResourceModelList(
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
                ).renderIfString(
                        "authenticated-attribute",
                        !isAuthenticated(exchange),
                        "hidden",
                        ""
                ).get()
        );

    }

    @Override
    public void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException {
        sendStatusCode(exchange, StatusCode.METHOD_NOT_ALLOWED);
    }
}
