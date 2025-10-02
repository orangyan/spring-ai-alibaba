
package com.alibaba.cloud.ai.exception;

import com.alibaba.cloud.ai.common.ReturnCode;

public class ServiceInternalException extends RuntimeException {

	private int code;

	private String msg;

	public ServiceInternalException() {
		this.code = ReturnCode.RC500.getCode();
		this.msg = ReturnCode.RC500.getMsg();
	}

	public ServiceInternalException(String msg) {
		this.code = ReturnCode.RC500.getCode();
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
