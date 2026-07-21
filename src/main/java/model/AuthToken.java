package model;

import java.sql.Timestamp;

public class AuthToken implements DataModel {
    private final String token;
    private final int user_id;
    private final Timestamp auth_time;

    private static final Fields fields = new Fields("token", "user_id", "auth_time");

    public AuthToken(String token, int user_id, Timestamp auth_time) {
        this.token = token;
        this.user_id = user_id;
        this.auth_time = auth_time;
    }

    public String getToken() {
        return token;
    }

    public int getUser_id() {
        return user_id;
    }

    public Timestamp getAuth_time() {
        return auth_time;
    }

    @Override
    public Fields getFields() { return fields; }
}