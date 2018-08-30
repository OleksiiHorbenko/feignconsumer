package o.horbenko.web.rest;

public class CustomRuntimeException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "CustomRuntimeException.DEFAULT_MESSAGE ";

    public CustomRuntimeException() {
        super(DEFAULT_MESSAGE);
    }

    public CustomRuntimeException(String message) {
        super(message);

    }
}
