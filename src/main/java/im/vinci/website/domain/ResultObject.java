package im.vinci.website.domain;

/**
 * Created by henryhome on 2/27/15.
 */
public class ResultObject<T> extends Result {

    private T content;
    
    public ResultObject() {
        super();
    }
    
    public ResultObject(T content) {
        super();
        this.content = content;
    }

    public ResultObject(Integer status, T content) {
        super(status);
        this.content = content;
    }

    public T getContent() {
        return content;
    }
    
    public void setContent(T content) {
        this.content = content;
    }
}
