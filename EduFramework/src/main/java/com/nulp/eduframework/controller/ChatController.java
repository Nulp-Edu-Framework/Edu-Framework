package com.nulp.eduframework.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.nulp.eduframework.controller.dto.LectureDTO;
import com.nulp.eduframework.domain.LectureChat;
import com.nulp.eduframework.service.LectureChatService;
import com.nulp.eduframework.util.PresentationConverter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
 


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/api/v1/chat/")
public class ChatController {

	public static final String CONTENT_FOLDER_PATH = "/lectures";
	
	@Autowired
	private LectureChatService lectureChatService;

	@RequestMapping(value="/list", method = RequestMethod.GET)
	@ResponseBody public String list() {
		Gson gson = new Gson();
    	
    	List<LectureDTO> lectureChatList = lectureChatService.getLectureDTOList();
        
    	return gson.toJson(lectureChatList);
    }
	
    
	@RequestMapping(value="/get/presentation", method = RequestMethod.GET)
	@ResponseBody public byte[] doDownload(@RequestParam("lectureId") String lectureId, HttpServletRequest request, HttpServletResponse response) throws IOException {
 
        // get absolute path of the application
        ServletContext context = request.getSession().getServletContext();
        String appPath = context.getRealPath("");
        System.out.println("appPath = " + appPath);
 
        // construct the complete absolute path of the file
        String fullPath = appPath + CONTENT_FOLDER_PATH + File.separator + lectureId + "/lecture.zip";      
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
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public ModelAndView uploadFileHandler(
			HttpServletRequest request, 
			@RequestParam(required = false) String source,
			@RequestParam(required = false) Boolean edit, 
			@RequestParam(required = false) Integer id, 
			@RequestParam MultipartFile file, 
			@RequestParam String chatName) {

		Boolean isCreated = false;
		
		Integer lectureId = null;
		LectureChat chat = null;
		
		if(edit!=null && edit && id!=null){
			lectureId = id;
			chat = lectureChatService.getLectureChatById(lectureId);
			chat.setName(chatName);
			if (file.isEmpty()) {
				lectureChatService.addLectureChat(chat);
				isCreated = true;
			}
		} else if (!file.isEmpty()) {
			lectureId = createLecture(chatName);
			chat = lectureChatService.getLectureChatById(lectureId);
		}

		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				ServletContext context = request.getSession().getServletContext();
				String rootPath = context.getRealPath("");
				File dir = new File(rootPath + File.separator + "lectures");
				
				if (!dir.exists()) {
					dir.mkdirs();
				}

				if(lectureId != null && chat != null) {
				
					// Create the file on server
					String dirPath =  dir.getAbsolutePath() + File.separator + lectureId;

					File lectureDir = new File(dirPath);
					lectureDir.delete();
					lectureDir.mkdir();
					
					String uploadedFileName = dirPath + File.separator + file.getOriginalFilename();
					System.out.println("Uploaded : " + uploadedFileName);
					File serverFile = new File(uploadedFileName);
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					
					stream.write(bytes);
					stream.close();
					
					System.out.println("START FILE CONVERSION : " + dirPath);
					Integer slidesCount = PresentationConverter.convertPresentationToImages(uploadedFileName, dirPath);
					
					System.out.println("READED : " + slidesCount);

					chat.setStepCount(slidesCount);
					lectureChatService.addLectureChat(chat);
				
				}
				
				isCreated = true;
			} catch (Exception e) {
				isCreated = false;
			}
		}
		
		ModelAndView model = new ModelAndView("redirect:/lecture");
		
		if(source != null && source.equals("admin")){
			if(edit!=null && edit){
				model = new ModelAndView("redirect:/editLection?lectionId="+id);
			} else {
				model = new ModelAndView("redirect:/createLection");	
			}

		} else {
			model = new ModelAndView("redirect:/lecture");
		}

		model.addObject("isCreated", isCreated);
		return model;
	}
	
	private Integer createLecture ( String lectureName) {
    	LectureChat chat = new LectureChat();
    	
    	chat.setName(lectureName);
    	chat.setCurrentStep(0);
    	chat.setStepCount(0);
    	
    	lectureChatService.addLectureChat(chat);
    	        
    	return chat.getId();
    }
	
}
