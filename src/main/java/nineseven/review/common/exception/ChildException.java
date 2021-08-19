package nineseven.review.common.exception;

public class ChildException extends ParentException {
    public ChildException() {
        super();
    }

    public ChildException(String message) {
        super(message);
    }

    public ChildException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChildException(Throwable cause) {
        super(cause);
    }

    protected ChildException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
