package cli.page.auth;

import cli.AuthStorage;
import cli.ConsoleApp;
import cli.page.HomePage;
import cli.page.Page;
import service.AuthentificationService;

public class LogoutPage extends Page {
    @Override
    public void render() {
        System.out.println("Выход...");
        AuthentificationService.logoutUser(AuthStorage.getAuthToken());
        AuthStorage.deleteAuthToken();
        ConsoleApp.setPage(new HomePage());
    }

    @Override
    public String getPageName() {
        return "ВЫХОД";
    }
}
