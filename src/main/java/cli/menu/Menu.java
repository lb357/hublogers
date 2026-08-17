package cli.menu;

public enum Menu {
    HOME("ХАБЛОГЕРС", MenuLogic::renderHome),
    POSTS("ПОСТЫ", MenuLogic::renderPosts),
    HUBS("ХАБЫ", MenuLogic::renderHubs),
    LOGIN("ВХОД", MenuLogic::renderLogin),
    SIGNUP("РЕГИСТРАЦИЯ", MenuLogic::renderSignup),
    LOGOUT("ВЫХОД", MenuLogic::renderLogout),
    TOP_POSTS("ЛУЧШЕЕ", MenuLogic::renderTopPosts),
    LAST_POSTS("СВЕЖЕЕ", MenuLogic::renderLastPosts),
    FIND_POSTS("ПОИСК", MenuLogic::renderFindPosts),
    CREATE_POSTS("СОЗДАТЬ", MenuLogic::renderCreatePost),
    CHOICE_POST("ПОСТ ПО ID", MenuLogic::renderChoicePost),
    PROFILE("ПРОФИЛЬ", MenuLogic::renderProfile),
    HUBS_AS_LIST("ХАБЫ СПИСКОМ", MenuLogic::renderHubsAsList),
    CREATE_HUB("СОЗДАТЬ ХАБ", MenuLogic::renderCreateHub),
    CHOICE_HUB("ХАБ ПО ID", MenuLogic::renderChoiceHub),
    ADMIN_PANEL("АДМИН ПАНЕЛЬ", MenuLogic::renderAdminPanel);


    private final String menuName;
    private final Runnable renderer;

    Menu(String menuName, Runnable renderer) {
        this.menuName = menuName;
        this.renderer = renderer;
    }

    public Runnable getRenderer() {
        return renderer;
    }

    public String getMenuName() {
        return menuName;
    }
}
