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
        this.driver = get("DATABASE_DRIVER", "org.postgresql.Driver");
        this.url = get("DATABASE_URL", "jdbc:postgresql://localhost/");
        this.username = get("DATABASE_USERNAME", "postgres");
        this.password = get("DATABASE_PASSWORD", "postgres");
        this.insertExampleData = get("DATABASE_INSERT_EXAMPLE_DATA", "TRUE").toLowerCase().trim().equals("true");
        this.initDataBase = get("DATABASE_INIT", "TRUE").toLowerCase().trim().equals("true");
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
