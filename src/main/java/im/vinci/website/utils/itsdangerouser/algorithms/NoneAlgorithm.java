package im.vinci.website.utils.itsdangerouser.algorithms;

/**
 * This class provides a algorithim that does not perform any signing and returns an empty signature.
 *
 * @author aLaserShark
 * @since 0.1
 */
public class NoneAlgorithm extends SigningAlgorithm {

    @Override
    public String getSignature(String key, String value) {
        return "";
    }

}
