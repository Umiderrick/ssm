package im.vinci.monitor.exception;

/**
 * @author sunli
 */
public class TimeOutOfBoundsException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 2045627623173374956L;

    public TimeOutOfBoundsException() {
        super();
    }

    public TimeOutOfBoundsException(String message) {
        super(message);
    }
}
