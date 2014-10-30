package com.nulp.eduframework.controller;

import java.io.IOException;

import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.nulp.eduframework.model.EduMessage;
import com.nulp.eduframework.model.Message;
import com.nulp.eduframework.util.Constants;
import com.nulp.eduframework.util.Secure;

@Controller
@RequestMapping("/async/api/v1")
public class AsyncController {
		
    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    @ResponseBody public void onConnectToChat(@RequestParam(value = "chatId") Integer chatId, AtmosphereResource atmosphereResource) throws IOException {
    	System.out.println("connected to : " + chatId);
    	atmosphereResource.addEventListener(new AsyncChatEventListener(chatId,atmosphereResource));
        atmosphereResource.resumeOnBroadcast(atmosphereResource.transport() == AtmosphereResource.TRANSPORT.LONG_POLLING).suspend();
    }

    @RequestMapping(value = "/chat", method = RequestMethod.POST)
    @ResponseBody public void onSendMessageToChat(@RequestParam(value = "chatId") Integer chatId, AtmosphereResource atmosphereResource) throws IOException{
    	System.out.println("post on : " + chatId);
    	BroadcasterFactory factory = atmosphereResource.getAtmosphereConfig().getBroadcasterFactory();
    	Broadcaster chatChannel = factory.lookup("/chat_"+chatId, true);

    	Gson gson = new Gson();
    	AtmosphereRequest atmosphereRequest = atmosphereResource.getRequest();
    	
    	if(Secure.isAuthorized(atmosphereResource)){
            String body = atmosphereRequest.getReader().readLine().trim();
            String author = atmosphereResource.getRequest().getHeader("senderName");        	
            String message = body.substring(body.lastIndexOf(":") + 2, body.length() - 2);
            String response =  gson.toJson((new Message(author, message)));
            chatChannel.broadcast(response);

    	} else {
    		String response =  gson.toJson((new EduMessage(false)));
    		atmosphereResource.getResponse().write(response);
    	}

    }

}
