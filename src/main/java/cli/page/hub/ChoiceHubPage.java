package cli.page.hub;

import cli.ConsoleApp;
import cli.page.Page;
import model.composite.MetaHub;
import service.ContentService;
import model.TransactionResult;

public class ChoiceHubPage extends Page {
    @Override
    public void render() {
        System.out.println("Выберите хаб, введя его id");
        int hubId = renderSelect();
        while (hubId!=0) {
            TransactionResult<MetaHub> hubTransactionResult = ContentService.getHub(hubId);
            if (hubTransactionResult.isSuccess()) {
                ConsoleApp.setPage(new HubPage(hubTransactionResult.getData()));
                hubId = 0;
            } else {
                renderFailMessage(hubTransactionResult.getMessage());
            }
        }
    }

    @Override
    public String getPageName() {
        return "ВЫБОР ХАБА";
    }
}
