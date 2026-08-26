package cli.page.user;

import cli.AuthStorage;
import cli.ConsoleApp;
import cli.page.HomePage;
import cli.page.ListPage;
import cli.page.Page;
import cli.page.hub.ChoiceHubPage;
import cli.page.post.ChoicePostPage;
import model.domain.User;
import service.AuthentificationService;
import service.ContentService;
import model.TransactionResult;

public class ProfilePage extends Page {
    @Override
    public void render() {
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
                    ConsoleApp.setPage(new ListPage<>(
                            (Integer currentPage) -> ContentService.getUserPosts(user.getId(), currentPage),
                            () -> ContentService.userPostPages(user.getId()),
                            new ChoicePostPage()
                    ));
                }
                case 3 -> {
                    ConsoleApp.setPage(new ListPage<>(
                            (Integer currentPage) -> ContentService.getUserHubs(user.getId(), currentPage),
                            () -> ContentService.userHubPages(user.getId()),
                            new ChoiceHubPage()
                    ));
                }

                case 0 -> {
                    ConsoleApp.setPage(new HomePage());
                }
            }

        } else {
            renderFailMessage(userTransactionResult.getMessage());
        }
    }

    @Override
    public String getPageName() {
        return "ПРОФИЛЬ";
    }
}
