package repository;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static String DRIVER;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static boolean ready;

    public static void start() {
        System.out.println("Конфигурация базы данных...");
        loadConfig();
        ready = checkConnection();
        if (ready) {
            System.out.println("База данных подключена!");
        } else {
            System.out.println("База данных НЕ ПОДКЛЮЧЕНА!");
        }
    }

    public static boolean isReady() {
        return ready;
    }

    private static boolean checkConnection(){
        return checkDriver() && checkDB();
    }

    private static void loadConfig(){
        boolean loaded = false;
        Properties properties = new Properties();

        try (InputStream input = Database.class.getClassLoader().getResourceAsStream("database.properties")) {
            properties.load(input);
            DRIVER = (String) properties.getOrDefault("database-driver", null);
            URL = (String) properties.getOrDefault("database-url", null);
            USERNAME = (String) properties.getOrDefault("database-username", null);
            PASSWORD = (String) properties.getOrDefault("database-password", null);
            loaded = DRIVER != null && URL != null && USERNAME != null && PASSWORD != null;
        } catch (FileNotFoundException e) {
            System.out.printf("Файл database.properties не найден: %s\n", e.getMessage());
        } catch (Exception e) {
            System.out.printf("Ошибка чтения файла database.properties: %s\n", e.getMessage());
        }

        if (!loaded) {
            System.out.println("В файле database.properties не найдены необходимые поля");
            System.out.println("Значения database-driver, database-url, database-username, database-password файла database.properties используются по умолчанию");
            DRIVER = "org.postgresql.Driver";
            URL = "jdbc:postgresql://localhost/hublogers";
            USERNAME = "postgres";
            PASSWORD = "postgres";
        }

    }

    private static boolean checkDriver() {
        try {
            Class.forName(DRIVER);
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("Нет JDBC-драйвера! Подключите JDBC-драйвер к проекту согласно инструкции.");
            throw new RuntimeException(e);
        }
    }

    private static boolean checkDB() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Нет подключения к базе данных! Проверьте имя базы, путь к базе или разверните локально резервную копию согласно инструкции");
            return false;
        }
    }

    public static Connection getConnection() throws SQLException {
        if (ready) {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Подключение к базе данных не открылось");
            }
            return connection;
        } else {
            throw new SQLException("Подключение к базе данных не конфигурировано");
        }
    }
}
