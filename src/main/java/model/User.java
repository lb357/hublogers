package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class User implements DataModel {
    private final Integer id;
    private final String username;
    private final String email;
    private final String passwordHash;
    private final String status;
    private static final Fields fields = new Fields("id", "username", "email", "password_hash", "status");
    private static final MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

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

    private static String getHashString(String value) {
        byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
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
        return this.passwordHash.equals(getHashString(password));
    }

    @Override
    public Fields getFields() { return fields; }
}
