package im.vinci.website.utils.itsdangerouser.exceptions;

/**
 * This error is raised if a signature does not match.
 *
 * @author aLaserShark
 * @see BadDataException
 * @since 0.1
 */
public class BadSignatureException extends BadDataException {

    public Object payload;

    /**
     * The payload is passed as a generic object.
     *
     * @param message Returned in toString()
     * @param payload The corrupted payload
     */
    public BadSignatureException(String message, String payload) {
        super(message);
        this.payload = payload;
    }

    public BadSignatureException(String message) {
        super(message);
    }

    public Object getPayload() throws NullPointerException {
        return payload;
    }

}
