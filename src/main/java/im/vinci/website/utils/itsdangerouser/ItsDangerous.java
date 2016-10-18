package im.vinci.website.utils.itsdangerouser;

import org.apache.commons.codec.binary.Base64;

import java.math.BigInteger;

/**
 * "It's Dangerouser" is a pretty straight-up port of It's dangerous (stylized itsdangerous), a simple little thing
 * that can be used to generate signatures. It's written in python, though, which means I can't use it on things like
 * android. So, caught in a coffee driven rage that I could not use this library, I decided to port it to java.
 * <p/>
 * The original code can be found here: https://github.com/mitsuhiko/itsdangerous
 * <p/>
 * This class contains all the static methods.
 *
 * @author aLaserShark
 * @since 0.1
 */
public class ItsDangerous {

    /**
     * base64 encodes a single byte array (and is tolerant to getting called with a unicode string).
     * The resulting byte array is safe for putting into URLs.
     *
     * @param s The string to be encoded.
     * @return The encoded string.
     */
    public static byte[] base64Encode(String s) {
        return Base64.encodeBase64URLSafe(s.getBytes());
    }

    /**
     * base64 decodes a single byte array (and is tolerant to getting called with a unicode string).
     *
     * @param bytes The bytes to be decoded.
     * @return The string representation of the decoded byte array.
     */
    public static String base64Decode(byte[] bytes) {
        return new String(Base64.decodeBase64(bytes));
    }

    public static byte[] intToBytes(int i) {
        BigInteger bi = BigInteger.valueOf(i);
        return bi.toByteArray();
    }

    public static int bytesToInt(byte[] b) {
        BigInteger bi = new BigInteger(b);
        return bi.intValue();
    }

}
