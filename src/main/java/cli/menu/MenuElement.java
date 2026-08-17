package cli.menu;

public class MenuElement {
    private final String name;
    private final Menu target;

    private MenuElement(String name, Menu target) {
        this.name = name;
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public Menu getTarget() {
        return target;
    }

    public static MenuElement menu(String name, Menu target) {
        return new MenuElement(name, target);
    }


    public static MenuElement home(String name) {
        return new MenuElement(name, Menu.HOME);
    }

    public static MenuElement exit(String name) {
        return new MenuElement(name, null);
    }
}