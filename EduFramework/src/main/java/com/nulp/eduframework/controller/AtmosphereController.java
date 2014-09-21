package com.nulp.eduframework.controller;

import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nulp.eduframework.model.Message;

import javax.servlet.http.HttpSession;

import java.io.IOException;

@Controller
public class AtmosphereController {

    @RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView getView() {
        return new ModelAndView("chat");
    }

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

    	UserDetails userDetail = null;
    	String author = null;
    	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			userDetail = (UserDetails) auth.getPrincipal();
		}
  	  
        AtmosphereRequest atmosphereRequest = atmosphereResource.getRequest();

        String body = atmosphereRequest.getReader().readLine().trim();

        if(userDetail != null){
        	author = userDetail.getUsername();
        } else {
        	author = "user" + atmosphereResource.getBroadcaster().getID();        	
        }
        
        String message = body.substring(body.lastIndexOf(":") + 2, body.length() - 2);
        atmosphereResource.getBroadcaster().broadcast(new Message(author, message).toString());

    }

}