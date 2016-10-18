package im.vinci.website.utils.itsdangerouser.algorithms;

/**
 * Subclasses of `SigningAlgorithm` have to implement `getSignature` to provide signature generation functionality.
 *
 * @author aLaserShark
 * @since 0.1
 */
public abstract class SigningAlgorithm {

    /**
     * Returns the signature for the given key and value.
     */
    public abstract String getSignature(String key, String value);

    /**
     * Verifies the given signature matches the expected signature.
     */
    public boolean verifySignature(String key, String value, String signature) {
        return signature.equals(this.getSignature(key, value));
    }

}
