package com.nulp.eduframework.controller;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListener;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;

import com.google.gson.Gson;
import com.nulp.eduframework.model.EduMessage;
import com.nulp.eduframework.util.Secure;
import com.nulp.eduframework.util.Constants.ConnectionType;

public class AsyncChatEventListener implements AtmosphereResourceEventListener{
	
	private Integer lectureId;
	private BroadcasterFactory factory;
	private ConnectionType connectionType;
	
	public AsyncChatEventListener(Integer lectureId, ConnectionType connectionType, AtmosphereResource atmosphereResource){
		this.lectureId = lectureId;
		this.connectionType = connectionType;
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
		String response =  gson.toJson((new EduMessage(Secure.isAuthorized(atmosphereResource))));
		atmosphereResource.getResponse().write(response);

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
