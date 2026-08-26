package cli.page.post;

import cli.AuthStorage;
import cli.ConsoleApp;
import cli.MenuElement;
import cli.page.Page;

public class PostsPage extends Page {
    @Override
    public void render() {
        MenuElement[] selectPageMenu;
        if (AuthStorage.isAuthenticated()) {
            selectPageMenu = new MenuElement[]{
                    MenuElement.home("Назад"),
                    MenuElement.menu("Свежее", new LastPostsPage()),
                    MenuElement.menu("Лучшее", new TopPostsPage()),
                    MenuElement.menu("Найти", new FindPostsPage()),
                    MenuElement.menu("Создать", new CreatePostPage()),
                    MenuElement.menu("Выбрать по id", new ChoicePostPage())
            };
        } else {
            selectPageMenu = new MenuElement[]{
                    MenuElement.home("Назад"),
                    MenuElement.menu("Свежее", new LastPostsPage()),
                    MenuElement.menu("Лучшее", new TopPostsPage()),
                    MenuElement.menu("Найти", new FindPostsPage()),
                    MenuElement.menu("Выбрать по id", new ChoicePostPage())
            };
        }
        ConsoleApp.setPage(renderSelectPageMenu(selectPageMenu));
    }

    @Override
    public String getPageName() {
        return "ПОСТЫ";
    }
}
