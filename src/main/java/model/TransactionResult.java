package model;

import java.sql.SQLException;

public class TransactionResult<T> {
    private final T data;
    private final boolean success;
    private final String message;

    private TransactionResult(T data, boolean success, String message) {
        this.data = data;
        this.success = success;
        this.message = message;
    }

    public static <T> TransactionResult<T> successResponse(T data) throws SQLException {
        if (data == null) {
            throw new SQLException("В успешном (не Void) результате data == null");
        }
        return new TransactionResult<>(data, true, "OK");
    }

    public static TransactionResult<Void> successResponse() throws SQLException {
        return new TransactionResult<>(null, true, "OK");
    }

    public static <T> TransactionResult<T> failResponse(String message){
        return new TransactionResult<>(null, false, message);
    }

    public static <T> TransactionResult<T> errorResponse(){
        return new TransactionResult<>(null, false, "Внутренняя ошибка");
    }

    public T getData() { return data; }

    public String getMessage() { return message; }

    public boolean isSuccess() { return success; }

    public <K> TransactionResult<K> transferFailure() throws SQLException {
        if (data == null) {
            return new TransactionResult<>(null, false, message);
        } else {
            throw new SQLException("data != null");
        }
    }
}
