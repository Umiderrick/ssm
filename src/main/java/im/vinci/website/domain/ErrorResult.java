package im.vinci.website.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

/**
 * Created by tim@vinci on 16/1/6.
 */
public class ErrorResult extends Result{
    @JsonIgnore
    private String message;
    private String messageToUser;

    public ErrorResult(Integer errorCode, String message, String messageToUser) {
        super(errorCode);
        this.message = message;
        this.messageToUser = messageToUser;
    }

    public ErrorResult(HttpStatus errorCode, String message, String messageToUser) {
        super(errorCode.value());
        this.message = message;
        this.messageToUser = messageToUser;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageToUser() {
        return messageToUser;
    }

    public void setMessageToUser(String messageToUser) {
        this.messageToUser = messageToUser;
    }
}
