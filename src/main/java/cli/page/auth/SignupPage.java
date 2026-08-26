package cli.page.auth;

import cli.AuthStorage;
import cli.ConsoleApp;
import cli.page.HomePage;
import cli.page.Page;
import model.domain.Session;
import service.AuthentificationService;
import model.TransactionResult;

public class SignupPage extends Page {
    @Override
    public void render() {
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
        ConsoleApp.setPage(new HomePage());
    }

    @Override
    public String getPageName() {
        return "РЕГИСТРАЦИЯ";
    }
}
