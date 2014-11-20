package com.eduframework.edroid.service;

import org.atmosphere.wasync.Function;

import com.eduframework.edroid.dto.MessageDTO;
import com.eduframework.edroid.model.Message;


public interface EduFrameworkChatService {
	public Boolean connectToMessagesStream (String eduSecureToken, String serverAddress, Function<MessageDTO> onMessageFunction, Function<String> onErrorFunction);
	public Boolean sendMessage (Message message);
	public void leaveChat ();
	public Integer getLectureId();
}
