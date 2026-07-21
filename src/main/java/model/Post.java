package model;

import java.sql.Timestamp;

public class Post implements DataModel {
    private final int id;
    private final int authorId;
    private final Integer hubId;
    private final String label;
    private final String content;
    private final Timestamp creationTime;
    private static final Fields fields = new Fields("id", "author_id", "hub_id", "label", "content", "creation_time");


    public Post(Integer id, Integer authorId, Integer hubId, String label, String content, Timestamp creationTime) {
        this.id = id;
        this.authorId = authorId;
        this.hubId = hubId;
        this.label = label;
        this.content = content;
        this.creationTime = creationTime;
    }

    public int getId() {
        return id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public Integer getHubId() {
        return hubId;
    }

    public String getLabel() {
        return label;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    @Override
    public Fields getFields() { return fields; }
}
