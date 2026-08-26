package cli.page.hub;

import cli.AuthStorage;
import cli.ConsoleApp;
import cli.MenuElement;
import cli.page.Page;

public class HubsPage extends Page {
    @Override
    public void render() {
        MenuElement[] menu;
        if (AuthStorage.isAuthenticated()) {
            menu = new MenuElement[]{
                    MenuElement.home("Назад"),
                    MenuElement.menu("Хабы списком", new HubsListPage()),
                    MenuElement.menu("Создать хаб", new CreateHubPage()),
                    MenuElement.menu("Выбрать хаб по id", new ChoiceHubPage())
            };
        } else {
            menu = new MenuElement[]{
                    MenuElement.home("Назад"), //
                    MenuElement.menu("Хабы списком", new HubsListPage()),
                    MenuElement.menu("Выбрать хаб по id", new ChoiceHubPage())
            };
        }
        ConsoleApp.setPage(renderSelectPageMenu(menu));
    }

    @Override
    public String getPageName() {
        return "ХАБЫ";
    }
}
