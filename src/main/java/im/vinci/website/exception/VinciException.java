package im.vinci.website.exception;

import org.springframework.http.HttpStatus;

/**
 * Vinci自定义的Exception
 * Created by tim@vinci on 16/1/6.
 */
public class VinciException extends RuntimeException {

    private Integer errorCode;
    private String errorMsgToUser;

    public VinciException() {
        this.errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public VinciException(Integer errorCode, String errorMsg, String errorMsgToUser) {
        this(null,errorCode,errorMsg,errorMsgToUser);
    }


    public VinciException(Throwable cause) {
        super(cause);
    }

    public VinciException(Throwable cause, Integer errorCode, String errorMsg, String errorMsgToUser) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsgToUser = errorMsgToUser;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsgToUser() {
        return errorMsgToUser;
    }

    public void setErrorMsgToUser(String errorMsgToUser) {
        this.errorMsgToUser = errorMsgToUser;
    }
}