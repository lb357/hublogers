import cli.ConsoleApp;
import service.AdminService;
import util.AdminOutput;
import web.ViewRenderer;
import web.WebServer;
import repository.Database;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Database.start();
        if (Database.isReady()) {
            WebServer.start();
            AdminOutput.info("Админ-ключ: %s\n".formatted(AdminService.loadAdminKey()));
            ConsoleApp.menu();
            WebServer.stop();
        }
    }
}
