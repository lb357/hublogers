package cli;

import cli.menu.Menu;
import cli.menu.MenuLogic;

public class ConsoleApp {
    private static boolean working = true;
    private static Menu currentMenu = Menu.HOME;

    public static void menu() {
        while (working) {
            MenuLogic.renderSeparator(currentMenu.getMenuName());
            Runnable renderer = currentMenu.getRenderer();
            renderer.run();
            if (currentMenu == null) {
                working = false;
            }
        }
    }

    public static void setMenu(Menu currentMenu) {
        ConsoleApp.currentMenu = currentMenu;
    }
}
