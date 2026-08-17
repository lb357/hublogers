package model.data;

import model.common.DataModel;

public class Hub extends DataModel {
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
}