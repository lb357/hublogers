package util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class AdminOutput {
    public static void info(String message) {
        System.out.println("===== АДМИНИСТРАТИВНАЯ ИНФОРМАЦИЯ ===== СИСТЕМНОЕ СООБЩЕНИЕ =====");
        System.out.println(message);
    }

    public static void error(String message) {
        System.out.println("====== АДМИНИСТРАТИВНАЯ ИНФОРМАЦИЯ ===== ВНУТРЕННЯЯ ОШИБКА ======");
        System.out.println(message);
    }

    public static void error(Throwable throwable){
        try (
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ) {
            throwable.printStackTrace(printWriter);
            error(stringWriter.getBuffer().toString());
        } catch (IOException e) {
            error("Ошибка административного вывода: %s".formatted(e.getMessage()));
        }
    }

}
