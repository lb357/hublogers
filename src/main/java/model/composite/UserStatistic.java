package model.composite;

import model.common.DataModel;
import model.data.User;

public class UserStatistic extends DataModel {
    private final User user;
    private final int postCount;
    private final int hubCount;
    private final int voteCount;
    private final int activeSessionCount;

    public UserStatistic(User user, int postCount, int hubCount, int voteCount, int activeSessionCount) {
        this.user = user;
        this.postCount = postCount;
        this.hubCount = hubCount;
        this.voteCount = voteCount;
        this.activeSessionCount = activeSessionCount;
    }

    public User getUser() {
        return user;
    }

    public int getPostCount() {
        return postCount;
    }

    public int getHubCount() {
        return hubCount;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public int getActiveSessionCount() {
        return activeSessionCount;
    }

    @Override
    public String toString() {
        return "(%d - %s) %d / %d / %d / %d".formatted(user.getId(), user.getUsername(), postCount, hubCount, voteCount, activeSessionCount);
    }


    public static String getFieldsDescription() {
        return "(id Пользователя - Имя Пользователя) Количество Постов / Количество Хабов / Количество Голосов / Количество активных сессий";
    }
}
