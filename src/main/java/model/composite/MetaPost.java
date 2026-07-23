package model.composite;

import model.common.DataModel;

import java.sql.Timestamp;

public class MetaPost extends DataModel {
    private final int postId;
    private final int authorId;
    private final int hubId;
    private final String label;
    private final Timestamp creationTime;
    private final int likes;
    private final int dislikes;
    private final String username;
    private final String hubname;

    public MetaPost(int postId, int authorId, int hubId, String label, Timestamp creationTime, int likes, int dislikes, String username, String hubname) {
        this.postId = postId;
        this.authorId = authorId;
        this.hubId = hubId;
        this.label = label;
        this.creationTime = creationTime;
        this.likes = likes;
        this.dislikes = dislikes;
        this.username = username;
        this.hubname = hubname;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public int getPostId() {
        return postId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getUsername() {
        return username;
    }

    public int getHubId() {
        return hubId;
    }

    public String getHubname() {
        return hubname;
    }

    public String getLabel() {
        return label;
    }


    public Timestamp getCreationTime() {
        return creationTime;
    }
}
