package com.nulp.eduframework.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nulp.eduframework.domain.LectureMessage;
import com.nulp.eduframework.domain.User;
import com.nulp.eduframework.service.LectureChatService;
import com.nulp.eduframework.service.LectureMessageService;
import com.nulp.eduframework.service.UserService;

@Controller
public class ApplicationController {
	
	@Autowired
	private LectureChatService lectureChatService;

    @RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView main() {
    	ModelAndView model = new ModelAndView("main");
        return model;
    }
    
    @RequestMapping(value="/chat", method = RequestMethod.GET)
    public ModelAndView chat(@RequestParam(value = "chatId") Integer chatId, Principal principal ) {
    	ModelAndView model = new ModelAndView("chat");
		model.addObject("chatId", chatId);
		model.addObject("senderName", principal.getName());
        return model;
    }
    
    @RequestMapping(value="/lecture", method = RequestMethod.GET)
    public ModelAndView lecturePanel() {
    	ModelAndView model = new ModelAndView("lecture");
    	model.addObject("lecturesList", lectureChatService.lectureChatsList());
        return model;
    }

}