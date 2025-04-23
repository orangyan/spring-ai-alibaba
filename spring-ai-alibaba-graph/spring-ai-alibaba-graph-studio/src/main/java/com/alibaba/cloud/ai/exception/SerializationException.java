
package com.alibaba.cloud.ai.exception;

import com.alibaba.cloud.ai.common.ReturnCode;

public class SerializationException extends RuntimeException {

	private int code;

	private String msg;

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public SerializationException(String message) {
		super(message);
		this.code = ReturnCode.RC500.getCode();
		this.msg = message;
	}

	public SerializationException(String message, Throwable cause) {
		super(message, cause);
		this.code = ReturnCode.RC500.getCode();
		this.msg = message;
	}

}
