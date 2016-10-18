package im.vinci.monitor.exception;

/**
 * @author sunli
 */
public class MonitorNotExistsException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MonitorNotExistsException() {
        super();
    }

    public MonitorNotExistsException(String message) {
        super(message);
    }
}
