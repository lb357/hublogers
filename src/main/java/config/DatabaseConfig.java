package config;

public class DatabaseConfig extends Config {
    private String driver;
    private String url;
    private String username;
    private String password;
    private boolean insertExampleData;
    private boolean initDataBase;
    private static final String DATABASE_NAME = "hublogers";

    public void load() {
        this.driver = get("database-driver", "org.postgresql.Driver");
        this.url = get("database-url", "jdbc:postgresql://localhost/");
        this.username = get("database-username", "postgres");
        this.password = get("database-password", "postgres");
        this.insertExampleData = get("database-insert-example-data", "false").toLowerCase().trim().equals("true");
        this.initDataBase = get("database-init", "false").toLowerCase().trim().equals("true");
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public String getFullUrl() {
        return url+DATABASE_NAME;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isInsertExampleData() {
        return insertExampleData;
    }

    public boolean isInitDataBase() {
        return initDataBase;
    }
}
