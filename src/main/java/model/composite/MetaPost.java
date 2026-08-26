package model.composite;

import model.DataModel;
import model.domain.Hub;
import model.domain.Post;
import model.domain.User;

import java.util.HashMap;
import java.util.Map;

public class MetaPost implements DataModel {
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


    @Override
    public Map<String, String> toPlainTextData() {
        Map<String, String> map = new HashMap<>(Map.of(
                "likes", Integer.toString(likes),
                "dislikes", Integer.toString(dislikes)
        ));
        post.toPlainTextData().forEach((key, value) -> map.put("post."+key, value));
        if (author!=null) author.toPlainTextData().forEach((key, value) -> map.put("author."+key, value));
        if (hub!=null) hub.toPlainTextData().forEach((key, value) -> map.put("hub."+key, value));
        return map;
    }
}
