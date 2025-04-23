
package com.alibaba.cloud.ai.common;

public class R<T> {

	private Integer code;

	private String msg;

	private T data;

	private long timestamp;

	public R() {
		this.timestamp = System.currentTimeMillis();
	}

	public Integer getCode() {
		return code;
	}

	public R<T> setCode(Integer code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public R<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public T getData() {
		return data;
	}

	public R<T> setData(T data) {
		this.data = data;
		return this;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public R<T> setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public static <T> R<T> success(T data) {
		R<T> r = new R<>();
		r.setCode(ReturnCode.RC200.getCode());
		r.setMsg(ReturnCode.RC200.getMsg());
		r.setData(data);
		return r;
	}

	public static <T> R<T> error(int code, String msg) {
		R<T> r = new R<>();
		r.setCode(code);
		r.setMsg(msg);
		r.setData(null);
		return r;
	}

}
