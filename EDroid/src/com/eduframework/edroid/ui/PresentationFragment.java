package com.eduframework.edroid.ui;

import java.util.List;

import org.atmosphere.wasync.Function;

import com.eduframework.edroid.R;
import com.eduframework.edroid.dto.PresentationDTO;
import com.eduframework.edroid.service.EduFrameworkAPIService;
import com.eduframework.edroid.service.EduFrameworkAPIServiceImpl;
import com.eduframework.edroid.service.EduFrameworkPresentationService;
import com.eduframework.edroid.util.AppConstants;
import com.eduframework.edroid.util.AsyncWorkTask;
import com.eduframework.edroid.util.OnFinishTask;
import com.eduframework.edroid.util.PresentationContentManager;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class PresentationFragment extends Fragment {
	
	private final Handler uiHandler = new Handler();
	
	private ImageView presentationScreen;
	private ProgressBar loadingContentprgBar;
	private Button next;
	private Button prev;
	
	private EduFrameworkPresentationService eduPresentationService;
	
	private Boolean ifPresentationViewVisible;
	
	public PresentationFragment(EduFrameworkPresentationService eduPresentationService){
		this.eduPresentationService = eduPresentationService;
		this.ifPresentationViewVisible = false;
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_presentation, container, false);
        
        presentationScreen = (ImageView) rootView.findViewById(R.id.presentationScreen);
        loadingContentprgBar = (ProgressBar) rootView.findViewById(R.id.loadingContentprgBar);
        next = (Button) rootView.findViewById(R.id.button1);
        prev = (Button) rootView.findViewById(R.id.button2);
               
		final EduFrameworkAPIService eduAPIService = EduFrameworkAPIServiceImpl.getInstance(AppConstants.SERVER_ADDRESS);
		eduAPIService.loadPresentationContent(eduPresentationService.getLectureId(), new OnFinishTask() {
			
			@Override
			public void onFinish(Object object) {
				new AsyncWorkTask().execute(new OnFinishTask() {
					
					@SuppressWarnings("unchecked")
					public void onFinish(Object object) {
						final List<Bitmap> presentationImages = (List<Bitmap>) object;
						
						eduPresentationService.connectToPresentationStream(eduAPIService.getSecureToken(), AppConstants.SERVER_ADDRESS,
								new Function<PresentationDTO>() {
									public void on(final PresentationDTO message) {
										uiHandler.post(new Runnable() {
											public void run() {
												if(message != null){
													if(presentationImages != null) {
														Log.d("PRESENTATION : " ,"step : " + message.getPresentationStatusMessage().getCurrentStep());
														presentationScreen.setImageBitmap(presentationImages.get(message.getPresentationStatusMessage().getCurrentStep()));
														showPresentationView();
													}
												}
											}
										});
									}
								}, 
							
								new Function<String>() {
									public void on(String str) {}
								}
						);
					}
					
					@Override
					public Object doInBackground() {
						final List<Bitmap> presentationImages = PresentationContentManager.getAllImagesForPresentation(eduPresentationService.getLectureId());
						return presentationImages;
					}
				});
			}
			
			@Override
			public Object doInBackground() {
				return null;
			}
		});
		               
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				eduPresentationService.nextSlide();
			}
		});
		
		prev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				eduPresentationService.prevSlide();
			}
		});
		
        return rootView;
    }
	
	private void showPresentationView(){
		if(loadingContentprgBar != null && presentationScreen != null && !ifPresentationViewVisible) {
			loadingContentprgBar.setVisibility(View.GONE);
			presentationScreen.setVisibility(View.VISIBLE);
			ifPresentationViewVisible = true;
		}
	}

}
