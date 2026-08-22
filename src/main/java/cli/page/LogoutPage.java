package cli.page;

import cli.AuthStorage;
import cli.ConsoleApp;
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
