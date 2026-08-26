package repository;

import config.DatabaseConfig;
import util.AdminOutput;
import util.ResourceReader;

import java.io.*;
import java.sql.*;


public class Database {
    private static boolean ready;
    private static final DatabaseConfig config = new DatabaseConfig();

    public static void start() {
        System.out.println("Конфигурация базы данных...");
        config.load();
        ready = checkConnection();
        if (ready) {
            if (config.isInitDataBase()) {
                initDatabase();
            }
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

    private static void initDatabase(){
        try (Connection connection = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword())) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(ResourceReader.readResource("sql/create_database.sql"));
        } catch (SQLException e) {
            if (!e.getSQLState().equals("42P04") || e.getErrorCode() != 0) { // 42P04 код duplicate_database
                AdminOutput.error(e);
            }
        }
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(ResourceReader.readResource("sql/ddl_init.sql"));
        } catch (SQLException e) {
            AdminOutput.error(e);
        }
        if (config.isInsertExampleData()){
            boolean execDML = false;
            try (Connection connection = getConnection()) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM posts;");
                if (resultSet.next()) {
                    if (resultSet.getInt(1) <= 0) {
                        execDML = true;
                    }
                } else {
                    AdminOutput.error("Поле count в ответе из базы данных обязано существовать, однако отсутствует");
                }
            } catch (SQLException e) {
                AdminOutput.error(e);
            }
            if (execDML) {
                try (Connection connection = getConnection()) {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(ResourceReader.readResource("sql/dml_init.sql"));
                } catch (SQLException e) {
                    AdminOutput.error(e);
                }
            }
        }
    }

    private static boolean checkDriver() {
        try {
            Class.forName(config.getDriver());
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("Нет JDBC-драйвера! Подключите JDBC-драйвер к проекту согласно инструкции.");
            throw new RuntimeException(e);
        }
    }

    private static boolean checkDB() {
        try {
            Connection connection = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Нет подключения к базе данных! Проверьте имя базы, путь к базе или разверните локально резервную копию согласно инструкции");
            return false;
        }
    }

    public static Connection getConnection() throws SQLException {
        if (ready) {
            Connection connection = DriverManager.getConnection(config.getFullUrl(), config.getUsername(), config.getPassword());
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Подключение к базе данных не открылось");
            }
            return connection;
        } else {
            throw new SQLException("Подключение к базе данных не конфигурировано");
        }
    }
}
