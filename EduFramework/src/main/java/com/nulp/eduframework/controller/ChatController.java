package com.nulp.eduframework.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.nulp.eduframework.domain.LectureChat;
import com.nulp.eduframework.service.LectureChatService;

@Controller
@RequestMapping("/api/v1/chat/")
public class ChatController {

	@Autowired
	private LectureChatService lectureChatService;

	@RequestMapping(value="/create", method = RequestMethod.POST)
	@ResponseBody public String create(@RequestParam(value = "chatName") String chatName) {
		Gson gson = new Gson();
    	LectureChat chat = new LectureChat();
    	
    	chat.setName(chatName);
    	chat.setCurrentStep(0);
    	chat.setStepCount(10);
    	
    	lectureChatService.addLectureChat(chat);
        
    	return gson.toJson(chat);
    }

	@RequestMapping(value="/list", method = RequestMethod.GET)
	@ResponseBody public String list() {
		Gson gson = new Gson();
    	
    	List<LectureChat> lectureChatList = lectureChatService.lectureChatsList();
        
    	return gson.toJson(lectureChatList);
    }
}
