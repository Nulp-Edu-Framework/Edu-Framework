package com.nulp.eduframework.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nulp.eduframework.domain.LectureChat;
import com.nulp.eduframework.domain.User;
import com.nulp.eduframework.domain.UserDetails;
import com.nulp.eduframework.domain.UserRole;
import com.nulp.eduframework.service.LectureChatService;
import com.nulp.eduframework.service.UserService;

@Controller
public class ApplicationController {
	
	@Autowired
	private LectureChatService lectureChatService;
	
	@Autowired
	private UserService userService;

    @RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView main() {
    	ModelAndView model = new ModelAndView("main");
        return model;
    }
    	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error) {
 
		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Ви  ввели  неправильні  дані  користувача");
		}
 
		model.setViewName("login");
 
		return model;
 
	}
	
    @RequestMapping(value="/chat", method = RequestMethod.GET)
    public ModelAndView chatPanel(@RequestParam(value = "chatId") Integer chatId, Principal principal ) {
    	ModelAndView model = new ModelAndView("chat");
		model.addObject("chatId", chatId);
		model.addObject("senderName", principal.getName());
		model.addObject("secureToken", userService.getUserByUSerName(principal.getName()).getSecureToken());
        return model;
    }
    
    @RequestMapping(value="/lecture", method = RequestMethod.GET)
    public ModelAndView lecturePanel(@RequestParam (required = false) Boolean isCreated) {
    	ModelAndView model = new ModelAndView("lecture");
    	
    	if(isCreated != null) {
    		model.addObject("isCreated", isCreated);
    	}

    	model.addObject("lecturesList", lectureChatService.lectureChatsList());
        return model;
    }

    @RequestMapping(value="/admin", method = RequestMethod.GET)
    public ModelAndView adminPanel(@RequestParam (required = false) Boolean isCreated) {
    	ModelAndView model = new ModelAndView("admin");
    	
    	if(isCreated != null){
    		model.addObject("isCreated",isCreated);
    	}
    	
    	model.addObject("usersList", userService.getUsersList());
    	model.addObject("lecturesList", lectureChatService.lectureChatsList());
        return model;
    }
    
    @RequestMapping(value="/createLection", method = RequestMethod.GET)
    public ModelAndView createLectionPanel(@RequestParam (required = false) Boolean isCreated) {
    	
    	ModelAndView model = new ModelAndView("edit");
    	
    	model.addObject("edit", "lection");
    	model.addObject("isNew", true);
    	
    	if(isCreated != null){
        	model.addObject("isSuccess",isCreated);
    	}
    	
        return model;
    }
    
    @RequestMapping(value="/editLection", method = RequestMethod.GET)
    public ModelAndView editLectionPanel(@RequestParam Integer lectionId, @RequestParam (required = false) Boolean isCreated) {
    	
    	ModelAndView model = new ModelAndView("edit");
    	
    	LectureChat lecture = lectureChatService.getLectureChatById(lectionId);
    	
    	model.addObject("edit", "lection");

    	if(lecture != null){
    		model.addObject("lection", lecture);    		
    	}
    	
    	if(isCreated != null){
        	model.addObject("isSuccess",isCreated);
    	}
    	
        return model;
    }
    
    @RequestMapping(value="/deleteLecture", method = RequestMethod.GET)
    public ModelAndView deleteLecture(HttpServletRequest request, @RequestParam Integer id) throws IOException {
    	
    	ModelAndView model = new ModelAndView("redirect:/admin?isCreated=true");
    	
    	LectureChat lecture = lectureChatService.getLectureChatById(id);
    		
    	if(lecture != null) {
    		lectureChatService.deleteLecture(lecture);
            ServletContext context = request.getSession().getServletContext();
            String appPath = context.getRealPath("");
            String lectureFolderPath = appPath + ChatController.CONTENT_FOLDER_PATH + File.separator + id + File.separator;
            System.out.println("delete : " + lectureFolderPath);
            FileUtils.deleteDirectory(new File(lectureFolderPath));
    	}
    		
        return model;
    }
    
    @RequestMapping(value="/createUser", method = RequestMethod.GET)
    public ModelAndView createUserPanel() {
    	
    	ModelAndView model = new ModelAndView("edit");
    	
    	model.addObject("edit", "user");
    	model.addObject("isNew", true);
    		
        return model;
    }
    
    @RequestMapping(value="/editUser", method = RequestMethod.GET)
    public ModelAndView editPanel(@RequestParam String username) {
    	
    	ModelAndView model = new ModelAndView("edit");
    	
    	User user = userService.getUserByUSerName(username);
    		
    	if(user != null) {
    		model.addObject("edit", "user");
    		model.addObject("user", user);
    	}
    		
        return model;
    }
    
    @RequestMapping(value="/deleteUser", method = RequestMethod.GET)
    public ModelAndView deleteUser(@RequestParam String username) {
    	
    	ModelAndView model = new ModelAndView("redirect:/admin?isCreated=true");
    	
    	User user = userService.getUserByUSerName(username);
    		
    	if(user != null) {
    		userService.deleteUser(user);
    	}
    		
        return model;
    }
    
    
    @RequestMapping(value="/createUser", method = RequestMethod.POST)
    public ModelAndView createAction(
    		@RequestParam String login,
    		@RequestParam String password,
    		@RequestParam String firstname,
    		@RequestParam String lastname,
    		@RequestParam String userRole) {
    	
    	ModelAndView model = new ModelAndView("edit");
    	Boolean isCuccess = false;
    	
    	User user = new User();    		
    		if(firstname.isEmpty() || lastname.isEmpty() || userRole.isEmpty()){
    			isCuccess = false;
    		} else {
        		UserDetails details = new UserDetails();
            	UserRole role = new UserRole();
            	            	
            	details.setFirstname(firstname);
            	details.setLastname(lastname);
            	details.setUser(user);
            	
            	role.setRole(userRole);
            	role.setUsername(login);
            	role.setUser(user);
            	
            	user.setUsername(login);
            	user.setPassword(password);
            	user.setUserDetails(details);
            	user.setUserRole(role);
            	user.setUserDetails(details);
            	user.setUserRole(role);
            	
            	try {
            		userService.addUser(user);
            		model.addObject("edit", "user");
                	model.addObject("user", user);
            		isCuccess = true;
            	} catch (Exception e) {
            		e.printStackTrace();
            		isCuccess = false;
            	}
    		}
  
    	if(!isCuccess) {
        	model.addObject("edit", "user");
        	model.addObject("isNew", true);
    	}
    	model.addObject("isSuccess", isCuccess);

        return model;
    }
    
    @RequestMapping(value="/editUser", method = RequestMethod.POST)
    public ModelAndView editAction(
    		@RequestParam String login,
    		@RequestParam String firstname, @RequestParam String lastname,
    		@RequestParam String secureToken, @RequestParam String userRole) {
    	
    	ModelAndView model = new ModelAndView("edit");
    	Boolean isCuccess = false;
    	
    	User user = userService.getUserByUSerName(login);
    		
    	if(user != null) {
    		
    		if(firstname.isEmpty() || lastname.isEmpty() || userRole.isEmpty()){
    			isCuccess = false;
    		} else {
        		UserDetails details = user.getUserDetails();
            	UserRole role = user.getUserRole();
            	
            	details.setFirstname(firstname);
            	details.setLastname(lastname);
            	details.setUser(user);
            	
            	role.setRole(userRole);
            	role.setUsername(login);
            	role.setUser(user);
            	
            	user.setUserDetails(details);
            	user.setUserRole(role);
            	
            	try {
                	userService.addUser(user);
                	isCuccess = true;
                } catch (Exception e) {
            		isCuccess = false;
            	}


    		}
    		
    		model.addObject("edit", "user");
        	model.addObject("user", user);

    	}
    	
    	model.addObject("isSuccess", isCuccess);

        return model;
    }

}