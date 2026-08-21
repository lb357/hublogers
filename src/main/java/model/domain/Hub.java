package model.domain;

import model.DataModel;
import model.value.DateTime;

import java.util.HashMap;
import java.util.Map;

public class Hub implements DataModel {
    private final int id;
    private final Integer creatorId;
    private final String hubname;
    private final String description;

    public Hub(Integer id, Integer creatorId, String hubname, String description) {
        this.id = id;
        this.creatorId = creatorId;
        this.hubname = hubname;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getHubname() {
        return hubname;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        if (creatorId != null) {
            return "(%d <- %d) %s - %s".formatted(
                    id,
                    creatorId,
                    hubname,
                    description
            );
        } else {
            return "(%d) %s - %s".formatted(
                    id,
                    hubname,
                    description
            );
        }
    }


    public static String getFieldsDescription() {
        return "(id Хаба <- id Пользователя) Название - Описание";
    }

    @Override
    public Map<String, String> toPlainTextData() {
        Map<String, String> map = new HashMap<>(Map.of(
                "id", Integer.toString(id),
                "hubname", hubname,
                "description", description
        ));
        if(creatorId!=null) map.put("creatorId", Integer.toString(creatorId));
        return map;
    }
}