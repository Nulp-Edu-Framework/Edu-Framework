package com.eduframework.edroid.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.eduframework.edroid.R;
import com.eduframework.edroid.adapter.LectureAdapter;
import com.eduframework.edroid.dto.LectureDTO;
import com.eduframework.edroid.service.EduFrameworkAPIService;
import com.eduframework.edroid.service.EduFrameworkAPIServiceImpl;
import com.eduframework.edroid.util.AppConstants;
import com.eduframework.edroid.util.OnFinishTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UserActivity extends Activity {	
	private LectureAdapter lectureAdapter;
	
	private EduFrameworkAPIService eduAPIService;
	
	private TextView userFullName;
	private ProgressBar loadingprgBar;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.user);
	        
	        EduFrameworkAPIService apiService =	EduFrameworkAPIServiceImpl.getInstance(AppConstants.SERVER_ADDRESS);
	        
	        loadingprgBar = (ProgressBar) findViewById(R.id.loadingprgBar);
	        userFullName = (TextView) findViewById(R.id.edtUserFullName);
	        userFullName.setText(apiService.getCurrentUser().getUserFirstName() + " " + apiService.getCurrentUser().getUserLastName());
	        
	        List<LectureDTO> lecturesList = new ArrayList<LectureDTO>();
	        lectureAdapter = new LectureAdapter(this, R.layout.listview_lecture_row, lecturesList);
	        
	        ListView lecturesListView = (ListView) findViewById(R.id.lecture_list_view);
	        lecturesListView.setAdapter(lectureAdapter);
	        
	        eduAPIService = EduFrameworkAPIServiceImpl.getInstance(AppConstants.SERVER_ADDRESS);
	        
	        loadingprgBar.setVisibility(View.VISIBLE);
	        eduAPIService.getAllLectures(new OnFinishTask() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void onFinish(Object object) {
					lectureAdapter.addAll((Collection<? extends LectureDTO>) object);
					lectureAdapter.notifyDataSetChanged();
					loadingprgBar.setVisibility(View.GONE);
				}
				
				@Override
				public Object doInBackground() {
					return null;
				}
			});
	        
	        lecturesListView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent lectureActivity = new Intent(getApplicationContext(), MainActivity.class);
					lectureActivity.putExtra("LECTURE_ID", lectureAdapter.getLectureByPostition(position).getId());
					startActivity(lectureActivity);
				}

	        });
	        
	    }
}
