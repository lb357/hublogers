package cli.page;

import cli.ConsoleApp;
import model.composite.MetaPost;
import service.ContentService;
import service.TransactionResult;

public class ChoicePostPage extends Page {
    @Override
    public void render() {
        System.out.println("Выберите пост, введя его id");
        int postId = renderSelect();
        while (postId!=0) {
            TransactionResult<MetaPost> postTransactionResult = ContentService.getPost(postId);
            if (postTransactionResult.isSuccess()) {
                ConsoleApp.setPage(new PostPage(postTransactionResult.getData()));
                postId = 0;
            } else {
                renderFailMessage(postTransactionResult.getMessage());
            }
        }
    }

    @Override
    public String getPageName() {
        return "ВЫБОР ПОСТА";
    }
}
