package com.eduframework.edroid.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.eduframework.edroid.dto.LectureDTO;
import com.eduframework.edroid.dto.UserDTO;
import com.eduframework.edroid.util.AppConstants;
import com.eduframework.edroid.util.AsyncWorkTask;
import com.eduframework.edroid.util.OnFinishTask;
import com.eduframework.edroid.util.PresentationContentManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class EduFrameworkAPIServiceImpl implements EduFrameworkAPIService {

	private final static String API_V1 = "/api/v1";
	private final static String API_SECURE = "/secure";
	private final static String API_LECTURE = "/chat";

	private final static String LOGIN_URL = "/j_spring_security_check";
	private final static String GET_SECURETOKEN_URL = "/securetoken";
	private final static String GET_LIST_URL = "/list";

	private final static String GET_URL = "/get";
	private final static String GET_PRESENTATION_CONTENT_URL = "/presentation?lectureId=";
	
	private final static String SET_COOKIE_HEADER = "Set-Cookie";
	private final static String COOKIE_HEADER = "Cookie";
	private final static String SPRING_SESSION_NAME = "JSESSIONID";
	private static final String GET_USER_URL = "/user";
	
	private static EduFrameworkAPIService apiServiceInstance = null;
	
	private final AndroidHttpClient httpClient;
	private final String serverAddress;
	private final Gson gson;
	private String springSession;
	private String secureToken;
	private UserDTO currentUser;
	
	public static EduFrameworkAPIService getInstance(String serverAddress) {
		if (apiServiceInstance == null) {
			apiServiceInstance = new EduFrameworkAPIServiceImpl(serverAddress);
		}
		return apiServiceInstance;
	}

	private EduFrameworkAPIServiceImpl(String serverAddress){
		this.serverAddress = serverAddress;
		this.httpClient = AndroidHttpClient.newInstance("Android");
		this.springSession = null;
		this.gson = new Gson();
		this.secureToken = null;
		currentUser = null;
	}
	
	public String getSpringSession() {
		return springSession;
	}

	public Boolean login(String userName, String password, final OnFinishTask onFinish) {
		final HttpPost loginPost = new HttpPost(serverAddress + LOGIN_URL);

        try {

        	List<NameValuePair> arguments = new ArrayList<NameValuePair>(3);
            arguments.add(new BasicNameValuePair("j_username", userName));
            arguments.add(new BasicNameValuePair("j_password", password));
            arguments.add(new BasicNameValuePair("submit", "Login"));

            loginPost.setEntity(new UrlEncodedFormEntity(arguments));
            
            new AsyncWorkTask().execute(new OnFinishTask() {
				
				@Override
				public void onFinish(Object object) {
					HttpResponse response = (HttpResponse) object;
					onFinish.onFinish(checkResponseLoginStatus(response));
					
				}
				
				@Override
				public Object doInBackground() {
					return postHttpRequest(httpClient, loginPost);
				}
			});
        } catch (Exception e) {
        	Log.e("EduFrameworkAPIService", "login error");
        	loginPost.abort();
        	return false;
        }

        return true;
	}
	
	public String getSecureToken() {
		return secureToken;
	}
	
	public Boolean getSecureToken(final OnFinishTask onFinish) {
		final HttpGet getSecureToken = new HttpGet(serverAddress + API_V1 + API_SECURE + GET_SECURETOKEN_URL);
        try {
			
			new AsyncWorkTask().execute(new OnFinishTask() {
				
				@Override
				public void onFinish(Object object) {
					HttpResponse response = (HttpResponse) object;
					if(checkResponseStatus(response)){
						final HttpEntity entity = response.getEntity();
						try {
							final String token = EntityUtils.toString(entity, "UTF-8");
							new AsyncWorkTask().execute(new OnFinishTask() {
								
								@Override
								public void onFinish(Object object) {
									getLoggedUser(token, new OnFinishTask() {
										
										@Override
										public void onFinish(Object object) {
											secureToken = token;
											currentUser = (UserDTO) object;
											onFinish.onFinish(secureToken);
										}
										
										@Override
										public Object doInBackground() {
											return null;
										}
									});
								}
								
								@Override
								public Object doInBackground() { 
									return null;
								}
							});
						} catch (Exception e) {
							onFinish.onFinish(null);
						}							
					}
				}
				
				@Override
				public Object doInBackground() {
					return getHttpRequest(httpClient, getSecureToken);
				}
			});

		} catch (Exception e) {
			getSecureToken.abort();
			return false;
		}

		return true;
	}
	
	public Boolean getAllLectures(final OnFinishTask onFinish) {
		final HttpGet getLecturesList = new HttpGet(serverAddress + API_V1 + API_LECTURE + GET_LIST_URL);

		try {
			
			new AsyncWorkTask().execute(new OnFinishTask() {
				
				@Override
				public void onFinish(Object object) {
					HttpResponse response = (HttpResponse) object;
					if (checkResponseStatus(response)) {
						final HttpEntity entity = response.getEntity();
						try {
							final String lecturesJson = EntityUtils.toString(entity, "UTF-8");
							onFinish.onFinish(gson.fromJson(lecturesJson, new TypeToken<List<LectureDTO>>(){}.getType()));
						} catch (Exception e) {
							onFinish.onFinish(null);
						}
					}
				}
				
				@Override
				public Object doInBackground() {
					return getHttpRequest(httpClient, getLecturesList);
				}
			});


		} catch (Exception e) {
			getLecturesList.abort();
			return false;
		}

		return true;
	}
	
	public Boolean loadPresentationContent(final Integer lectureId, final OnFinishTask onFinished) {
		final HttpGet getPresentationContentRequest = new HttpGet(serverAddress + API_V1 + API_LECTURE + GET_URL + GET_PRESENTATION_CONTENT_URL + lectureId);

		try {
			
			Log.d(AppConstants.DEBUG_TAG_NAME, "Start Load Presentation Content");
			
			new AsyncWorkTask().execute(new OnFinishTask() {
				
				@Override
				public void onFinish(Object object) {
					final HttpResponse response = (HttpResponse) object;
					
					if (checkResponseStatus(response)) {
						
						new AsyncWorkTask().execute(new OnFinishTask() {
							
							@Override
							public void onFinish(Object object) {
								Log.d(AppConstants.DEBUG_TAG_NAME, "End Load Presentation Content");
								onFinished.onFinish(null);
							}
							
							@Override
							public Object doInBackground() {
								final String loadedFileName = PresentationContentManager.PRESENTATION_CONTENT_FILE_NAME + "." + PresentationContentManager.PRESENTATION_CONTENT_LOADED_FORMAT;
								final String loadedDirectoryName = PresentationContentManager.PRESENTATION_LOADED_CONTENT_FOLDER_NAME;
								return PresentationContentManager.loadZipPresentetion(response, loadedFileName, loadedDirectoryName, lectureId, true);	
							}
						});
					}
					
				}
				
				@Override
				public Object doInBackground() {
					return getHttpRequest(httpClient, getPresentationContentRequest);
				}
			});

		} catch (Exception e) {
			getPresentationContentRequest.abort();
		}

		return true;
	}
	
	@Override
	public Boolean getLoggedUser(String token, final OnFinishTask onFinish) {
		final HttpGet getLoggedUser = new HttpGet(serverAddress + API_V1 + API_SECURE + GET_USER_URL + "?token=" + token);
        try {
			
			new AsyncWorkTask().execute(new OnFinishTask() {
				
				@Override
				public void onFinish(Object object) {
					HttpResponse response = (HttpResponse) object;
					if(checkResponseStatus(response)){
						final HttpEntity entity = response.getEntity();
						try {
							String user = EntityUtils.toString(entity, "UTF-8");
							UserDTO userDto = gson.fromJson(user, UserDTO.class);
							onFinish.onFinish(userDto);
						} catch (Exception e) {
							onFinish.onFinish(null);
						}							
					}
				}
				
				@Override
				public Object doInBackground() {
					return getHttpRequest(httpClient, getLoggedUser);
				}
			});

		} catch (Exception e) {
			getLoggedUser.abort();
			return false;
		}

		return true;
	}

	private Boolean checkResponseLoginStatus(HttpResponse response) {
		Header[] header = response.getHeaders(SET_COOKIE_HEADER);
		
		if(header.length > 0 && header[0].getValue().contains(SPRING_SESSION_NAME)){
			String setCookieHeader = header[0].getValue();
			Integer start = setCookieHeader.indexOf(SPRING_SESSION_NAME);
			Integer end = setCookieHeader.indexOf(";");
			if(start >= 0 && end > 0){
				springSession = setCookieHeader.substring(start, end);				
				return true;
			}
		}
		
		springSession = null;
		return false;
    }
	
	private Boolean checkResponseStatus(HttpResponse response) {
        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            return false;
        } else {
            return true;
        }
    }
	
	public UserDTO getCurrentUser() {
		return currentUser;
	}
	
	private HttpResponse getHttpRequest (AndroidHttpClient httpClient, HttpGet get) {
		HttpResponse response = null;
		try {
			get.setHeader(COOKIE_HEADER, springSession);
			response = httpClient.execute(get);
		} catch (IOException e) {
			Log.e("HttpGetTask", "get error");
		}
		return response;
	}
	
	private HttpResponse postHttpRequest (AndroidHttpClient httpClient, HttpPost post) {
		HttpResponse response = null;
		try {
			post.setHeader(COOKIE_HEADER, springSession);
			response = httpClient.execute(post);
		} catch (IOException e) {
			Log.e("HttpGetTask", "get error");
		}
		return response;
	}

}
