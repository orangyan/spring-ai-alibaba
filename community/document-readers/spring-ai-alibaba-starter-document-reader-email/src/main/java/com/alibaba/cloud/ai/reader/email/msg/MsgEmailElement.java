
package com.alibaba.cloud.ai.reader.email.msg;

import java.util.ArrayList;
import java.util.List;

/**
 * MSG Email Element Class Stores the parsed components of an MSG email message
 *
 * @author xiadong
 * @since 2024-01-19
 */
public class MsgEmailElement {

	private String subject;

	private String from;

	private String fromName;

	private String to;

	private String toName;

	private String date;

	private String contentType;

	private String content;

	private List<MsgEmailElement> attachments;

	public MsgEmailElement() {
		this.attachments = new ArrayList<>();
	}

	// Getters and Setters
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTextType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getText() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<MsgEmailElement> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<MsgEmailElement> attachments) {
		this.attachments = attachments;
	}

	public void addAttachment(MsgEmailElement attachment) {
		this.attachments.add(attachment);
	}

}
