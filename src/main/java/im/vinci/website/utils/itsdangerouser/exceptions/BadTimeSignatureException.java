package im.vinci.website.utils.itsdangerouser.exceptions;

/**
 * Raised for time-based signatures that fail. Extends BadSignatureException.
 *
 * @author aLaserShark
 * @see BadSignatureException
 * @see BadDataException
 * @since 0.1
 */
public class BadTimeSignatureException extends BadSignatureException {

    Object dateSigned;

    /**
     * If the signature was expired, dateSigned exposes the date it was created.
     *
     * @param message    Returned in toString()
     * @param payload    The corrupted payload
     * @param dateSigned The date the payload was signed.
     */
    public BadTimeSignatureException(String message, String payload, long dateSigned) {
        super(message, payload);
        this.dateSigned = dateSigned;
    }

    public Object getDateSigned() throws NullPointerException {
        return dateSigned;
    }
}
