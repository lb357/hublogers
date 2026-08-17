package model.composite;

import model.common.DataModel;
import model.data.Hub;
import model.data.User;

public class MetaHub extends DataModel {
    private final Hub hub;
    private final User creator;

    public MetaHub(Hub hub, User creator) {
        this.hub = hub;
        this.creator = creator;
    }

    public Hub getHub() {
        return hub;
    }

    public User getCreator() {
        return creator;
    }

    @Override
    public String toString() {
        if (creator != null) {
            return "(%d / %s / %d / %s)\n%s\n".formatted(
                    hub.getId(),
                    hub.getHubname(),
                    creator.getId(),
                    creator.getUsername(),
                    hub.getDescription()
            );
        } else {
            return "(%d / %s)\n%s\n".formatted(
                    hub.getId(),
                    hub.getHubname(),
                    hub.getDescription()
            );
        }
    }


    public static String getFieldsDescription() {
        return "(id Хаба / Название Хаба / id Создателя / Имя создателя)\nОписание хаба\n";
    }
}
