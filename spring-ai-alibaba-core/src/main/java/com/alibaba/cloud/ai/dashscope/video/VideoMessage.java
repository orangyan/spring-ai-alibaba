

package com.alibaba.cloud.ai.dashscope.video;

import java.util.Objects;

/**
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 */

public class VideoMessage {

	private String text;

	// Add image?

	public VideoMessage(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {

			return true;
		}

		if (!(o instanceof VideoMessage that)) {

			return false;
		}

		return Objects.equals(text, that.text);
	}

	@Override
	public int hashCode() {

		return Objects.hash(text);
	}

}
