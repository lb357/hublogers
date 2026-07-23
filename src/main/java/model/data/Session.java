package model.data;

import model.common.DataModel;

import java.sql.Timestamp;

public class Session extends DataModel {
    private final String authToken;
    private final int userId;
    private final Timestamp authTime;

    public Session(String token, int user_id, Timestamp auth_time) {
        this.authToken = token;
        this.userId = user_id;
        this.authTime = auth_time;
    }

    public String getAuthToken() {
        return authToken;
    }

    public int getUserId() {
        return userId;
    }

    public Timestamp getAuthTime() {
        return authTime;
    }
}