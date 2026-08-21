package config;

public class AdminConfig extends Config {
    private String adminKey;

    public void load() {
        this.adminKey = get("ADMIN_KEY", util.SecureStringGenerator.getTokenString());
    }

    public String getAdminKey() {
        return adminKey;
    }
}
