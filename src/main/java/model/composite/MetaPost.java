package model.composite;

import model.common.DataModel;
import model.data.Hub;
import model.data.Post;
import model.data.User;

public class MetaPost extends DataModel {
    private final Post post;
    private final User author;
    private final Hub hub;

    private final int likes;
    private final int dislikes;


    public MetaPost(Post post, User author, Hub hub, int likes, int dislikes) {
        this.post = post;
        this.author = author;
        this.hub = hub;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public Post getPost() {
        return post;
    }

    public User getAuthor() {
        return author;
    }

    public Hub getHub() {
        return hub;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    @Override
    public String toString() {
        if (hub != null) {
            return "(%d / %s / %d / %s) %s\n%s [+%d -%d]\n".formatted(
                    post.getId(),
                    author.getUsername(),
                    hub.getId(),
                    hub.getHubname(),
                    post.getCreationTime(),
                    post.getLabel(),
                    likes,
                    dislikes
            );
        } else {
            return "(%d / %s) %s\n%s [+%d -%d]\n".formatted(
                    post.getId(),
                    author.getUsername(),
                    post.getCreationTime(),
                    post.getLabel(),
                    likes,
                    dislikes
            );
        }
    }

    public static String getFieldsDescription() {
        return "(id Поста / Имя Пользователя / id Хаба / Название Хаба) Время создания\nЗаголовок [Рейтинг]\n";
    }
}
