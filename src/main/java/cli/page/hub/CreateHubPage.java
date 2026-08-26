package cli.page.hub;

import cli.AuthStorage;
import cli.ConsoleApp;
import cli.page.HomePage;
import cli.page.Page;
import model.domain.Hub;
import service.ContentService;
import model.TransactionResult;

public class CreateHubPage extends Page {
    @Override
    public void render() {
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
        ConsoleApp.setPage(new HomePage());
    }

    @Override
    public String getPageName() {
        return "СОЗДАНИЕ ХАБА";
    }
}
