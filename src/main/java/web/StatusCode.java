package web;

public enum StatusCode {
    OK(200),
    BAD_REQUEST(400),
    ACCESS_DENIED(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405);


    final int code;
    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
