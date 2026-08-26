package web;

import com.sun.net.httpserver.HttpServer;
import config.WebConfig;
import web.controller.*;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;

public class WebServer {
    private static HttpServer server;
    private static final WebConfig config = new WebConfig();

    public static void registerControllers(){
        registerController(new HomeController("/"));
        registerController(new StylesController("/styles.css"));
        registerController(new AdminController("/admin"));
        registerController(new CreateHubController("/create/hub"));
        registerController(new CreatePostController("/create/post"));
        registerController(new FindController("/find"));
        registerController(new HubController("/hub"));
        registerController(new HubsController("/hubs"));
        registerController(new LastController("/last"));
        registerController(new LoginController("/login"));
        registerController(new LogoutController("/logout"));
        registerController(new PostController("/post"));
        registerController(new ProfileController("/profile"));
        registerController(new SignupController("/signup"));
        registerController(new TopController("/top"));
        registerController(new UserController("/user"));
        registerController(new ErrorController("/error"));
    }

    public static void start() {
        System.out.println("Конфигурация веб-сервера...");
        config.load();
        if (config.isEnabled()) {
            init();
            System.out.printf("Веб-сервер запущен: http:/%s\n", server.getAddress());
        } else {
            System.out.println("Веб-сервер не включен");
        }
    }

    private static void init() {
        try {
            server = HttpServer.create(new InetSocketAddress(Inet4Address.getByName(config.getHostName()), config.getPort()), 0);
            registerControllers();
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerController(Controller controller){
        server.createContext(controller.getPath(), controller);
    }

    public static Integer getPort() {
        return config.getPort();
    }

    public static String getHostname() {
        return config.getHostName();
    }

    public static Boolean isEnabled() {
        return config.isEnabled();
    }

    public static void stop() {
        if (config.isEnabled()) {
            System.out.println("Выключение веб-сервера...");
            server.stop(5);
            System.out.println("Веб-сервер выключен");
        }
    }
}
