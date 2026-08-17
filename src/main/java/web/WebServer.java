package web;

import com.sun.net.httpserver.HttpServer;
import repository.Database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.Properties;

public class WebServer {
    private static HttpServer server;
    private static Boolean ENABLED;
    private static String HOSTNAME;
    private static Integer PORT;

    public static void start() {
        System.out.println("Конфигурация веб-сервера...");
        loadConfig();
        if (ENABLED) {
            init();
            System.out.printf("Веб-сервер запущен: http:/%s\n", server.getAddress());
        } else {
            System.out.println("Веб-сервер не включен");
        }
    }

    private static void init() {
        try {
            server = HttpServer.create(new InetSocketAddress(Inet4Address.getByName(HOSTNAME), PORT), 0);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadConfig() {
        Properties properties = new Properties();
        boolean loaded = false;
        try (InputStream input = Database.class.getClassLoader().getResourceAsStream("webserver.properties")) {
            properties.load(input);
            ENABLED = (Boolean)Boolean.parseBoolean((String)properties.getOrDefault("webserver-enabled", null));
            PORT = (Integer)Integer.parseInt((String) properties.getOrDefault("webserver-port", null));
            HOSTNAME = (String)properties.getOrDefault("webserver-hostname", null);
            loaded = PORT != null && ENABLED != null && HOSTNAME != null;
        } catch (FileNotFoundException e) {
            System.out.printf("Файл webserver.properties не найден: %s\n", e.getMessage());
        } catch (Exception e) {
            System.out.printf("Ошибка чтения файла webserver.properties: %s\n", e.getMessage());
        }

        if (!loaded) {
            System.out.println("В файле webserver.properties не найдены необходимые поля");
            System.out.println("Значения webserver-enabled, webserver-port, webserver-hostname файла webserver.properties используются по умолчанию");
            ENABLED = false;
            PORT = 80;
            HOSTNAME = "127.0.0.1";
        }
    }

    public static Integer getPort() {
        return PORT;
    }

    public static String getHostname() {
        return HOSTNAME;
    }

    public static Boolean isEnabled() {
        return ENABLED;
    }

    public static void stop() {
        if (ENABLED) {
            System.out.println("Выключение веб-сервера...");
            server.stop(5);
            System.out.println("Веб-сервер выключен");
        }
    }
}
