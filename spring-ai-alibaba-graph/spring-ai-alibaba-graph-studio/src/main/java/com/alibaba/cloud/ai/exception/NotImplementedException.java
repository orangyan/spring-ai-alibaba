
package com.alibaba.cloud.ai.exception;

import com.alibaba.cloud.ai.common.ReturnCode;

public class NotImplementedException extends RuntimeException {

	private int code;

	private String msg;

	public int getCode() {
		return code;
	}

	public NotImplementedException setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public NotImplementedException setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public NotImplementedException() {
		super();
		this.code = ReturnCode.RC501.getCode();
		this.msg = ReturnCode.RC501.getMsg();
	}

	public NotImplementedException(String msg) {
		super(msg);
		this.code = ReturnCode.RC501.getCode();
		this.msg = msg;
	}

}
