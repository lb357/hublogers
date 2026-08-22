package cli.page;

import cli.ConsoleApp;
import service.ContentService;

public class HubsListPage extends Page {
    @Override
    public void render() {
        ConsoleApp.setPage(new ListPage<>(
                ContentService::getHubs,
                ContentService::hubPages,
                new ChoicePostPage()
        ));
    }

    @Override
    public String getPageName() {
        return "ХАБЫ СПИСКОМ";
    }
}
