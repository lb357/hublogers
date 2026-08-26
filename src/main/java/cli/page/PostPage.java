package cli.page;

import cli.AuthStorage;
import cli.ConsoleApp;
import model.composite.MetaPost;
import model.domain.Hub;
import model.domain.Post;
import model.domain.User;
import model.domain.Vote;
import service.AuthentificationService;
import service.ContentService;
import model.TransactionResult;

public class PostPage extends Page {
    private final MetaPost metaPost;

    public PostPage(MetaPost metaPost) {
        this.metaPost = metaPost;
    }

    @Override
    public void render() {
        Post post = metaPost.getPost();
        User author = metaPost.getAuthor();
        Hub hub = metaPost.getHub();
        int likes = metaPost.getLikes();
        int dislikes = metaPost.getDislikes();

        System.out.printf("АВТОР: %s (%d)\n",
                author.getUsername(), author.getId()
        );
        if (hub != null) {
            System.out.printf("ХАБ: %s (%d)\n", hub.getHubname(), hub.getId());
        }
        System.out.printf("%s\n\n%s\n[+%d -%d]\n",
                post.getCreationTime(),
                post.getContent(),
                likes, dislikes
        );

        if (AuthStorage.isAuthenticated()) {
            TransactionResult<User> currentUserTransactionResult = AuthentificationService.authUser(AuthStorage.getAuthToken());
            TransactionResult<Vote> voteTransactionResult = ContentService.getVote(AuthStorage.getAuthToken(), post.getId());
            String vote = "/";
            if (voteTransactionResult.isSuccess()) {
                int delta = voteTransactionResult.getData().getVoteDelta();
                if (delta > 0) {
                    vote = "+";
                } else if (delta < 0) {
                    vote = "-";
                }
            }
            int selectMax = 3;

            System.out.printf("Ваша оценка: %s\n\n", vote);
            System.out.println("\n1) Нравится");
            System.out.println("2) Не нравится");
            System.out.println("3) Об авторе");
            if (currentUserTransactionResult.isSuccess() && (currentUserTransactionResult.getData().getId().equals(author.getId()))) {
                System.out.println("4) Редактировать");
                selectMax++;
            }
            System.out.println("0) Назад");
            switch (renderSelect(selectMax)) {
                case 1 -> {
                    TransactionResult<Vote> newVoteTransactionResult = ContentService.likePost(AuthStorage.getAuthToken(), post.getId());
                    if (!newVoteTransactionResult.isSuccess()){
                        System.out.println(newVoteTransactionResult.getMessage());
                    }
                }
                case 2 -> {
                    TransactionResult<Vote> newVoteTransactionResult = ContentService.dislikePost(AuthStorage.getAuthToken(), post.getId());
                    if (!newVoteTransactionResult.isSuccess()){
                        System.out.println(newVoteTransactionResult.getMessage());
                    }
                }
                case 3 -> {
                    ConsoleApp.setPage(new UserPage(author));
                }
                case 4 -> {
                    System.out.println("Введите обновленную версию текста:");
                    String content = scanner.nextLine();
                    if (!(content ==null) && !(content.isBlank()) && !(content.equals("0"))) {
                        TransactionResult<Post> updatedPostTransactionResult = ContentService.updatePost(AuthStorage.getAuthToken(), post.getId(), content);
                        if (!updatedPostTransactionResult.isSuccess()){
                            System.out.println(updatedPostTransactionResult.getMessage());
                        }
                    }
                }
                default -> {
                    ConsoleApp.setPage(new HomePage());
                }
            }
        } else {
            System.out.println("\n0) Назад");
            System.out.println("1) Об авторе");
            if (renderSelect(1) == 1) {
                ConsoleApp.setPage(new UserPage(author));
            } else {
                ConsoleApp.setPage(new HomePage());
            }
        }
    }

    @Override
    public String getPageName() {
        return "%s (%d)".formatted(metaPost.getPost().getLabel(), metaPost.getPost().getId());
    }
}
