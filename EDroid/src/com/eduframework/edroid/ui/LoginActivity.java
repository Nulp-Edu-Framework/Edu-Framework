package com.eduframework.edroid.ui;

import com.eduframework.edroid.R;
import com.eduframework.edroid.service.EduFrameworkAPIService;
import com.eduframework.edroid.service.EduFrameworkAPIServiceImpl;
import com.eduframework.edroid.util.AppConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	private EditText userName;
	private EditText password;
	private Button loginButton;
	
	private EduFrameworkAPIService eduAPIService; 
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        userName = (EditText) findViewById(R.id.loginUserName);
        password = (EditText) findViewById(R.id.loginPassword);
        loginButton = (Button) findViewById(R.id.btnLogin);

        eduAPIService = EduFrameworkAPIServiceImpl.getInstance(AppConstants.SERVER_ADDRESS);
        
        loginButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String userLogin = userName.getText().toString();
				String userPassword = password.getText().toString();
				
				Boolean authResult = eduAPIService.login(userLogin, userPassword);
				
				if(authResult) {
					eduAPIService.getSecureToken();
					Intent userActivity = new Intent(getApplicationContext(), UserActivity.class);
					startActivity(userActivity);
				}
			}
			
		});
    }
}
