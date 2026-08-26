package cli.page.hub;

import cli.ConsoleApp;
import cli.page.post.ChoicePostPage;
import cli.page.ListPage;
import cli.page.Page;
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
