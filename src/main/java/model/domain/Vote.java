package model.domain;

import model.DataModel;
import model.value.DateTime;

import java.util.HashMap;
import java.util.Map;

public class Vote implements DataModel {
    private final int postId;
    private final int voterId;
    private final Boolean vote;


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
        if (vote == null) {
            return 0;
        }

        if (vote) {
            return 1;
        } else {
            return -1;
        }
    }

    public Boolean get() {return vote;}

    @Override
    public String toString() {
        if (vote) {
            return "(%d <- %d) +".formatted(postId, voterId);
        } else {
            return "(%d <- %d) -".formatted(postId, voterId);
        }
    }


    public static String getFieldsDescription() {
        return "(id Поста <- id Пользователя) Голос";
    }

    @Override
    public Map<String, String> toPlainTextData() {
        return Map.of(
                "postId", Integer.toString(postId),
                "voterId", Integer.toString(voterId),
                "vote", Boolean.toString(vote)
        );
    }
}
