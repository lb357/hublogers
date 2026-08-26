package cli.page.post;

import cli.ConsoleApp;
import cli.page.ListPage;
import cli.page.Page;
import service.ContentService;

public class LastPostsPage extends Page {
    @Override
    public void render() {
        ConsoleApp.setPage(new ListPage<>(
                ContentService::getLastPosts,
                ContentService::getPostPages,
                new ChoicePostPage()
        ));
    }

    @Override
    public String getPageName() {
        return "СВЕЖЕЕ";
    }
}
