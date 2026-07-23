package model.data;

import model.common.DataModel;

public class Vote extends DataModel {
    private final int postId;
    private final int voterId;
    private final boolean vote;


    public Vote(Integer postId, Integer voterId, Boolean vote) {
        this.postId = postId;
        this.voterId = voterId;
        this.vote = vote;
    }

    public int getPostId() {
        return postId;
    }

    public int getVoterId() {
        return voterId;
    }

    public int getVoteDelta() {
        if (vote) {
            return 1;
        } else {
            return -1;
        }
    }
}
