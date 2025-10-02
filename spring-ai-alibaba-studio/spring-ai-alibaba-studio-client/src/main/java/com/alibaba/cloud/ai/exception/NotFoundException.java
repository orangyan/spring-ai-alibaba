
package com.alibaba.cloud.ai.exception;

import com.alibaba.cloud.ai.common.ReturnCode;

public class NotFoundException extends RuntimeException {

	private int code;

	private String msg;

	public NotFoundException() {
		this.code = ReturnCode.RC404.getCode();
		this.msg = ReturnCode.RC404.getMsg();
	}

	public NotFoundException(String msg) {
		this.code = ReturnCode.RC404.getCode();
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
