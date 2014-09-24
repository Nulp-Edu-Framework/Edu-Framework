package com.nulp.eduframework.controller;

import java.io.IOException;

import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.nulp.eduframework.model.EduMessage;
import com.nulp.eduframework.model.Message;
import com.nulp.eduframework.util.Secure;

@Controller
@RequestMapping("/async/api/v1")
public class AsyncController {
	
    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    @ResponseBody public void onConnectToChat(AtmosphereResource atmosphereResource) throws IOException {
    	System.out.println(atmosphereResource.getRequest().getHeader("negotiating"));
        atmosphereResource.resumeOnBroadcast(atmosphereResource.transport() == AtmosphereResource.TRANSPORT.LONG_POLLING).suspend();
    }

    @RequestMapping(value = "/chat", method = RequestMethod.POST)
    @ResponseBody public void onSendMessageToChat(AtmosphereResource atmosphereResource) throws IOException{

    	Gson gson = new Gson();
    	AtmosphereRequest atmosphereRequest = atmosphereResource.getRequest();
    	
    	if(Secure.isAuthorized(atmosphereResource)){
            String body = atmosphereRequest.getReader().readLine().trim();
            String author = "user" + atmosphereResource.getBroadcaster().getID();        	
            String message = body.substring(body.lastIndexOf(":") + 2, body.length() - 2);
            String response =  gson.toJson((new Message(author, message)));
            atmosphereResource.getBroadcaster().broadcast(response);

    	} else {
    		String response =  gson.toJson((new EduMessage(false)));
    		atmosphereResource.getResponse().write(response);
    	}

    }

}
