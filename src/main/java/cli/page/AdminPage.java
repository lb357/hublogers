package cli.page;

import cli.ConsoleApp;
import model.composite.UserStatistic;
import model.domain.*;
import service.AdminService;
import service.TransactionResult;

import java.util.ArrayList;

public class AdminPage extends Page {
    @Override
    public void render() {
        System.out.println("Введите админ-ключ для продолжения:");
        String adminKey = scanner.nextLine();
        if (AdminService.checkAdminKey(adminKey)) {
            boolean repeat = true;
            while (repeat) {
                System.out.println("\n1) Продемонстрировать данные из таблицы users");
                System.out.println("2) Продемонстрировать данные из таблицы sessions");
                System.out.println("3) Продемонстрировать данные из таблицы hubs");
                System.out.println("4) Продемонстрировать данные из таблицы posts");
                System.out.println("5) Продемонстрировать данные из таблицы votes");
                System.out.println("6) Удалить пост");
                System.out.println("7) Удалить пользователя");
                System.out.println("8) Удалить хаб");
                System.out.println("9) Продемонстрировать статистику всех пользователей");
                System.out.println("0) Назад");
                switch (renderSelect(9)) {
                    case 0 -> {
                        repeat = false;
                        ConsoleApp.setPage(new HomePage());
                    }
                    case 1 -> {
                        System.out.println("Данные из таблицы users\nФормат:");
                        System.out.println(User.getFieldsDescription());
                        System.out.println();
                        TransactionResult<ArrayList<User>> transactionResult = AdminService.getAllUsers(adminKey);
                        if (transactionResult.isSuccess()){
                            transactionResult.getData().forEach(System.out::println);
                        } else {
                            renderFailMessage(transactionResult.getMessage());
                        }
                    }
                    case 2 -> {
                        System.out.println("Данные из таблицы sessions\nФормат:");
                        System.out.println(Session.getFieldsDescription());
                        System.out.println();
                        TransactionResult<ArrayList<Session>> transactionResult = AdminService.getAllSession(adminKey);
                        if (transactionResult.isSuccess()){
                            transactionResult.getData().forEach(System.out::println);
                        } else {
                            renderFailMessage(transactionResult.getMessage());
                        }
                    }
                    case 3 -> {
                        System.out.println("Данные из таблицы hubs\nФормат:");
                        System.out.println(Hub.getFieldsDescription());
                        System.out.println();
                        TransactionResult<ArrayList<Hub>> transactionResult = AdminService.getAllHubs(adminKey);
                        if (transactionResult.isSuccess()){
                            transactionResult.getData().forEach(System.out::println);
                        } else {
                            renderFailMessage(transactionResult.getMessage());
                        }
                    }
                    case 4 -> {
                        System.out.println("Данные из таблицы posts\nФормат:");
                        System.out.println(Hub.getFieldsDescription());
                        System.out.println();
                        TransactionResult<ArrayList<Post>> transactionResult = AdminService.getAllPosts(adminKey);
                        if (transactionResult.isSuccess()){
                            transactionResult.getData().forEach(System.out::println);
                        } else {
                            renderFailMessage(transactionResult.getMessage());
                        }
                    }
                    case 5 -> {
                        System.out.println("Данные из таблицы votes\nФормат:");
                        System.out.println(Hub.getFieldsDescription());
                        System.out.println();
                        TransactionResult<ArrayList<Vote>> transactionResult = AdminService.getAllVotes(adminKey);
                        if (transactionResult.isSuccess()){
                            transactionResult.getData().forEach(System.out::println);
                        } else {
                            renderFailMessage(transactionResult.getMessage());
                        }
                    }
                    case 6 -> {
                        System.out.println("Введите id поста для удаления:");
                        AdminService.deletePost(adminKey, renderSelect());
                    }
                    case 7 -> {
                        System.out.println("Введите id пользователя для удаления:");
                        AdminService.deleteUser(adminKey, renderSelect());
                    }
                    case 8 -> {
                        System.out.println("Введите id хаба для удаления:");
                        AdminService.deleteHub(adminKey, renderSelect());
                    }
                    case 9 -> {
                        System.out.println("Статистика пользователей (данные из всех таблиц)\nФормат:");
                        System.out.println(UserStatistic.getFieldsDescription());
                        System.out.println();
                        TransactionResult<ArrayList<UserStatistic>> transactionResult = AdminService.getAllUsersStatistic(adminKey);
                        if (transactionResult.isSuccess()){
                            transactionResult.getData().forEach(System.out::println);
                        } else {
                            renderFailMessage(transactionResult.getMessage());
                        }
                    }
                }
            }
        } else {
            renderFailMessage("Неверный админ-ключ");
        }
    }

    @Override
    public String getPageName() {
        return "АДМИН-ПАНЕЛь";
    }
}
