package im.vinci.website.utils.itsdangerouser.algorithms;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * This class provides signature generation using HMACs.
 *
 * @author aLaserShark
 * @since 0.1
 */
public class HMACAlgorithm extends SigningAlgorithm {

    @Override
    public String getSignature(String key, String value) {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        Mac mac;
        String s;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
            s = new String(mac.doFinal(value.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "error";
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return "error";
        }
        return s;
    }

}
