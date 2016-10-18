package im.vinci.website.utils.itsdangerouser;


import im.vinci.website.utils.itsdangerouser.algorithms.HMACAlgorithm;
import im.vinci.website.utils.itsdangerouser.algorithms.SigningAlgorithm;
import im.vinci.website.utils.itsdangerouser.exceptions.BadSignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * This class can sign bytes and unsign it and validate the signature provided.
 * Salt can be used to namespace the hash, so that a signed string is only valid for a given namespace.  Leaving this
 * at the default value or re-using a salt value across different parts of your application where the same signed value
 * in one part can mean something different in another part is a security risk.
 * <p/>
 * The java version currently does not implement the django or concat version of key deriviation. HMAC is the default
 * and as of 1.0 the only way to derive a key.
 *
 * @author aLaserShark
 * @since 0.1
 */
public class Signer {

    String secretKey;
    String salt = "";
    String separator = ".";
    SigningAlgorithm algo = new HMACAlgorithm();

    /**
     * Creates a new Signer object.
     *
     * @param secretKey The secret key used to sign and check signatures.
     * @param salt      The salt. Defaults to none.
     * @param separator The character used to separate the statement and the signature. Defaults to "."
     * @param algo      The SigningAlgorithim used to sign keys. Defaults to HMACAlgorithim.
     */
    public Signer(String secretKey, String salt, String separator, SigningAlgorithm algo) {
        this.secretKey = secretKey;
        this.salt = salt;
        this.separator = separator;
        this.algo = algo;
    }

    public Signer(String secretKey, String salt, String separator) {
        this.secretKey = secretKey;
        this.salt = salt;
        this.separator = separator;
    }

    public Signer(String secretKey, String salt) {
        this.secretKey = secretKey;
        this.salt = salt;
    }

    public Signer(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * This method is called to derive the key.  If you're unhappy with the default key derivation choices you can
     * override them here. Keep in mind that the key derivation in itsdangerous is not intended to be used as a security
     * method to make a complex key out of a short password.  Instead you should use large random secret keys.
     *
     * @return The derived key
     */
    public String deriveKey() {
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");
        Mac mac;
        String s;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
            s = new String(mac.doFinal(salt.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "error";
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return "error";
        }
        return s;
    }

    /**
     * Returns the signature for the given value.
     */
    public String getSignature(String value) {
        String key = this.deriveKey();
        String sig = this.algo.getSignature(key, value);
        return new String(ItsDangerous.base64Encode(sig));
    }

    /**
     * Signs the given string.
     */
    public String sign(String me) {
        return me + this.separator + this.getSignature(me);
    }

    /**
     * Verifies the signature for the given value.
     */
    public boolean verifySignature(String value, String signature) {
        String key = this.deriveKey();
        String decoded = ItsDangerous.base64Decode(signature.getBytes());
        return this.algo.verifySignature(key, value, decoded);
    }

    /**
     * Unsigns the given string.
     */
    public String unsign(String me) throws BadSignatureException {
        if (!me.contains(this.separator.subSequence(0, 1)))
            throw new BadSignatureException("No separator found in value");
        String[] stuffs = me.split(this.separator);
        String value;
        String sig;
        String ts = ".";
        if (stuffs.length == 2) {
            value = stuffs[0];
            ts = "";
            sig = stuffs[1];
        } else if (stuffs.length == 3) {
            value = stuffs[0];
            ts += stuffs[1];
            sig = stuffs[2];
        } else {
            throw new BadSignatureException("Too many periods!");
        }
        if (this.verifySignature(value, sig)) return value + ts;
        else throw new BadSignatureException("Signature does not match", value);
    }

    /**
     * Just validates the given signed value.  Returns `True` if the signature exists and is valid, `False` otherwise
     */
    public boolean validate(String me) {
        try {
            this.unsign(me);
            return true;
        } catch (BadSignatureException e) {
            return false;
        }
    }

}
