
package com.alibaba.cloud.ai.controller;

import com.alibaba.cloud.ai.api.ChatClientAPI;
import com.alibaba.cloud.ai.service.ChatClientDelegate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("studio/api/chat-clients")
public class ChatClientAPIController implements ChatClientAPI {

	private final ChatClientDelegate delegate;

	public ChatClientAPIController(@Autowired(required = false) ChatClientDelegate delegate) {
		this.delegate = Optional.ofNullable(delegate).orElse(new ChatClientDelegate() {
		});
	}

	@Override
	public ChatClientDelegate getDelegate() {
		return delegate;
	}

}
