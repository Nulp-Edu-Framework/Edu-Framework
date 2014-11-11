package com.nulp.eduframework.controller;

import java.io.IOException;

import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.nulp.eduframework.domain.LectureChat;
import com.nulp.eduframework.domain.LectureMessage;
import com.nulp.eduframework.domain.User;
import com.nulp.eduframework.model.EduMessage;
import com.nulp.eduframework.model.Message;
import com.nulp.eduframework.model.PresentationEventMessage;
import com.nulp.eduframework.model.PresentationStatusMessage;
import com.nulp.eduframework.service.LectureChatService;
import com.nulp.eduframework.service.LectureMessageService;
import com.nulp.eduframework.service.UserService;
import com.nulp.eduframework.util.Constants.PresentationDirection;
import com.nulp.eduframework.util.Secure;
import com.nulp.eduframework.util.Constants.ConnectionType;

@Controller
@RequestMapping("/async/api/v1")
public class AsyncController {

	@Autowired
	private LectureMessageService lectureMessageService;
	
	@Autowired
	private LectureChatService lectureChatService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SessionFactory sessionFactory;
	
    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    @ResponseBody public void onConnectToChat(@RequestParam(value = "lectureId") Integer lectureId, AtmosphereResource atmosphereResource) throws IOException {
    	AsyncChatEventListener chatEventListener = new AsyncChatEventListener(lectureId, ConnectionType.LECTURE, atmosphereResource, sessionFactory, lectureChatService, lectureMessageService);
    	atmosphereResource.addEventListener(chatEventListener);
        atmosphereResource.resumeOnBroadcast(atmosphereResource.transport() == AtmosphereResource.TRANSPORT.LONG_POLLING).suspend();
    }

    @RequestMapping(value = "/chat", method = RequestMethod.POST)
    @ResponseBody public void onSendMessageToChat(@RequestParam(value = "lectureId") Integer lectureId, AtmosphereResource atmosphereResource) throws IOException{
    	Session session = sessionFactory.openSession();
    	BroadcasterFactory factory = atmosphereResource.getAtmosphereConfig().getBroadcasterFactory();
    	Broadcaster chatChannel = factory.lookup("/" + ConnectionType.LECTURE.getName() + "_" + lectureId, true);
    	
    	System.out.println("SENT TO : " + "/" + ConnectionType.LECTURE + "_" + lectureId);

    	Gson gson = new Gson();
    	AtmosphereRequest atmosphereRequest = atmosphereResource.getRequest();
    	
    	if(Secure.isAuthorized(atmosphereResource)){
            String body = atmosphereRequest.getReader().readLine().trim();
            Message message = gson.fromJson(body, Message.class);
            saveMessage(lectureId, message, session);
            String response =  gson.toJson(message);
            chatChannel.broadcast(response);
    	} else {
    		String response =  gson.toJson((new EduMessage(false)));
    		atmosphereResource.getResponse().write(response);
    	}
    	
    	session.flush();
    	session.close();
    }
    
    @RequestMapping(value = "/presentation", method = RequestMethod.GET)
    @ResponseBody public void onConnectToPresentation(@RequestParam(value = "lectureId") Integer lectureId, AtmosphereResource atmosphereResource) throws IOException {
    	AsyncChatEventListener chatEventListener = new AsyncChatEventListener(lectureId, ConnectionType.PRESENTATION, atmosphereResource, sessionFactory, lectureChatService, lectureMessageService);
    	atmosphereResource.addEventListener(chatEventListener);
    	atmosphereResource.resumeOnBroadcast(atmosphereResource.transport() == AtmosphereResource.TRANSPORT.LONG_POLLING).suspend();
    }

    @RequestMapping(value = "/presentation", method = RequestMethod.POST)
    @ResponseBody public void onPresentationEvent(@RequestParam(value = "lectureId") Integer lectureId, AtmosphereResource atmosphereResource) throws IOException{
    	Session session = sessionFactory.openSession();
    	BroadcasterFactory factory = atmosphereResource.getAtmosphereConfig().getBroadcasterFactory();
    	Broadcaster chatChannel = factory.lookup("/" + ConnectionType.PRESENTATION.getName() + "_" + lectureId, true);

    	Gson gson = new Gson();
    	String response = null;
    	AtmosphereRequest atmosphereRequest = atmosphereResource.getRequest();
    	
    	if(Secure.isAuthorized(atmosphereResource)){
    		
    		LectureChat lecture = lectureChatService.getLectureChatById(lectureId, session);
    		Integer currentStep = lecture.getCurrentStep();
    		
    		String body = atmosphereRequest.getReader().readLine().trim();
    		PresentationEventMessage preEventMessage = gson.fromJson(body, PresentationEventMessage.class);
    		
    		if(preEventMessage.getPreDirection().equals(PresentationDirection.NEXT.getName())){
    			Integer nextStep = lecture.nextStep();
    			lecture.setCurrentStep(nextStep > 0 ? nextStep : currentStep);
    		} else if (preEventMessage.getPreDirection().equals(PresentationDirection.PREV.getName())){
    			Integer prevStep = lecture.prevStep();
    			lecture.setCurrentStep(prevStep >= 0 ? prevStep : currentStep);
    		} else if (preEventMessage.getPreDirection().equals(PresentationDirection.RESTART.getName())){
    			lecture.setCurrentStep(0);
    		}

    		lectureChatService.addLectureChat(lecture, session);
    		System.out.println("currentStep " + lecture.getCurrentStep());
			response =  gson.toJson((new PresentationStatusMessage(lecture.getCurrentStep(), lecture.getStepCount())));
    		chatChannel.broadcast(response);
    	} else {
    		response =  gson.toJson((new EduMessage(false)));
    		atmosphereResource.getResponse().write(response);
    	}

    	session.flush();
    	session.close();
    }
    
	private LectureMessage saveMessage(Integer lectureId, Message message, Session session) {
		LectureChat lecture = null;
		User user = null;
		LectureMessage lectureMessage = null;

		if (lectureId != null || message.getAuthor() != null || session != null) {
			lecture = lectureChatService.getLectureChatById(lectureId, session);
			user = userService.getUserByUSerName(message.getAuthor(), session);
			if (lecture != null || user != null) {
				lectureMessage = new LectureMessage(message.getText());
				lectureMessage.setLecture(lecture);
				lectureMessage.setUser(user);
				lectureMessageService.addLectureMessage(lectureMessage, session);
			}
		}
		
		return lectureMessage;
	}
}
