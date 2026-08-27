package cli.page.post;

import cli.ConsoleApp;
import cli.page.ListPage;
import cli.page.Page;
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
        return "ПОИСК ПОСТОВ";
    }
}
