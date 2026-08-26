package cli.page;

import cli.AuthStorage;
import cli.ConsoleApp;
import model.domain.Hub;
import model.domain.Post;
import service.ContentService;
import model.TransactionResult;

public class CreatePostPage extends Page {
    Hub hub;

    public CreatePostPage(Hub hub) {
        this.hub = hub;
    }

    public CreatePostPage() {
        this.hub = null;
    }

    @Override
    public void render() {
        if (AuthStorage.isAuthenticated()) {
            System.out.print("ЗАГОЛОВОК: ");
            String label = scanner.nextLine();
            System.out.println("ТЕКСТ:");
            String content = scanner.nextLine();
            System.out.println("1) Опубликовать\n0) Отменить");
            int in = renderSelect(1);
            if (in == 1) {
                TransactionResult<Post> postTransactionResult;
                if (hub != null) {
                    postTransactionResult = ContentService.createPost(AuthStorage.getAuthToken(), hub.getId(), label, content);
                } else {
                    postTransactionResult = ContentService.createPost(AuthStorage.getAuthToken(), null, label, content);
                }
                if (postTransactionResult.isSuccess()) {
                    System.out.println("Пост опубликован");
                } else {
                    System.out.println("Пост не опубликован:");
                    renderFailMessage(postTransactionResult.getMessage());
                }
            } else {
                System.out.println("Публикация поста отменена");
            }
        } else {
            renderFailMessage("Вы не авторизированы");
        }
        ConsoleApp.setPage(new HomePage());
    }

    @Override
    public String getPageName() {
        return "СОЗДАНИЕ ПОСТА";
    }
}
