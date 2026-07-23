package model.composite;

import model.common.DataModel;

public class UserStatistic extends DataModel  {
    private final int userId;
    private final String username;
    private final int postCount;
    private final int hubCount;
    private final int voteCount;
    private final int activeSessionCount;

    public UserStatistic(int userId, String username, int postCount, int hubCount, int voteCount, int activeSessionCount) {
        this.userId = userId;
        this.username = username;
        this.postCount = postCount;
        this.hubCount = hubCount;
        this.voteCount = voteCount;
        this.activeSessionCount = activeSessionCount;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
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
}
