package cli.page;

import cli.ConsoleApp;
import cli.MenuElement;

import java.util.Scanner;

public abstract class Page {
    protected static final Scanner scanner = new Scanner(System.in);

    public void renderPage() {
        renderSeparator(getPageName());
        render();
    }

    public abstract void render();
    public abstract String getPageName();

    public static Page renderSelectPageMenu(MenuElement[] menuElements){
        if (menuElements.length > 1) {
            for (int i = 1; i < menuElements.length; i++) {
                System.out.printf("%d) %s\n", i, menuElements[i].getName());
            }
        }
        System.out.printf("%d) %s\n", 0, menuElements[0].getName());
        return menuElements[renderSelect(menuElements.length-1)].getTarget();
    }

    public static int renderSelect(Integer maxValue) {
        while (true) {
            try {
                System.out.print("> ");
                int in = Integer.parseInt(scanner.nextLine());
                if (maxValue == null || (in >= 0 && in <= maxValue)) {
                    return in;
                } else {
                    System.out.println("Неизвестное действие, повторите попытку");
                }
            } catch (NumberFormatException e){
                System.out.println("Некорректный ввод, повторите попытку");
            }
        }
    }

    public static int renderSelect() {
        return renderSelect(null);
    }

    public static void renderSeparator(String name) {
        int menuSize = 65;
        int textSize = name.length() + 2;
        int sepSize = menuSize-textSize;
        int offset = sepSize % 2 == 0 ? 0 : 1;
        System.out.println(
                "=".repeat(sepSize/2 + offset) +
                        " " +
                        name +
                        " " +
                        "=".repeat(sepSize/2)
        );
    }

    public static void renderFailMessage(String message, Page target) {
        System.out.println("Запрос отклонён:");
        System.out.println(message);
        ConsoleApp.setPage(target);
    }

    public static void renderFailMessage(String message) {
        renderFailMessage(message, new HomePage());
    }
}
