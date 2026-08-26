package web.controller;

import com.sun.net.httpserver.HttpExchange;
import model.composite.MetaPost;
import model.domain.Vote;
import service.ContentService;
import service.TransactionResult;
import web.StatusCode;
import web.ViewRenderer;

import java.io.IOException;
import java.util.Map;

public class PostController extends Controller {
    public PostController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        Integer id = parseIntegerQueryField(urlQuery, "id");
        if (id==null) {
            redirectToError(exchange, "Некорректный id поста");
            return;
        }

        int voteDelta = 0;
        if (isAuthenticated(exchange)) {
            TransactionResult<Vote> voteTransactionResult = ContentService.getVote(getAuthToken(exchange), id);
            if (voteTransactionResult.isSuccess()){
                Vote vote = voteTransactionResult.getData();
                voteDelta = vote.getVoteDelta();
            }
        }

        TransactionResult<MetaPost> postTransactionResult = ContentService.getPost(id);
        if (assertAction(exchange, postTransactionResult)) return;
        MetaPost post = postTransactionResult.getData();

        sendHtml(exchange,
                ViewRenderer.fromResource("base").renderNav(isAuthenticated(exchange)).renderBase(
                        "post/post"
                ).renderIfResource(
                        "votes",
                        isAuthenticated(exchange),
                        "vote/votes-form",
                        "vote/votes-display"
                ).renderIfString(
                        "like-attribute",
                        isAuthenticated(exchange)&&voteDelta>=0,
                        "",
                        "disabled"
                ).renderIfString(
                        "dislike-attribute",
                        isAuthenticated(exchange)&&voteDelta<=0,
                        "",
                        "disabled"
                ).renderIfString(
                        "hub-attribute",
                        post.getHub() == null,
                        "hidden", ""
                ).renderModel(post).get()
        );

    }

    @Override
    public void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException {
        Integer id = parseIntegerQueryField(bodyQuery, "id");
        if (id == null || !bodyQuery.containsKey("vote") || !isAuthenticated(exchange)) {
            redirectToError(exchange, "Переданные параметры не позволяют оставить голос");
            return;
        }
        String vote = bodyQuery.get("vote");
        if (vote.equals("like")){
            TransactionResult<Vote> voteTransactionResult = ContentService.likePost(getAuthToken(exchange), id);
            if (assertAction(exchange, voteTransactionResult)) return;
        } else if (vote.equals("dislike")) {
            TransactionResult<Vote> voteTransactionResult = ContentService.dislikePost(getAuthToken(exchange), id);
            if (assertAction(exchange, voteTransactionResult)) return;
        } else {
            redirectToError(exchange, "Неизвестный тип голоса");
            return;
        }

        redirect(exchange, "/post?id=%s".formatted(id));
    }
}
