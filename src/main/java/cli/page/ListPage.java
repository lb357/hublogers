package cli.page;

import cli.ConsoleApp;
import model.composite.MetaPost;
import service.TransactionResult;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

public class ListPage<T> extends Page {
    private final Function<Integer, TransactionResult<ArrayList<T>>> dataGetter;
    private final Supplier<TransactionResult<Integer>> maxPageGetter;
    private final Page choicePage;
    private int currentPage = 0;

    public ListPage(
            Function<Integer, TransactionResult<ArrayList<T>>> dataGetter,
            Supplier<TransactionResult<Integer>> maxPageGetter,
            Page choicePage
    ) {
        this.dataGetter = dataGetter;
        this.maxPageGetter = maxPageGetter;
        this.choicePage = choicePage;
    }

    @Override
    public void render() {
        TransactionResult<Integer> maxPageTransactionResult = maxPageGetter.get();
        if (maxPageTransactionResult.isSuccess()) {
            int maxPage = maxPageTransactionResult.getData();
            TransactionResult<ArrayList<T>> dataTransactionResult = dataGetter.apply(currentPage);
            if (dataTransactionResult.isSuccess()) {
                ArrayList<T> data = dataTransactionResult.getData();
                if (data == null || data.isEmpty()) {
                    renderFailMessage("Посты не найдены...");
                } else {
                    String description = "(id Поста / Имя Пользователя / id Хаба / Название Хаба) Время создания\nЗаголовок [Рейтинг]\n";
                    System.out.printf("Формат:\n%s\nСтраница: %d/%d\n\n", description, currentPage + 1, maxPage + 1);
                    data.forEach(System.out::println);
                    System.out.println("1) Просмотреть");
                    System.out.println("2) Предыдущая страница");
                    System.out.println("3) Следующая страница");
                    System.out.println("0) Назад");
                    switch (renderSelect(3)) {
                        case 1 -> {
                            ConsoleApp.setPage(choicePage);
                        }
                        case 2 -> {
                            currentPage = Math.min(maxPage, currentPage+1);
                        }
                        case 3 -> {
                            currentPage = Math.max(0, currentPage-1);
                        }
                        case 0 -> {
                            ConsoleApp.setPage(new HomePage());
                        }
                    }
                }
            } else {
                renderFailMessage(dataTransactionResult.getMessage());
            }
        } else {
            renderFailMessage(maxPageTransactionResult.getMessage());
        }
    }

    @Override
    public String getPageName() {
        return "ПОСТЫ";
    }
}
