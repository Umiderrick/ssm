package im.vinci.website.utils.itsdangerouser.exceptions;

/**
 * Raised if a signed header is invalid in some form.  This only happens for serializers that have a header that goes
 * with the signature.
 *
 * @author aLaserShark
 * @see BadSignatureException
 * @see BadDataException
 * @since 0.1
 */
public class BadHeaderException extends BadSignatureException {

    Object header;
    Exception originalError;

    /**
     * Initalizes a new BadHeaderException
     *
     * @param message       Returned in toString()
     * @param payload       The corrupted payload
     * @param header        The malformed header
     * @param originalError The original exception
     */
    public BadHeaderException(String message, String payload, String header, Exception originalError) {
        super(message, payload);
        this.header = header;
        this.originalError = originalError;
    }

    public Object getHeader() {
        return header;
    }

    public Exception getOriginalError() throws NullPointerException {
        if (originalError.equals(null)) throw new NullPointerException("Original Error is null.");
        else return originalError;
    }

}
