package com.nulp.eduframework.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.nulp.eduframework.controller.dto.LectureDTO;
import com.nulp.eduframework.domain.LectureChat;
import com.nulp.eduframework.service.LectureChatService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
 
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/api/v1/chat/")
public class ChatController {

	@Autowired
	private LectureChatService lectureChatService;

	@RequestMapping(value="/create", method = RequestMethod.POST)
	@ResponseBody public String create(@RequestParam(value = "chatName") String chatName) {
		Gson gson = new Gson();
    	LectureChat chat = new LectureChat();
    	
    	chat.setName(chatName);
    	chat.setCurrentStep(0);
    	chat.setStepCount(10);
    	
    	lectureChatService.addLectureChat(chat);
        
    	return gson.toJson(chat);
    }

	@RequestMapping(value="/list", method = RequestMethod.GET)
	@ResponseBody public String list() {
		Gson gson = new Gson();
    	
    	List<LectureDTO> lectureChatList = lectureChatService.getLectureDTOList();
        
    	return gson.toJson(lectureChatList);
    }
	
       
	    /**
	     * Path of the file to be downloaded, relative to application's directory
	     */
	    private String filePath = "/presentation.zip";
	
	@RequestMapping(value="/get/presentation", method = RequestMethod.GET)
	@ResponseBody public byte[] doDownload(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
 
        // get absolute path of the application
        ServletContext context = request.getSession().getServletContext();
        String appPath = context.getRealPath("");
        System.out.println("appPath = " + appPath);
 
        // construct the complete absolute path of the file
        String fullPath = appPath + filePath;      
        File downloadFile = new File(fullPath);
        
        byte[] bytes = org.springframework.util.FileCopyUtils.copyToByteArray(downloadFile);
         
        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);
 
        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", downloadFile.getName()));

        return bytes;
 
    }
}
