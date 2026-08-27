package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Config {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.out.printf("Ошибка ввода вывода, при чтении файла application.properties: %s\n", e.getMessage());
        }
    }

    protected String get(String field, String defaultValue){
        return properties.getOrDefault(field, defaultValue).toString();
    }

    public abstract void load();
}
