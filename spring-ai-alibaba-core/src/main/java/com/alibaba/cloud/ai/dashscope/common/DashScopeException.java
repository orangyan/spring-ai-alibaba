
package com.alibaba.cloud.ai.dashscope.common;

/**
 * DashScope异常类，继承自RuntimeException
 * 用于处理DashScope应用中发生的运行时错误
 * 继承RuntimeException意味着该异常可以被Java的异常处理机制捕获和处理
 */
public class DashScopeException extends RuntimeException {

	public DashScopeException(String message) {
		super(message);
	}

	public DashScopeException(String message, Throwable cause) {
		super(message, cause);
	}

	public DashScopeException(ErrorCodeEnum error) {
		super(error.getCode() + ":" + error.message());
	}

}
