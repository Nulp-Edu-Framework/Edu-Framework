package com.eduframework.edroid.service;

import java.io.IOException;
import java.util.List;

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

import com.eduframework.edroid.dto.MessageDTO;
import com.eduframework.edroid.model.Message;
import com.eduframework.edroid.util.AppConstants;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class EduFrameworkChatServiceImpl implements EduFrameworkChatService {
	
	private final static String ASYNC_API = "/async/api/v1/chat";
	private final static String LECTURE_ID_PARAMETER = "lectureId";

	private Gson gson;
	private JsonParser jsonParser;
	private AtmosphereClient client;
	private RequestBuilder<AtmosphereRequestBuilder> request;
	private org.atmosphere.wasync.Socket socket;
	private Boolean connectionStatus;
	
	private Integer lectionId;
	
	public EduFrameworkChatServiceImpl(Integer lectionId) {
		this.client = ClientFactory.getDefault().newClient(AtmosphereClient.class);
		this.gson = new Gson();
		this.jsonParser = new JsonParser();
		this.socket = client.create();
		this.connectionStatus = false;
		this.lectionId = lectionId;
	}

	public Boolean connectToMessagesStream(String eduSecureToken, String serverAddress, Function<MessageDTO> onMessageFunction, Function<String> onErrorFunction) {
		String lectureId = lectionId.toString();
		Log.d(AppConstants.DEBUG_TAG_NAME, "Start  connect To Messages Stream");
		if(!connectionStatus){
			try {
				request = client.newRequestBuilder()
						.method(Request.METHOD.GET).uri(serverAddress + ASYNC_API + "?" + LECTURE_ID_PARAMETER + "=" + lectureId)
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
		Log.d(AppConstants.DEBUG_TAG_NAME, "End  connect To Messages Stream");
		return connectionStatus;
	}
	

	public Boolean sendMessage(Message message) {
		if(connectionStatus){
			try {
				socket.fire(message);
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
	
	public void leaveChat() {
		if(connectionStatus && socket != null){
			socket.close();
			connectionStatus = false;
		}
	}
	
	public Integer getLectureId() {
		return lectionId;
	}
	
	private class EduFrameworkEncoder implements Encoder<Message, String> {

		public String encode(Message message) {
			return gson.toJson(message, Message.class);
		}
		
	}
	
	private class EduFrameworkDecoder implements Decoder<String, MessageDTO> {

		public MessageDTO decode(Event event, String data) {
			MessageDTO message = null;
			data = data.trim();
			
			if (data.length() == 0) {
				return null;
			} 
			
			if (event.equals(Event.MESSAGE)) {
		        JsonElement jsonElement = jsonParser.parse(data);
		        Log.d(AppConstants.DEBUG_TAG_NAME, "Message from chat " + data);

		        if (jsonElement.isJsonArray()) {
		            List<Message> messages = gson.fromJson(data, new TypeToken<List<Message>>(){}.getType());
		            message = new MessageDTO(true, messages);
		        } else {
		        	message = new MessageDTO(false,  gson.fromJson(data, Message.class));
		        }
		        
		        return message;
			} else {
				return null;
			}
		}
		
	}

}
