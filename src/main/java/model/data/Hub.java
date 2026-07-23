package model.data;

import model.common.DataModel;

public class Hub extends DataModel {
    private final int id;
    private final int creatorId;
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
}