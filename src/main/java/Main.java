import cli.ConsoleApp;
import service.AdminService;
import web.WebServer;

import java.security.NoSuchAlgorithmException;

import repository.Database;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Database.start();
        WebServer.start();
        System.out.printf("Сгенерирован админ-ключ: %s\n", AdminService.generateAdminKey());
        ConsoleApp.menu();
        WebServer.stop();
    }
}
