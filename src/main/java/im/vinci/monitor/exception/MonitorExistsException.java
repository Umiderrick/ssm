package im.vinci.monitor.exception;

/**
 * @author sunli
 */
public class MonitorExistsException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MonitorExistsException() {
        super();
    }

    public MonitorExistsException(String message) {
        super(message);
    }
}
