package im.vinci.website.utils.itsdangerouser.exceptions;

/**
 * This error is raised in situations when a payload is loaded without checking the signature first.
 *
 * @author aLaserShark
 * @see BadDataException
 * @since 0.1
 */
public class BadPayloadException extends BadDataException {

    Exception originalException;

    /**
     * You can pass the original exception that caused the error as the second parameter.
     *
     * @param message           Returned in toString()
     * @param originalException The original exception.
     */
    public BadPayloadException(String message, Exception originalException) {
        super(message);
        this.originalException = originalException;
    }

    public Exception getOriginalException() throws NullPointerException {
        if (originalException.equals(null)) throw new NullPointerException("No original exception.");
        else return originalException;
    }

}
