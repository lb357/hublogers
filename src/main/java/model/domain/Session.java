package model.domain;

import model.DataModel;
import model.value.DateTime;

import java.util.HashMap;
import java.util.Map;

public class Session implements DataModel {
    private final String authToken;
    private final int userId;
    private final DateTime authTime;

    public Session(String token, int user_id, DateTime auth_time) {
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

    public DateTime getAuthTime() {
        return authTime;
    }

    @Override
    public String toString() {
        return "%s <-> %d (%s)".formatted(authTime, userId, authTime.toString());
    }


    @Override
    public Map<String, String> toPlainTextData() {
        return Map.of(
                "authToken", authToken,
                "userId", Integer.toString(userId),
                "authTime.datetime", authTime.toString(),
                "authTime", Long.toString(authTime.getTime())
        );
    }
}