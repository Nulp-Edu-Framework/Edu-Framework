package com.nulp.eduframework.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nulp.eduframework.model.Message;

@Controller
@RequestMapping("/async/api/v1")
public class AsyncController {
	
    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    @ResponseBody public void onRequest(AtmosphereResource atmosphereResource, HttpSession session) throws IOException {

        AtmosphereRequest atmosphereRequest = atmosphereResource.getRequest();

        if(atmosphereRequest.getHeader("negotiating") == null) {
            atmosphereResource.resumeOnBroadcast(atmosphereResource.transport() == AtmosphereResource.TRANSPORT.LONG_POLLING).suspend();
        } else {
            atmosphereResource.getResponse().getWriter().write("OK");
        }

    }

    @RequestMapping(value = "/chat", method = RequestMethod.POST)
    @ResponseBody public void onPost(AtmosphereResource atmosphereResource) throws IOException{

        AtmosphereRequest atmosphereRequest = atmosphereResource.getRequest();

        String body = atmosphereRequest.getReader().readLine().trim();

        String author = "user" + atmosphereResource.getBroadcaster().getID();        	
        String message = body.substring(body.lastIndexOf(":") + 2, body.length() - 2);
        atmosphereResource.getBroadcaster().broadcast(new Message(author, message).toString());

    }

}
