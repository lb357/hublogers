package model.domain;

import model.DataModel;
import model.value.DateTime;
import util.SecureStringGenerator;

import java.util.HashMap;
import java.util.Map;

public class User implements DataModel {
    private final Integer id;
    private final String username;
    private final String email;
    private final String passwordHash;
    private final String status;

    public User(Integer id, String username, String email, String passwordHash, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.status = status;
    }

    public User(Integer id, String username, String status) {
        this.id = id;
        this.username = username;
        this.email = null;
        this.passwordHash = null;
        this.status = status;
    }


    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getStatus() {
        return status;
    }

    public boolean checkPassword(String password) {
        if (this.passwordHash != null) {
            return this.passwordHash.equals(SecureStringGenerator.getSHA256String(password));
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (email == null || passwordHash == null) {
            return "(%d) %s - %s".formatted(id, username, status);
        } else {
            return "(%d) %s - %s / %s - %s".formatted(id, username, status, email, passwordHash);
        }
    }


    public static String getFieldsDescription() {
        return "(id Пользователя) Имя - Статус / Адрес электронной почты - Хэш пароля";
    }

    @Override
    public Map<String, String> toPlainTextData() {
        Map<String, String> map = new HashMap<>(Map.of(
                "id", Integer.toString(id),
                "username", username,
                "status", status
        ));
        if (email!=null) map.put("email", email);
        if (passwordHash!=null) map.put("passwordHash", passwordHash);
        return map;
    }
}
