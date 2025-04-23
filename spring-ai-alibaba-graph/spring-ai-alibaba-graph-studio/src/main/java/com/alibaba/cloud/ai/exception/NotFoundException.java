
package com.alibaba.cloud.ai.exception;

import com.alibaba.cloud.ai.common.ReturnCode;

public class NotFoundException extends RuntimeException {

	private int code;

	private String msg;

	public int getCode() {
		return code;
	}

	public NotFoundException setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public NotFoundException setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public NotFoundException() {
		this.code = ReturnCode.RC404.getCode();
		this.msg = ReturnCode.RC404.getMsg();
	}

	public NotFoundException(String msg) {
		this.code = ReturnCode.RC404.getCode();
		this.msg = msg;
	}

}
