package com.nulp.eduframework.controller;

import java.util.ArrayList;
import java.util.List;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListener;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nulp.eduframework.domain.LectureChat;
import com.nulp.eduframework.domain.LectureMessage;
import com.nulp.eduframework.model.EduMessage;
import com.nulp.eduframework.model.Message;
import com.nulp.eduframework.model.PresentationStatusMessage;
import com.nulp.eduframework.service.LectureChatService;
import com.nulp.eduframework.service.LectureMessageService;
import com.nulp.eduframework.service.SecureService;
import com.nulp.eduframework.service.SecureServiceImpl;
import com.nulp.eduframework.service.UserService;
import com.nulp.eduframework.util.Constants.ConnectionType;

public class AsyncChatEventListener implements AtmosphereResourceEventListener{

	private LectureMessageService lectureMessageService;
	private LectureChatService lectureChatService;
	private SessionFactory sessionFactory;
	private SecureService secureUtil;
	private Integer lectureId;
	private BroadcasterFactory factory;
	private ConnectionType connectionType;
	
	public AsyncChatEventListener(Integer lectureId, ConnectionType connectionType, AtmosphereResource atmosphereResource,
			SessionFactory sessionFactory, LectureChatService lectureChatService, LectureMessageService lectureMessageService, SecureService secureUtil){
		this.lectureId = lectureId;
		this.connectionType = connectionType;
		this.sessionFactory = sessionFactory;
		this.lectureMessageService = lectureMessageService;
		this.lectureChatService = lectureChatService;
		this.secureUtil = secureUtil;
		this.factory = atmosphereResource.getAtmosphereConfig().getBroadcasterFactory();
	}

	@Override
	public void onHeartbeat(AtmosphereResourceEvent event) {
		System.out.println("onHeartbeat");
	}

	@Override
	public void onPreSuspend(AtmosphereResourceEvent event) {
		System.out.println("onPreSuspend");
	}

	@Override
	public void onSuspend(AtmosphereResourceEvent event) {
		System.out.println("onSuspend");
		Gson gson = new Gson();
		AtmosphereResource atmosphereResource = event.getResource();
		EduMessage responseMessage = new EduMessage(secureUtil.isAuthorized(atmosphereResource));
		String response =  gson.toJson(responseMessage);
		atmosphereResource.getResponse().write(response);
		
		if(connectionType.equals(ConnectionType.LECTURE) && responseMessage.getIsAuthorized()){
			Session session = sessionFactory.openSession();
			
			LectureChat lecture = lectureChatService.getLectureChatById(lectureId, session);
			List<LectureMessage> lectureMessages = lectureMessageService.getAllMessagesByLecture(lecture, session);
			List<Message> messages = new ArrayList<Message>();
			
			for (LectureMessage lectureMessage : lectureMessages) {
				messages.add(lectureMessage.toMessage());
			}
			
			atmosphereResource.getResponse().write(gson.toJson(messages, new TypeToken<List<Message>>(){}.getType()));
			
			session.flush();
			session.close();
		} else if (connectionType.equals(ConnectionType.PRESENTATION) && responseMessage.getIsAuthorized()) {
			Session session = sessionFactory.openSession();
			
			LectureChat lecture = lectureChatService.getLectureChatById(lectureId, session);
			atmosphereResource.getResponse().write(gson.toJson(lecture.getPresentationStatus(), PresentationStatusMessage.class));
			
			session.flush();
			session.close();
		}

        Broadcaster chatChannel = factory.lookup("/" + connectionType.getName() + "_" + lectureId, true);
        System.out.println("CONNECT TO : " + "/" + connectionType.getName() + "_" + lectureId);
        chatChannel.addAtmosphereResource(atmosphereResource);
	}

	@Override
	public void onResume(AtmosphereResourceEvent event) {
		System.out.println("onResume");
	}

	@Override
	public void onDisconnect(AtmosphereResourceEvent event) {
		System.out.println("onDisconnect");
	}

	@Override
	public void onBroadcast(AtmosphereResourceEvent event) {
		System.out.println("onBroadcast");
	}

	@Override
	public void onThrowable(AtmosphereResourceEvent event) {
		System.out.println("onThrowable");
	}

	@Override
	public void onClose(AtmosphereResourceEvent event) {
		System.out.println("onClose");
	}

}
