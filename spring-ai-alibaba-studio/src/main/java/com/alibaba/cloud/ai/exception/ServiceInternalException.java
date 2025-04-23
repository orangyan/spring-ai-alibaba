
package com.alibaba.cloud.ai.exception;

import com.alibaba.cloud.ai.common.ReturnCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
