package cli.menu;

import cli.AuthStorage;
import cli.ConsoleApp;
import model.common.TransactionResult;
import model.composite.MetaHub;
import model.composite.MetaPost;
import model.composite.UserStatistic;
import model.data.*;
import service.AdminService;
import service.AuthentificationService;
import service.ContentService;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.BiConsumer;

public class MenuLogic {
    private static final Scanner scanner = new Scanner(System.in);

    public static void renderHome() {
        MenuElement[] menu;
        if (AuthStorage.isAuthenticated()) {
            menu = new MenuElement[]{
                    MenuElement.exit("Остановить программу"),
                    MenuElement.menu("Посты", Menu.POSTS),
                    MenuElement.menu("Хабы", Menu.HUBS),
                    MenuElement.menu("Профиль", Menu.PROFILE),
                    MenuElement.menu("Выйти", Menu.LOGOUT),
                    MenuElement.menu("Админ-панель", Menu.ADMIN_PANEL)
            };
        } else {
            menu = new MenuElement[]{
                    MenuElement.exit("Остановить программу"), //
                    MenuElement.menu("Посты", Menu.POSTS),
                    MenuElement.menu("Хабы", Menu.HUBS),
                    MenuElement.menu("Войти", Menu.LOGIN),
                    MenuElement.menu("Зарегистрироваться", Menu.SIGNUP),
                    MenuElement.menu("Админ-панель", Menu.ADMIN_PANEL)
            };
        }
        ConsoleApp.setMenu(renderSelectMenu(menu));
    }

    public static void renderPosts() {
        MenuElement[] menu;
        if (AuthStorage.isAuthenticated()) {
            menu = new MenuElement[]{
                    MenuElement.home("Назад"), //
                    MenuElement.menu("Свежее", Menu.LAST_POSTS), //
                    MenuElement.menu("Лучшее", Menu.TOP_POSTS), //
                    MenuElement.menu("Найти", Menu.FIND_POSTS), //
                    MenuElement.menu("Создать", Menu.CREATE_POSTS),
                    MenuElement.menu("Выбрать по id", Menu.CHOICE_POST) //
            };
        } else {
            menu = new MenuElement[]{
                    MenuElement.home("Назад"), //
                    MenuElement.menu("Свежее", Menu.LAST_POSTS),
                    MenuElement.menu("Лучшее", Menu.TOP_POSTS),
                    MenuElement.menu("Найти", Menu.FIND_POSTS),
                    MenuElement.menu("Выбрать по id", Menu.CHOICE_POST)
            };
        }
        ConsoleApp.setMenu(renderSelectMenu(menu));
    }

    public static void renderChoicePost() {
        renderPostChoice();
        ConsoleApp.setMenu(Menu.POSTS);
    }

    public static void renderChoiceHub() {
        renderHubChoice();
        ConsoleApp.setMenu(Menu.HUBS);
    }

    public static void renderProfile() {
        TransactionResult<User> userTransactionResult = AuthentificationService.authUser(AuthStorage.getAuthToken());
        if (userTransactionResult.isSuccess()) {
            User user = userTransactionResult.getData();

            System.out.printf("ПОЛЬЗОВАТЕЛЬ: %s (%d)\n", user.getUsername(), user.getId());
            System.out.printf("ОБ АВТОРЕ: %s\n", user.getStatus());

            System.out.println("1) Изменить статус\n2) Мои посты\n3) Мои хабы\n0) Назад");
            switch(renderSelect(3)) {
                case 1 -> {
                    System.out.print("Новый статус: ");
                    TransactionResult<User> newUserTransactionResult = ContentService.updateUser(
                            AuthStorage.getAuthToken(), scanner.nextLine()
                    );
                    if (!newUserTransactionResult.isSuccess()) {
                        renderFailMessage(newUserTransactionResult.getMessage());
                    }
                }
                case 2 -> {
                    renderList(
                            (Integer currentPage, Integer maxPage) -> renderPostList(
                                    ContentService.getUserPosts(user.getId(), currentPage), currentPage, maxPage
                            ),
                            ContentService.userPostPages(user.getId()),
                            MenuLogic::renderPostChoice

                    );
                }
                case 3 -> {
                    renderList(
                            (Integer currentPage, Integer maxPage) -> renderHubList(
                                    ContentService.getUserHubs(user.getId(), currentPage), currentPage, maxPage
                            ),
                            ContentService.userHubPages(user.getId()),
                            MenuLogic::renderHubChoice

                    );
                }

                case 0 -> {
                    ConsoleApp.setMenu(Menu.HUBS);
                }
            }

        } else {
            renderFailMessage(userTransactionResult.getMessage());
        }
    }

    public static boolean renderPost(int id) {
        TransactionResult<MetaPost> postTransactionResult = ContentService.getPost(id);
        if (!postTransactionResult.isSuccess()) {
            renderFailMessage(postTransactionResult.getMessage());
            return true;
        }
        MetaPost metaPost = postTransactionResult.getData();

        Post post = metaPost.getPost();
        User author = metaPost.getAuthor();
        Hub hub = metaPost.getHub();
        int likes = metaPost.getLikes();
        int dislikes = metaPost.getDislikes();

        renderSeparator("%s (%d)".formatted(post.getLabel(), post.getId()));
        System.out.printf("АВТОР: %s (%d)\n",
                author.getUsername(), author.getId()
        );
        if (hub != null) {
            System.out.printf("ХАБ: %s (%d)\n", hub.getHubname(), hub.getId());
        }
        System.out.printf("%s\n\n%s\n[+%d -%d]\n",
                post.getCreationTime(),
                post.getContent(),
                likes, dislikes
        );

        if (AuthStorage.isAuthenticated()) {
            TransactionResult<User> currentUserTransactionResult = AuthentificationService.authUser(AuthStorage.getAuthToken());

            TransactionResult<Vote> voteTransactionResult = ContentService.getVote(AuthStorage.getAuthToken(), post.getId());
            String vote = "/";
            if (voteTransactionResult.isSuccess()) {
                int delta = voteTransactionResult.getData().getVoteDelta();
                if (delta > 0) {
                    vote = "+";
                } else if (delta < 0) {
                    vote = "-";
                }
            }
            int selectMax = 3;

            System.out.printf("Ваша оценка: %s\n\n", vote);
            System.out.println("\n1) Нравится");
            System.out.println("2) Не нравится");
            System.out.println("3) Об авторе");
            if (currentUserTransactionResult.isSuccess() && (currentUserTransactionResult.getData().getId().equals(author.getId()))) {
                System.out.println("4) Редактировать");
                selectMax++;
            }
            System.out.println("0) Назад");
            switch (renderSelect(selectMax)) {
                case 1 -> {
                    TransactionResult<Vote> newVoteTransactionResult = ContentService.likePost(AuthStorage.getAuthToken(), post.getId());
                    if (!newVoteTransactionResult.isSuccess()){
                        System.out.println(newVoteTransactionResult.getMessage());
                    }
                    return false;
                }
                case 2 -> {
                    TransactionResult<Vote> newVoteTransactionResult = ContentService.dislikePost(AuthStorage.getAuthToken(), post.getId());
                    if (!newVoteTransactionResult.isSuccess()){
                        System.out.println(newVoteTransactionResult.getMessage());
                    }
                    return false;
                }
                case 3 -> {
                    renderUser(author);
                    return false;
                }
                case 4 -> {
                    System.out.println("Введите обновленную версию текста:");
                    String content = scanner.nextLine();
                    if (!(content ==null) && !(content.isBlank()) && !(content.equals("0"))) {
                        TransactionResult<Post> updatedPostTransactionResult = ContentService.updatePost(AuthStorage.getAuthToken(), post.getId(), content);
                        if (!updatedPostTransactionResult.isSuccess()){
                            System.out.println(updatedPostTransactionResult.getMessage());
                        }
                    }
                    return false;
                }
                default -> {
                    return true;
                }
            }
        } else {
            System.out.println("\n0) Назад");
            System.out.println("1) Об авторе");
            if (renderSelect(1) == 1) {
                renderUser(author);
                return false;
            } else {
                return true;
            }
        }
    }

    public static void renderUser(User author) {
        renderSeparator("ПОЛЬЗОВАТЕЛЬ");
        System.out.printf("ИМЯ ПОЛЬЗОВАТЕЛЯ: %s\n", author.getUsername());
        System.out.printf("ОБ АВТОРЕ: %s\n", author.getStatus());
        System.out.println("1) Посты автора\n2) Хабы автора\n0) Назад");
        switch (renderSelect(2)){
            case 1 -> {
                renderList(
                        (Integer currentPage, Integer maxPage) -> renderPostList(
                                ContentService.getUserPosts(author.getId(), currentPage), currentPage, maxPage
                        ),
                        ContentService.userPostPages(author.getId()),
                        MenuLogic::renderPostChoice

                );
            }
            case 2 -> {
                renderList(
                        (Integer currentPage, Integer maxPage) -> renderHubList(
                                ContentService.getUserHubs(author.getId(), currentPage), currentPage, maxPage
                        ),
                        ContentService.userHubPages(author.getId()),
                        MenuLogic::renderHubChoice

                );
            }
        }
    }

    public static void renderPostList(
            TransactionResult<ArrayList<MetaPost>> postsTransactionResult,
            int currentPage,
            int maxPage

            ) {
        if (!postsTransactionResult.isSuccess()) {
            renderFailMessage(postsTransactionResult.getMessage());
            ConsoleApp.setMenu(Menu.HOME);
        }

        ArrayList<MetaPost> posts = postsTransactionResult.getData();
        if (posts.isEmpty()) {
            renderFailMessage("Посты не найдены...");
        } else {
            System.out.printf("Формат:\n%s\nСтраница: %d/%d\n\n", MetaPost.getFieldsDescription(), currentPage+1, maxPage+1);
            posts.forEach(System.out::println);
        }
    }


    public static void renderList(BiConsumer<Integer, Integer> listRenderer,
                                  TransactionResult<Integer> maxPageTransactionResult,
                                  Runnable elementRenderer) {
        int currentPage = 0;
        boolean repeat = true;

        while (repeat) {
            if (!maxPageTransactionResult.isSuccess()) {
                renderFailMessage(maxPageTransactionResult.getMessage());
                ConsoleApp.setMenu(Menu.HOME);
                return;
            }
            int maxPage = maxPageTransactionResult.getData();
            currentPage = Math.max(Math.min(currentPage, maxPage), 0);
            listRenderer.accept(currentPage, maxPage);

            System.out.println("1) Просмотреть");
            System.out.println("2) Предыдущая страница");
            System.out.println("3) Следующая страница");
            System.out.println("0) Назад");
            switch (renderSelect(3)) {
                case 1 -> {
                    elementRenderer.run();
                }
                case 2 -> {
                    currentPage--;
                }
                case 3 -> {
                    currentPage++;
                }
                case 0 -> {
                    repeat = false;
                }
            }
        }
        ConsoleApp.setMenu(Menu.HOME);
    }

    public static void renderPostChoice(){
        System.out.println("Выберите пост, введя его id");
        boolean postViewed = false;
        int postId = renderSelect();
        while (postId!=0 && !postViewed) {
            postViewed = renderPost(postId);
        }
    }

    public static void renderLastPosts() {
        renderList(
                (Integer currentPage, Integer maxPage) -> renderPostList(
                        ContentService.getLastPosts(currentPage), currentPage, maxPage
                ),
                ContentService.getPostPages(),
                MenuLogic::renderPostChoice
        );
    }

    public static void renderTopPosts() {
        renderList(
                (Integer currentPage, Integer maxPage) -> renderPostList(
                        ContentService.getTopPosts(currentPage), currentPage, maxPage
                ),
                ContentService.getPostPages(),
                MenuLogic::renderPostChoice
        );
    }

    public static boolean renderHub(int hubId) {
        TransactionResult<MetaHub> hubTransactionResult = ContentService.getHub(hubId);
        if (!hubTransactionResult.isSuccess()) {
            renderFailMessage(hubTransactionResult.getMessage());
            return true;
        }
        MetaHub metaHub = hubTransactionResult.getData();

        Hub hub = metaHub.getHub();
        User creator = metaHub.getCreator();

        renderSeparator("%s (%d)".formatted(hub.getHubname(), hub.getId()));
        System.out.printf("СОЗДАТЕЛЬ: %s (%d)\n", creator.getUsername(), creator.getId());
        System.out.printf("ОПИСАНИЕ: %s\n", hub.getDescription());
        if (AuthStorage.isAuthenticated()) {
            System.out.println("1) Посты\n2) Создать пост в хабе\n0) Назад");
            switch (renderSelect(2)){
                case 1 -> {
                    renderList(
                            (Integer currentPage, Integer maxPage) -> renderPostList(
                                    ContentService.getHubPosts(hub.getId(), currentPage), currentPage, maxPage
                            ),
                            ContentService.hubPostPages(hub.getId()),
                            MenuLogic::renderPostChoice
                    );
                    return false;
                }
                case 2 -> {
                    renderCreatePost(hub);
                    return false;
                }
                default -> {
                    return true;
                }
            }
        } else {
            System.out.println("1) Посты\n0) Назад");
            if (renderSelect(1) == 1) {
                renderList(
                        (Integer currentPage, Integer maxPage) -> renderPostList(
                                ContentService.getHubPosts(hub.getId(), currentPage), currentPage, maxPage
                        ),
                        ContentService.hubPostPages(hub.getId()),
                        MenuLogic::renderPostChoice
                );
                return false;
            } else {
                return true;
            }
        }
    }

    public static void renderHubChoice() {
        System.out.println("Выберите хаб, введя его id:");
        boolean postViewed = false;
        int hubId = renderSelect();
        while (hubId!=0 && !postViewed) {
            postViewed = renderHub(hubId);
        }
    }

    public static void renderFindPosts() {
        System.out.print("Введите поисковый запрос: ");
        String query = scanner.nextLine();
        renderList(
                (Integer currentPage, Integer maxPage) -> renderPostList(
                        ContentService.findPosts(query, currentPage), currentPage, maxPage
                ),
                ContentService.findPostPages(query),
                MenuLogic::renderPostChoice
        );
    }

    public static void renderCreatePost() {
        renderCreatePost(null);
    }

    public static void renderCreatePost(Hub hub) {
        if (AuthStorage.isAuthenticated()) {
            System.out.print("ЗАГОЛОВОК: ");
            String label = scanner.nextLine();
            System.out.println("ТЕКСТ:");
            String content = scanner.nextLine();
            System.out.println("1) Опубликовать\n0) Отменить");
            int in = renderSelect(1);
            if (in == 1) {
                TransactionResult<Post> postTransactionResult;
                if (hub != null) {
                    postTransactionResult = ContentService.createPost(AuthStorage.getAuthToken(), hub.getId(), label, content);
                } else {
                    postTransactionResult = ContentService.createPost(AuthStorage.getAuthToken(), null, label, content);
                }
                if (postTransactionResult.isSuccess()) {
                    System.out.println("Пост опубликован");
                } else {
                    System.out.println("Пост не опубликован:");
                    renderFailMessage(postTransactionResult.getMessage());
                }
            } else {
                System.out.println("Публикация поста отменена");
            }
        } else {
            renderFailMessage("Вы не авторизированы");
        }
        ConsoleApp.setMenu(Menu.HOME);
    }

    public static void renderCreateHub() {
        if (AuthStorage.isAuthenticated()) {
            System.out.print("НАЗВАНИЕ: ");
            String hubname = scanner.nextLine();
            System.out.print("ОПИСАНИЕ: ");
            String description = scanner.nextLine();
            System.out.println("1) Создать\n0) Отменить");
            int in = renderSelect(1);
            if (in == 1) {
                TransactionResult<Hub> hubTransactionResult = ContentService.createHub(AuthStorage.getAuthToken(), hubname, description);
                if (hubTransactionResult.isSuccess()) {
                    System.out.println("Хаб создан");
                } else {
                    System.out.println("Хаб не создан:");
                    renderFailMessage(hubTransactionResult.getMessage());
                }
            } else {
                System.out.println("Создание хаба отменено");
            }
        } else {
            renderFailMessage("Вы не авторизированы");
        }
        ConsoleApp.setMenu(Menu.HOME);
    }

    public static void renderHubList(
            TransactionResult<ArrayList<MetaHub>> hubsTransactionResult,
            int currentPage,
            int maxPage

    ) {
        if (!hubsTransactionResult.isSuccess()) {
            renderFailMessage(hubsTransactionResult.getMessage());
            ConsoleApp.setMenu(Menu.HOME);
        }

        ArrayList<MetaHub> hubs = hubsTransactionResult.getData();
        if (hubs.isEmpty()) {
            renderFailMessage("Хабы не найдены...");
        } else {
            System.out.printf("Формат:\n%s\nСтраница: %d/%d\n\n", MetaHub.getFieldsDescription(), currentPage+1, maxPage+1);
            hubs.forEach(System.out::println);
        }
    }

    public static void renderHubs(){
        MenuElement[] menu;
        if (AuthStorage.isAuthenticated()) {
            menu = new MenuElement[]{
                    MenuElement.home("Назад"), //
                    MenuElement.menu("Хабы списком", Menu.HUBS_AS_LIST),
                    MenuElement.menu("Создать хаб", Menu.CREATE_HUB),
                    MenuElement.menu("Выбрать хаб по id", Menu.CHOICE_HUB)//
            };
        } else {
            menu = new MenuElement[]{
                    MenuElement.home("Назад"), //
                    MenuElement.menu("Хабы списком", Menu.HUBS_AS_LIST),
                    MenuElement.menu("Выбрать хаб по id", Menu.CHOICE_HUB)
            };
        }
        ConsoleApp.setMenu(renderSelectMenu(menu));
    }

    public static void renderHubsAsList(){
        renderList(
                (Integer currentPage, Integer maxPage) -> renderHubList(
                        ContentService.getHubs(currentPage), currentPage, maxPage
                ),
                ContentService.hubPages(),
                MenuLogic::renderHubChoice

        );
    }

    public static void renderLogin() {
        System.out.print("Адрес электронной почты: ");
        String email = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();
        TransactionResult<Session> sessionTransactionResult = AuthentificationService.loginUser(
                email,
                password
        );
        if (sessionTransactionResult.isSuccess()) {
            AuthStorage.setAuthToken(sessionTransactionResult.getData().getAuthToken());
        } else {
            renderFailMessage(sessionTransactionResult.getMessage());
        }
        ConsoleApp.setMenu(Menu.HOME);
    }

    public static void renderSignup() {
        System.out.print("Имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Адрес электронной почты: ");
        String email = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();
        TransactionResult<Session> sessionTransactionResult = AuthentificationService.signupUser(
                username,
                email,
                password
        );

        if (sessionTransactionResult.isSuccess()) {
            AuthStorage.setAuthToken(sessionTransactionResult.getData().getAuthToken());
        } else {
            renderFailMessage(sessionTransactionResult.getMessage());
        }
        ConsoleApp.setMenu(Menu.HOME);
    }

    public static void renderLogout() {
        System.out.println("Выход...");
        AuthentificationService.logoutUser(AuthStorage.getAuthToken());
        AuthStorage.resetAuthToken();
        ConsoleApp.setMenu(Menu.HOME);
    }



    public static Menu renderSelectMenu(MenuElement[] menuElements){
        if (menuElements.length > 1) {
            for (int i = 1; i < menuElements.length; i++) {
                System.out.printf("%d) %s\n", i, menuElements[i].getName());
            }
        }
        System.out.printf("%d) %s\n", 0, menuElements[0].getName());
        return menuElements[renderSelect(menuElements.length-1)].getTarget();
    }

    public static int renderSelect(Integer maxValue) {
        while (true) {
            try {
                System.out.print("> ");
                int in = Integer.parseInt(scanner.nextLine());
                if (maxValue == null || (in >= 0 && in <= maxValue)) {
                    return in;
                } else {
                    System.out.println("Неизвестное действие, повторите попытку");
                }
            } catch (NumberFormatException e){
                System.out.println("Некорректный ввод, повторите попытку");
            }
        }
    }

    public static int renderSelect() {
        return renderSelect(null);
    }

    public static void renderSeparator(String name) {
        int menuSize = 65;
        int textSize = name.length() + 2;
        int sepSize = menuSize-textSize;
        int offset = sepSize % 2 == 0 ? 0 : 1;
        System.out.println(
                "=".repeat(sepSize/2 + offset) +
                        " " +
                        name +
                        " " +
                        "=".repeat(sepSize/2)
        );
    }

    public static void renderFailMessage(String message, Menu target) {
        System.out.println("Запрос отклонён:");
        System.out.println(message);
        ConsoleApp.setMenu(target);
    }

    public static void renderFailMessage(String message) {
        renderFailMessage(message, Menu.HOME);
    }

    public static void renderAdminPanel(){
        System.out.println("Введите админ-ключ для продолжения:");
        String adminKey = scanner.nextLine();
        if (AdminService.checkAdminKey(adminKey)) {
            boolean repeat = true;
            while (repeat) {
                System.out.println("\n1) Продемонстрировать данные из таблицы users");
                System.out.println("2) Продемонстрировать данные из таблицы sessions");
                System.out.println("3) Продемонстрировать данные из таблицы hubs");
                System.out.println("4) Продемонстрировать данные из таблицы posts");
                System.out.println("5) Продемонстрировать данные из таблицы votes");
                System.out.println("6) Удалить пост");
                System.out.println("7) Удалить пользователя");
                System.out.println("8) Удалить хаб");
                System.out.println("9) Продемонстрировать статистику всех пользователей");
                System.out.println("0) Назад");
                switch (renderSelect(9)) {
                    case 0 -> {
                        repeat = false;
                        ConsoleApp.setMenu(Menu.HOME);
                    }
                    case 1 -> {
                        System.out.println("Данные из таблицы users\nФормат:");
                        System.out.println(User.getFieldsDescription());
                        System.out.println();
                        TransactionResult<ArrayList<User>> transactionResult = AdminService.getAllUsers(adminKey);
                        if (transactionResult.isSuccess()){
                            transactionResult.getData().forEach(System.out::println);
                        } else {
                            renderFailMessage(transactionResult.getMessage());
                        }
                    }
                    case 2 -> {
                        System.out.println("Данные из таблицы sessions\nФормат:");
                        System.out.println(Session.getFieldsDescription());
                        System.out.println();
                        TransactionResult<ArrayList<Session>> transactionResult = AdminService.getAllSession(adminKey);
                        if (transactionResult.isSuccess()){
                            transactionResult.getData().forEach(System.out::println);
                        } else {
                            renderFailMessage(transactionResult.getMessage());
                        }
                    }
                    case 3 -> {
                        System.out.println("Данные из таблицы hubs\nФормат:");
                        System.out.println(Hub.getFieldsDescription());
                        System.out.println();
                        TransactionResult<ArrayList<Hub>> transactionResult = AdminService.getAllHubs(adminKey);
                        if (transactionResult.isSuccess()){
                            transactionResult.getData().forEach(System.out::println);
                        } else {
                            renderFailMessage(transactionResult.getMessage());
                        }
                    }
                    case 4 -> {
                        System.out.println("Данные из таблицы posts\nФормат:");
                        System.out.println(Hub.getFieldsDescription());
                        System.out.println();
                        TransactionResult<ArrayList<Post>> transactionResult = AdminService.getAllPosts(adminKey);
                        if (transactionResult.isSuccess()){
                            transactionResult.getData().forEach(System.out::println);
                        } else {
                            renderFailMessage(transactionResult.getMessage());
                        }
                    }
                    case 5 -> {
                        System.out.println("Данные из таблицы votes\nФормат:");
                        System.out.println(Hub.getFieldsDescription());
                        System.out.println();
                        TransactionResult<ArrayList<Vote>> transactionResult = AdminService.getAllVotes(adminKey);
                        if (transactionResult.isSuccess()){
                            transactionResult.getData().forEach(System.out::println);
                        } else {
                            renderFailMessage(transactionResult.getMessage());
                        }
                    }
                    case 6 -> {
                        System.out.println("Введите id поста для удаления:");
                        AdminService.deletePost(adminKey, renderSelect());
                    }
                    case 7 -> {
                        System.out.println("Введите id пользователя для удаления:");
                        AdminService.deleteUser(adminKey, renderSelect());
                    }
                    case 8 -> {
                        System.out.println("Введите id хаба для удаления:");
                        AdminService.deleteHub(adminKey, renderSelect());
                    }
                    case 9 -> {
                        System.out.println("Статистика пользователей (данные из всех таблиц)\nФормат:");
                        System.out.println(UserStatistic.getFieldsDescription());
                        System.out.println();
                        TransactionResult<ArrayList<UserStatistic>> transactionResult = AdminService.getAllUsersStatistic(adminKey);
                        if (transactionResult.isSuccess()){
                            transactionResult.getData().forEach(System.out::println);
                        } else {
                            renderFailMessage(transactionResult.getMessage());
                        }
                    }
                }
            }
        } else {
            renderFailMessage("Неверный админ-ключ");
        }
    }
}
