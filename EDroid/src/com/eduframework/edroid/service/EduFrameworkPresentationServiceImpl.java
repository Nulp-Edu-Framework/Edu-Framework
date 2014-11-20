package com.eduframework.edroid.service;

import java.io.IOException;

import org.atmosphere.wasync.ClientFactory;
import org.atmosphere.wasync.Decoder;
import org.atmosphere.wasync.Encoder;
import org.atmosphere.wasync.Event;
import org.atmosphere.wasync.Function;
import org.atmosphere.wasync.Request;
import org.atmosphere.wasync.RequestBuilder;
import org.atmosphere.wasync.impl.AtmosphereClient;
import org.atmosphere.wasync.impl.AtmosphereRequest.AtmosphereRequestBuilder;

import android.util.Log;

import com.eduframework.edroid.dto.PresentationDTO;
import com.eduframework.edroid.model.Message;
import com.eduframework.edroid.model.PresentationEventMessage;
import com.eduframework.edroid.model.PresentationStatusMessage;
import com.eduframework.edroid.util.AppConstants;
import com.google.gson.Gson;

public class EduFrameworkPresentationServiceImpl implements EduFrameworkPresentationService {

	private final static String ASYNC_API = "/async/api/v1/presentation";
	private final static String LECTURE_ID_PARAMETER = "lectureId";
	
	private Gson gson;
	private AtmosphereClient client;
	private RequestBuilder<AtmosphereRequestBuilder> request;
	private org.atmosphere.wasync.Socket socket;
	private Boolean connectionStatus;
	private Integer lectionId;
	
	public EduFrameworkPresentationServiceImpl(Integer lectionId) {
		this.client = ClientFactory.getDefault().newClient(AtmosphereClient.class);
		this.gson = new Gson();
		this.socket = client.create();
		this.connectionStatus = false;
		this.lectionId = lectionId;
	}
	
	public Boolean connectToPresentationStream(String eduSecureToken, String serverAddress, Function<PresentationDTO> onMessageFunction, Function<String> onErrorFunction) {
		
		String lectureId = lectionId.toString();
		Log.d(AppConstants.DEBUG_TAG_NAME, "Start Connect To Presentation Stream");
		
		if (!connectionStatus) {
			try {
				request = client.newRequestBuilder()
						.method(Request.METHOD.GET)
						.uri(serverAddress + ASYNC_API + "?" + LECTURE_ID_PARAMETER + "=" + lectureId)
						.header("eduSecureToken", eduSecureToken)
						.header("senderName", "test")
						.trackMessageLength(true)
						.encoder(new EduFrameworkEncoder())
						.decoder(new EduFrameworkDecoder())
						.transport(Request.TRANSPORT.WEBSOCKET);

				socket.on(Event.MESSAGE, onMessageFunction)
					  .on(Event.ERROR, onErrorFunction)
					  .open(request.build());
			} catch (IOException e) {
				Log.e("EduFrameworkChatService", "connection error");
				connectionStatus = false;
				return connectionStatus;
			}

			connectionStatus = true;
		}
		
		Log.d(AppConstants.DEBUG_TAG_NAME, "End Connect To Presentation Stream");
		return connectionStatus;
	}

	public Boolean nextSlide() {
		if(connectionStatus){
			try {
				socket.fire(new PresentationDTO(new PresentationEventMessage("next")));
			} catch (IOException e) {
				Log.e("EduFrameworkChatService", "error during sending message");
				return false;
			}
		} else {
			Log.e("EduFrameworkChatService", "service not connected to server");
			return false;
		}
		
		return true;
	}

	public Boolean prevSlide() {
		if(connectionStatus){
			try {
				socket.fire(new PresentationDTO(new PresentationEventMessage("prev")));
			} catch (IOException e) {
				Log.e("EduFrameworkChatService", "error during sending message");
				return false;
			}
		} else {
			Log.e("EduFrameworkChatService", "service not connected to server");
			return false;
		}
		
		return true;
	}

	public Boolean restart() {
		if(connectionStatus){
			try {
				socket.fire(new PresentationEventMessage("restart"));
			} catch (IOException e) {
				Log.e("EduFrameworkChatService", "error during sending message");
				return false;
			}
		} else {
			Log.e("EduFrameworkChatService", "service not connected to server");
			return false;
		}
		
		return true;
	}

	public void leavePresentation() {
		if(connectionStatus && socket != null){
			socket.close();
			connectionStatus = false;
		}
	}
	
	private class EduFrameworkEncoder implements Encoder<PresentationDTO, String> {

		public String encode(PresentationDTO presentationDTO) {
			
			String response = null;
			
			if(presentationDTO.getMessageType().equals(PresentationEventMessage.MESSAGE_TYPE)){
				response = gson.toJson(presentationDTO.getPresentationEventMessage(), PresentationEventMessage.class);
			} else if(presentationDTO.getMessageType().equals(PresentationStatusMessage.MESSAGE_TYPE)){
				response = gson.toJson(presentationDTO.getPresentationStatusMessage(), PresentationStatusMessage.class);
			}
			return response;
		}
		
	}
	
	private class EduFrameworkDecoder implements Decoder<String, PresentationDTO> {

		public PresentationDTO decode(Event event, String data) {
			PresentationDTO message = null;
			data = data.trim();
			
			if (data.length() == 0) {
				return null;
			} 
			
			if (event.equals(Event.MESSAGE)) {
		        Message messageEvent = gson.fromJson(data, Message.class);
		        Log.d(AppConstants.DEBUG_TAG_NAME, "Message from Presentation Stream " + data);
		        if(messageEvent.getMessageType().equals(PresentationStatusMessage.MESSAGE_TYPE)) {
		        	message = new PresentationDTO(gson.fromJson(data, PresentationStatusMessage.class));
		        } else if (messageEvent.getMessageType().equals(PresentationEventMessage.MESSAGE_TYPE)) {
		        	message = new PresentationDTO(gson.fromJson(data, PresentationEventMessage.class));
		        }

		        return message;
			} else {
				return null;
			}
		}
		
	}

	@Override
	public Integer getLectureId() {
		return lectionId;
	}

}
