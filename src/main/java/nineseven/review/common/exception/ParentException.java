package nineseven.review.common.exception;

public class ParentException extends RuntimeException {
    public ParentException() {
        super();
    }

    public ParentException(String message) {
        super(message);
    }

    public ParentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParentException(Throwable cause) {
        super(cause);
    }

    protected ParentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}