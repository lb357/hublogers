package model.composite;

import model.DataModel;
import model.domain.Hub;
import model.domain.User;

import java.util.HashMap;
import java.util.Map;

public class MetaHub implements DataModel {
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

    @Override
    public Map<String, String> toPlainTextData() {
        Map<String, String> map = new HashMap<>();
        hub.toPlainTextData().forEach((key, value) -> map.put("hub."+key, value));
        if (creator!=null) creator.toPlainTextData().forEach((key, value) -> map.put("creator."+key, value));
        return map;
    }
}
