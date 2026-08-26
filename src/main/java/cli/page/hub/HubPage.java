package cli.page.hub;

import cli.AuthStorage;
import cli.ConsoleApp;
import cli.page.*;
import cli.page.post.ChoicePostPage;
import cli.page.post.CreatePostPage;
import model.composite.MetaHub;
import model.domain.Hub;
import model.domain.User;
import service.ContentService;

public class HubPage extends Page {
    private final MetaHub metaHub;

    public HubPage(MetaHub metaHub) {
        this.metaHub = metaHub;
    }

    @Override
    public void render() {
        Hub hub = metaHub.getHub();
        User creator = metaHub.getCreator();

        System.out.printf("СОЗДАТЕЛЬ: %s (%d)\n", creator.getUsername(), creator.getId());
        System.out.printf("ОПИСАНИЕ: %s\n", hub.getDescription());
        if (AuthStorage.isAuthenticated()) {
            System.out.println("1) Посты\n2) Создать пост в хабе\n0) Назад");
            switch (renderSelect(2)){
                case 1 -> {
                    ConsoleApp.setPage(new ListPage<>(
                            (Integer currentPage) -> ContentService.getHubPosts(hub.getId(), currentPage),
                            () -> ContentService.hubPostPages(hub.getId()),
                            new ChoicePostPage()
                    ));
                }
                case 2 -> {
                    ConsoleApp.setPage(new CreatePostPage(hub));
                }
                default -> {
                    ConsoleApp.setPage(new HomePage());
                }
            }
        } else {
            System.out.println("1) Посты\n0) Назад");
            if (renderSelect(1) == 1) {
                ConsoleApp.setPage(new ListPage<>(
                        (Integer currentPage) -> ContentService.getHubPosts(hub.getId(), currentPage),
                        () -> ContentService.hubPostPages(hub.getId()),
                        new ChoicePostPage()
                ));
            } else {
                ConsoleApp.setPage(new HomePage());
            }
        }
    }

    @Override
    public String getPageName() {
        return "%s (%d)".formatted(metaHub.getHub().getHubname(), metaHub.getHub().getId());
    }
}
