package cli;

import cli.page.HomePage;
import cli.page.Page;

public class ConsoleApp {
    private static boolean working = true;
    private static Page currentPage = new HomePage();

    public static void menu() {
        while (working) {
            currentPage.renderPage();
            if (currentPage == null) {
                working = false;
            }
        }
    }

    public static void setPage(Page currentPage) {
        ConsoleApp.currentPage = currentPage;
    }
}
