package cli.page;

import cli.ConsoleApp;
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
