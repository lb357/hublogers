package cli.page;

import cli.ConsoleApp;
import service.ContentService;

public class TopPostsPage extends Page {
    @Override
    public void render() {
        ConsoleApp.setPage(new ListPage<>(
                ContentService::getTopPosts,
                ContentService::getPostPages,
                new ChoicePostPage()
        ));
    }

    @Override
    public String getPageName() {
        return "ЛУЧШЕЕ";
    }
}
