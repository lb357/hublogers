package cli.page;

import cli.ConsoleApp;
import service.ContentService;

public class FindPostsPage extends Page {
    @Override
    public void render() {
        System.out.print("Введите поисковый запрос: ");
        String query = scanner.nextLine();
        ConsoleApp.setPage(new ListPage<>(
                (Integer currentPage) -> ContentService.findPosts(query, currentPage),
                () -> ContentService.findPostPages(query),
                new ChoicePostPage()
        ));
    }

    @Override
    public String getPageName() {
        return "СВЕЖЕЕ";
    }
}
