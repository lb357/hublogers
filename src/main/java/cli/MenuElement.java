package cli;

import cli.page.HomePage;
import cli.page.Page;


public class MenuElement {
    private final String name;
    private final Page target;

    private MenuElement(String name, Page target) {
        this.name = name;
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public Page getTarget() {
        return target;
    }

    public static MenuElement menu(String name, Page target) {
        return new MenuElement(name, target);
    }


    public static MenuElement home(String name) {
        return new MenuElement(name, new HomePage());
    }

    public static MenuElement exit(String name) {
        return new MenuElement(name, null);
    }
}