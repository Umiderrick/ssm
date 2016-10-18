package im.vinci.website.domain;

import com.alibaba.druid.support.json.JSONUtils;

import java.util.Map;

/**
 * Created by henryhome on 2/27/15.
 */
public class Result {

    protected Integer status;
    protected Map<String, String> extendedProperties;
    protected String callback;

    public Result() {
        this(200);
    }

    public Result(Integer status) {
        this.status = status;
    }

    public Map<String, String> getExtendedProperties() {
        return extendedProperties;
    }

    public void setExtendedProperties(Map<String, String> extendedProperties) {
        this.extendedProperties = extendedProperties;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

    @Override
    public String toString() {
        return JSONUtils.toJSONString(this);
    }
}




