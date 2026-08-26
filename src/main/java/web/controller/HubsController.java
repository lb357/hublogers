package web.controller;

import com.sun.net.httpserver.HttpExchange;
import model.DataModel;
import model.composite.MetaHub;
import service.ContentService;
import service.TransactionResult;
import web.StatusCode;
import web.ViewRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class HubsController extends Controller {
    public HubsController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        TransactionResult<Integer> maxPageTransactionResult = ContentService.hubPages();
        if (assertAction(exchange, maxPageTransactionResult)) return;

        int maxPage = maxPageTransactionResult.getData();
        int page = getPage(maxPage, urlQuery);

        TransactionResult<ArrayList<MetaHub>> hubsTransactionResult = ContentService.getHubs(page);
        if (assertAction(exchange, hubsTransactionResult)) return;
        ArrayList<MetaHub> hubs = hubsTransactionResult.getData();


        sendHtml(exchange,
                ViewRenderer.fromResource("base").renderNav(isAuthenticated(exchange)).renderBase(
                        "hub/hubs"
                ).renderString(
                        "max-page", Integer.toString(maxPage+1)
                ).renderString(
                        "current-page", Integer.toString(page+1)
                ).renderResourceModelList(
                        "result",
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
