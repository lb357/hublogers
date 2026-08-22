package cli.page;

import cli.AuthStorage;
import cli.ConsoleApp;
import cli.MenuElement;

public class HomePage extends Page {
    @Override
    public void render() {
        MenuElement[] selectPageMenu;
        if (AuthStorage.isAuthenticated()) {
            selectPageMenu = new MenuElement[]{
                    MenuElement.exit("Остановить программу"),
                    MenuElement.menu("Посты", new PostsPage()),
                    MenuElement.menu("Хабы", new HubsPage()),
                    MenuElement.menu("Профиль", new ProfilePage()),
                    MenuElement.menu("Выйти", new LogoutPage()),
                    MenuElement.menu("Админ-панель", new AdminPage())
            };
        } else {
            selectPageMenu = new MenuElement[]{
                    MenuElement.exit("Остановить программу"),
                    MenuElement.menu("Посты", new PostsPage()),
                    MenuElement.menu("Хабы", new HubsPage()),
                    MenuElement.menu("Войти", new LoginPage()),
                    MenuElement.menu("Зарегистрироваться", new SignupPage()),
                    MenuElement.menu("Админ-панель", new AdminPage())
            };
        }
        ConsoleApp.setPage(renderSelectPageMenu(selectPageMenu));
    }

    @Override
    public String getPageName() {
        return "ХАБЛОГЕРС";
    }
}
