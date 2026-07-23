package model.common;

public class TransactionResult<T> {
    private final T data;
    private final boolean success;

    private final String state;
    private final Integer code;

    public TransactionResult(String state, Integer code) {
        this.data = null;
        this.success = false;
        this.state = state;
        this.code = code;
    }

    public TransactionResult(T data) {
        this.data = data;
        this.success = true;
        this.state = null;
        this.code = null;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        if (state == null && code == null) {
            return "OK";
        } else {
            return "%s (%d)".formatted(state, code);
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public <K> TransactionResult<K> transferFail(){
        return new TransactionResult<>(state, code);
    }
}
