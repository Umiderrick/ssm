package im.vinci.website.utils.itsdangerouser.exceptions;

/**
 * Signature timestamp is older than required maxAge.
 *
 * @author aLaserShark
 * @see BadTimeSignatureException
 * @see BadSignatureException
 * @see BadDataException
 * @since 0.1
 */
public class SignatureExpiredException extends BadTimeSignatureException {

    public SignatureExpiredException(String message, String payload, long dateSigned) {
        super(message, payload, dateSigned);
    }

}
