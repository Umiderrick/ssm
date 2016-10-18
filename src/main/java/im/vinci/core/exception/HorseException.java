package im.vinci.core.exception;

import im.vinci.core.util.G4Constants;

/**
 * G4公共异常类<br>
 * 
 * @author OSWorks-XC
 * @since 2011-04-27
 */
public class HorseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2109854784072139874L;

	public HorseException() {
		super();
	}

	public HorseException(String msg) {
		super(G4Constants.Exception_Head + msg);
	}
	
	public static final int CODE_NONEXISTENT_USER = 10000001;// 不存在的用户
	public static final int CODE_CRITERIA_EXCEPTION = 10000002;//结果重叠
	public static final int CODE_SERVICE_EXCEPTION = 10000003;//调用服务发生异常
	public static final int CODE_PERSISTENCE_EXCEPTION = 10000004;//持久化发生异常
	private Integer code;

	private String message;

	public HorseException(Integer code) {
		super();
		this.code = code;
	}

	public HorseException(Integer code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public HorseException(Integer code, String message, Throwable cause) {
		super(cause);
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "code ..." + code;
	}
}
