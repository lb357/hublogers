package cli.page;

import cli.AuthStorage;
import cli.ConsoleApp;
import model.domain.Session;
import service.AuthentificationService;
import service.TransactionResult;

public class LoginPage extends Page {
    @Override
    public void render() {
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
        ConsoleApp.setPage(new HomePage());
    }

    @Override
    public String getPageName() {
        return "ВХОД";
    }
}
