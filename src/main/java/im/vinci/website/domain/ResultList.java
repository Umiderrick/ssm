package im.vinci.website.domain;

import java.util.List;

/**
 * Created by henryhome on 2/27/15.
 */
public class ResultList<T> extends Result {

    private List<T> contentList;

    public ResultList() {
        super();
    }
    
    public ResultList(List<T> contentList) {
        super();
        this.contentList = contentList;
    }

    public ResultList(Integer status, List<T> contentList) {
        super(status);
        this.contentList = contentList;
    }

    public List<T> getContentList() {
        return contentList;
    }

    public void setContentList(List<T> contentList) {
        this.contentList = contentList;
    }
}



