package cli.page;

import cli.ConsoleApp;
import model.domain.User;
import service.ContentService;

public class UserPage extends Page {
    private final User user;

    public UserPage(User user) {
        this.user = user;
    }

    @Override
    public void render() {
        System.out.printf("ИМЯ ПОЛЬЗОВАТЕЛЯ: %s\n", user.getUsername());
        System.out.printf("ОБ АВТОРЕ: %s\n", user.getStatus());
        System.out.println("1) Посты автора\n2) Хабы автора\n0) Назад");
        switch (renderSelect(2)){
            case 1 -> {
                ConsoleApp.setPage(new ListPage<>(
                        (Integer currentPage) -> ContentService.getUserPosts(user.getId(), currentPage),
                        () -> ContentService.userPostPages(user.getId()),
                        new ChoicePostPage()
                ));
            }
            case 2 -> {
                ConsoleApp.setPage(new ListPage<>(
                        (Integer currentPage) -> ContentService.getUserHubs(user.getId(), currentPage),
                        () -> ContentService.userHubPages(user.getId()),
                        new ChoiceHubPage()
                ));
            }
        }
    }

    @Override
    public String getPageName() {
        return "%s (%d)".formatted(user.getUsername(), user.getId());
    }
}
