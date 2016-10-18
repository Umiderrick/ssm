package im.vinci.website.utils.itsdangerouser;


import im.vinci.website.utils.itsdangerouser.algorithms.SigningAlgorithm;
import im.vinci.website.utils.itsdangerouser.exceptions.BadSignatureException;
import im.vinci.website.utils.itsdangerouser.exceptions.SignatureExpiredException;

/**
 * Works like the regular Signer but also records the time of the signing and can be used to expire signatures. The
 * unsign method can cause a SignatureExpired method if the unsigning failed because the signature is expired.  This
 * exception is a subclass of BadSignature.
 *
 * @author aLaserShark
 * @since 0.1
 */
public class TimestampSigner extends Signer {

    public TimestampSigner(String secretKey, String salt, String separator, SigningAlgorithm algo) {
        super(secretKey, salt, separator, algo);
    }

    public TimestampSigner(String secretKey, String salt, String separator) {
        super(secretKey, salt, separator);
    }

    public TimestampSigner(String secretKey, String salt) {
        super(secretKey, salt);
    }

    public TimestampSigner(String secretKey) {
        super(secretKey);
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * Signs the given string and also attaches time information.
     */
    @Override
    public String sign(String value) {
        String encodedTS = new String(ItsDangerous.base64Encode(String.valueOf(this.getTimestamp())));
        return value + this.separator + encodedTS + this.separator + this.getSignature(value);
    }

    /**
     * Works like the regular Signer.unsign() but can also validate the time.  See the base docstring of the
     * class for the general behavior.
     *
     * @param maxAge Set to -1 for there to be no max age.
     */
    public String unsign(String value, long maxAge) throws BadSignatureException {
        String result = this.unsign(value);
        if (!result.contains(this.separator.subSequence(0, 1))) throw new BadSignatureException("Timestamp Missing");
        String[] things = result.split(this.separator);
        String valued = things[0];
        String ts = things[1];
        long decoded = ItsDangerous.bytesToInt(ItsDangerous.base64Decode(ts.getBytes()).getBytes());
        if (maxAge > 0 && this.getTimestamp() - decoded > maxAge) throw new SignatureExpiredException(
                "Signature expired", value, decoded);
        return value;
    }

}
