package util;

import java.io.InputStream;

public class ResourceReader {
    public static String readResource(String fileName) {
        try (InputStream inputStream = ResourceReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) throw new IllegalArgumentException("Поток данных файла %s == null".formatted(fileName));
            return new String(inputStream.readAllBytes());
        } catch (Exception e) {
            AdminOutput.error(e);
            return null;
        }
    }
}
