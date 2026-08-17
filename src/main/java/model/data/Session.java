package model.data;

import model.common.DataModel;
import model.common.DateTime;

public class Session extends DataModel {
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


    public static String getFieldsDescription() {
        return "Токен <-> id Пользователя (Время создания)";
    }
}