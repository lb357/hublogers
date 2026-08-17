package model.data;

import model.common.DataModel;
import model.common.DateTime;

public class Post extends DataModel {
    private final int id;
    private final int authorId;
    private final Integer hubId;
    private final String label;
    private final String content;
    private final DateTime creationTime;

    public Post(Integer id, Integer authorId, Integer hubId, String label, String content, DateTime creationTime) {
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

    public DateTime getCreationTime() {
        return creationTime;
    }

    @Override
    public String toString() {
        if (hubId == null) {
            return "(%d <- %d) %s // %s // %s".formatted(id, authorId, creationTime.toString(), label, content);
        } else {
            return "(%d <- %d / %d) %s // %s // %s".formatted(id, authorId, hubId, creationTime.toString(), label, content);
        }
    }


    public static String getFieldsDescription() {
        return "(id Поста <- id Пользователя / id Хаба) Время создания // Заголовок // Контент";
    }
}
