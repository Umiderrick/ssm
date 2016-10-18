package im.vinci.website.utils.itsdangerouser.exceptions;

/**
 * An exception that is passed whenever there is any sort of bad data.
 *
 * @author aLaserShark
 * @since 0.1
 */
public class BadDataException extends Exception {

    public String message;

    /**
     * Creates a new BadDataException.
     *
     * @param message Returned in toString()
     */
    public BadDataException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
