package model.domain;

import model.DataModel;
import model.value.DateTime;

import java.util.HashMap;
import java.util.Map;

public class Post implements DataModel {
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


    @Override
    public Map<String, String> toPlainTextData() {
        Map<String, String> map = new HashMap<>(Map.of(
                "id", Integer.toString(id),
                "authorId", Integer.toString(authorId),
                "label", label,
                "content", content,
                "creationTime.datetime", creationTime.toString()
        ));
        if (hubId!=null) map.put("hubId", Integer.toString(hubId));
        return map;
    }
}
