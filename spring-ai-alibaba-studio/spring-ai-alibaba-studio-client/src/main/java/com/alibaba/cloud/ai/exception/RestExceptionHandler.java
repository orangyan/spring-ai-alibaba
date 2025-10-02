
package com.alibaba.cloud.ai.exception;

import com.alibaba.cloud.ai.common.R;
import com.alibaba.cloud.ai.common.ReturnCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseBody
public class RestExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

	@ExceptionHandler(NotFoundException.class)
	public R<String> notFoundException(NotFoundException e) {
		log.error("code={}, NotFoundException = {}", e.getCode(), e.getMessage(), e);
		return R.error(e.getCode(), e.getMsg());
	}

	@ExceptionHandler(NullPointerException.class)
	public R<String> nullPointerException(NullPointerException e) {
		log.error("NullPointerException ", e);
		return R.error(ReturnCode.RC400.getCode(), ReturnCode.RC400.getMsg());
	}

	@ExceptionHandler(RuntimeException.class)
	public R<String> runtimeException(RuntimeException e) {
		log.error("RuntimeException ", e);
		return R.error(ReturnCode.RC500.getCode(), e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public R<String> exception(Exception e) {
		log.error("Unknown exception = {}", e.getMessage(), e);
		return R.error(ReturnCode.RC500.getCode(), ReturnCode.RC500.getMsg());
	}

}
