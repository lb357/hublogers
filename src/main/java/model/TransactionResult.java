package model;

public class TransactionResult<T> {
    private final T data;
    private final String message;
    private final boolean success;

    public TransactionResult(String state, int code) {
        this.data = null;
        this.message = "%s (%d)".formatted(state, code);
        this.success = false;
    }

    public TransactionResult(T data) {
        this.data = data;
        this.message = "OK";
        this.success = true;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
