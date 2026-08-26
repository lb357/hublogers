package web.controller;

import com.sun.net.httpserver.HttpExchange;
import model.composite.UserStatistic;
import model.domain.*;
import service.AdminService;
import service.TransactionResult;
import web.ViewRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class AdminController extends Controller {
    public AdminController(String path) {
        super(path);
    }

    @Override
    public void get(HttpExchange exchange, Map<String, String> urlQuery) throws IOException {
        sendHtml(exchange,
                ViewRenderer.fromResource("base").renderNav(isAuthenticated(exchange)).renderBase(
                        "admin/admin-login"
                ).get()
        );
    }

    @Override
    public void post(HttpExchange exchange, Map<String, String> bodyQuery) throws IOException {
        if (bodyQuery!=null&&bodyQuery.containsKey("admin-key")&& AdminService.checkAdminKey(decodeString(bodyQuery.get("admin-key")))) {
            String adminKey = decodeString(bodyQuery.get("admin-key"));
            if (bodyQuery.containsKey("entity")&&bodyQuery.containsKey("id")&&parseIntegerQueryField(bodyQuery, "id")!=null) {
                int id = parseIntegerQueryField(bodyQuery, "id");
                TransactionResult<Void> result = TransactionResult.errorResponse();
                switch (decodeString(bodyQuery.get("entity"))) {
                    case "post" -> {
                        result = AdminService.deletePost(adminKey, id);
                    }
                    case "user" -> {
                        result = AdminService.deleteUser(adminKey, id);
                    }
                    case "hub" -> {
                        result = AdminService.deleteHub(adminKey, id);
                    }
                }
                if (assertAction(exchange, result)) return;
            }
            TransactionResult<ArrayList<Vote>> votesTransactionResult = AdminService.getAllVotes(adminKey);
            TransactionResult<ArrayList<Post>> postsTransactionResult = AdminService.getAllPosts(adminKey);
            TransactionResult<ArrayList<Hub>> hubsTransactionResult = AdminService.getAllHubs(adminKey);
            TransactionResult<ArrayList<Session>> sessionsTransactionResult = AdminService.getAllSessions(adminKey);
            TransactionResult<ArrayList<User>> usersTransactionResult = AdminService.getAllUsers(adminKey);
            TransactionResult<ArrayList<UserStatistic>> usersStatisticTransactionResult = AdminService.getAllUsersStatistic(adminKey);
            if (assertAction(exchange, votesTransactionResult)) return;
            if (assertAction(exchange, postsTransactionResult)) return;
            if (assertAction(exchange, hubsTransactionResult)) return;
            if (assertAction(exchange, sessionsTransactionResult)) return;
            if (assertAction(exchange, usersTransactionResult)) return;
            if (assertAction(exchange, usersStatisticTransactionResult)) return;



            sendHtml(exchange,
                    ViewRenderer.fromResource("base").renderNav(isAuthenticated(exchange)).renderBase(
                            "admin/admin"
                    ).renderString(
                            "votes-table",
                            ViewRenderer.fromResource("table").renderTable(
                                    "Данные из таблицы votes",
                                    new String[] {"postId", "voterId", "vote"},
                                    votesTransactionResult.getData()
                            ).get(),
                            false
                    ).renderString(
                            "posts-table",
                            ViewRenderer.fromResource("table").renderTable(
                                    "Данные из таблицы posts",
                                    new String[] {"id", "authorId", "hubId", "label", "content", "creationTime"},
                                    postsTransactionResult.getData()
                            ).get(),
                            false
                    ).renderString(
                            "hubs-table",
                            ViewRenderer.fromResource("table").renderTable(
                                    "Данные из таблицы hubs",
                                    new String[] {"id", "creatorId", "hubname", "description"},
                                    hubsTransactionResult.getData()
                            ).get(),
                            false
                    ).renderString(
                            "session-table",
                            ViewRenderer.fromResource("table").renderTable(
                                    "Данные из таблицы sessions",
                                    new String[] {"authToken", "userId", "authTime"},
                                    sessionsTransactionResult.getData()
                            ).get(),
                            false
                    ).renderString(
                            "users-table",
                            ViewRenderer.fromResource("table").renderTable(
                                    "Данные из таблицы users",
                                    new String[] {"id", "username", "email", "passwordHash", "status"},
                                    usersTransactionResult.getData()
                            ).get(),
                            false
                    ).renderString(
                            "statistic-table",
                            ViewRenderer.fromResource("table").renderTable(
                                    "Статистика пользователей",
                                    new String[] {"user.id", "user.username", "postCount", "hubCount", "voteCount", "activeSessionCount"},
                                    usersStatisticTransactionResult.getData()
                            ).get(),
                            false
                    ).renderString("admin-key", adminKey).get()
            );

        } else {
            redirectToError(exchange, "Неверный админ-код");
        }
    }
}
