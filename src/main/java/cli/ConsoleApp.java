package cli;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class ConsoleApp {
    private static Scanner scanner = new Scanner(System.in);

    public static void menu() {
        String cmd = "";
        while (!cmd.equals("exit")) {
            renderMenu(new HashMap<>(){{
                put("Лента постов", ConsoleApp::feed);
            }});
            cmd = scanner.nextLine();
        }
        scanner.close();
    }

    public static void renderMenu(HashMap<String, Runnable> data){
        renderSeparator();
        HashMap<Integer, String> chooseMap = new HashMap<Integer, String>();
        int i = 0;
        for (String key: data.keySet()) {
            i++;
            System.out.printf("%d) %s\n", i, key);
            chooseMap.put(i, key);
        }
    }

    public static void renderSeparator() {
        System.out.println("==================================================");
    }


    public static void feed() {
        renderSeparator();
    }
}
