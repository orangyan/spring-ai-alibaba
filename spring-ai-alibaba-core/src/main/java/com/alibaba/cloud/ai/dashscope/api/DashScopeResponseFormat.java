
package com.alibaba.cloud.ai.dashscope.api;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DashScope 响应格式类
 * 
 * 用于指定返回内容的格式，支持以下格式：
 * - text: 文本格式响应
 * - json_object: JSON 对象格式响应
 * 
 * 参考文档：
 * <a href= "https://help.aliyun.com/zh/dashscope/developer-reference/qwen-api">...</a>
 *
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashScopeResponseFormat {

	/**
	 * 响应格式类型，必须是 'text' 或 'json_object' 之一
	 */
	@JsonProperty("type")
	private Type type;

	/**
	 * 获取响应格式类型
	 * 
	 * @return 响应格式类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 设置响应格式类型
	 * 
	 * @param type 响应格式类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 默认构造函数
	 */
	public DashScopeResponseFormat() {
	}

	/**
	 * 带参数的构造函数
	 * 
	 * @param type 响应格式类型
	 */
	public DashScopeResponseFormat(Type type) {
		this.type = type;
	}

	/**
	 * 创建构建器实例
	 * 
	 * @return 构建器实例
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * 响应格式构建器类
	 */
	public static class Builder {

		/** 响应格式类型 */
		private Type type;

		/**
		 * 设置响应格式类型
		 * 
		 * @param type 响应格式类型
		 * @return 构建器实例
		 */
		public Builder type(Type type) {
			this.type = type;
			return this;
		}

		/**
		 * 构建响应格式实例
		 * 
		 * @return 响应格式实例
		 */
		public DashScopeResponseFormat build() {
			return new DashScopeResponseFormat(this.type);
		}
	}

	/**
	 * 响应格式类型枚举
	 */
	public enum Type {

		/**
		 * 生成文本响应（默认）
		 */
		@JsonProperty("text")
		TEXT,

		/**
		 * 启用 JSON 模式，保证模型生成的消息是有效的 JSON 字符串
		 */
		@JsonProperty("json_object")
		JSON_OBJECT,
	}

	/**
	 * 转换为字符串表示
	 * 
	 * @return 字符串表示
	 */
	@Override
	public String toString() {
		String typeValue = type == Type.TEXT ? "text" : "json_object";
		return "{\"type\":\"" + typeValue + "\"}";
	}

	/**
	 * 判断对象是否相等
	 * 
	 * @param o 要比较的对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DashScopeResponseFormat that = (DashScopeResponseFormat) o;
		return Objects.equals(type, that.type);
	}

	/**
	 * 计算哈希码
	 * 
	 * @return 哈希码
	 */
	@Override
	public int hashCode() {
		return Objects.hash(type);
	}

}
