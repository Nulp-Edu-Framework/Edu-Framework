package com.eduframework.edroid.ui;

import java.nio.ByteOrder;

import org.jboss.netty.channel.SucceededChannelFuture;

import com.eduframework.edroid.R;
import com.eduframework.edroid.service.AudioCall;
import com.eduframework.edroid.service.AudioCall.AudioCallStatus;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class AudioFragment extends Fragment {
	
	public static final String rtmpUrl = "rtmp://172.24.224.34:1935/oflaDemo";
	public static final String streamTopic = "presentation";
	
	PowerManager.WakeLock wakeLock;

	private boolean capturing = false;
	private boolean received = false;

	private Button startCaptureButton;
	private Button stopCaptureButton;
	
	private Button startReceivedButton;
	private Button stopReceivedButton;

	private TextView receivedStatusText;

	private AudioCall pablishCall;
	private AudioCall recivedCall;
	
	
	private int bytesReceived;
	private int bytesPerSecond;
	private long bytesReceivedStart;
	
	private Integer presentetionId;
	
	public AudioFragment(Integer presentetionId){
		this.presentetionId = presentetionId;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);
        
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 

		receivedStatusText = (TextView) rootView.findViewById(R.id.receivedStatusText);

		startCaptureButton = (Button) rootView.findViewById(R.id.startCaptureButton);
		startCaptureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!capturing) {
					startCall(rtmpUrl,streamTopic + presentetionId, AudioCallStatus.PUBLISH);
				}
			}
		});
		
		startReceivedButton = (Button) rootView.findViewById(R.id.startReceivedButton);
		startReceivedButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!received) {
					startCall(rtmpUrl,streamTopic + presentetionId, AudioCallStatus.SUBSCRIBE);
				}
			}
		});

		stopReceivedButton = (Button) rootView.findViewById(R.id.stopReceivedButton);
		stopReceivedButton.setEnabled(false);
		stopReceivedButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopCall(AudioCallStatus.SUBSCRIBE);
			}
		});
		
		stopCaptureButton = (Button) rootView.findViewById(R.id.stopCaptureButton);
		stopCaptureButton.setEnabled(false);
		stopCaptureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopCall(AudioCallStatus.PUBLISH);
			}
		});

		PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"streamingWakelock");

		Log.d("---------------------->", "" + ByteOrder.nativeOrder());
		
        return rootView;
    }
	
	public void startCall(String rtmp, String streamTopic, AudioCall.AudioCallStatus status) {

		if(status.equals(AudioCallStatus.SUBSCRIBE)){
			
			if (recivedCall != null) {
				recivedCall.finish();
			}

			recivedCall = new AudioCall(getActivity(), rtmp, streamTopic, streamTopic, status);
			wakeLock.acquire();
			recivedCall.start();
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (recivedCall != null) {
						if (recivedCall.audioSubscriber != null && recivedCall.audioSubscriber.overallBytesReceived != 0) {
							bytesReceived = recivedCall.audioSubscriber.overallBytesReceived;
							if ( bytesReceivedStart == 0 ) {
								bytesReceivedStart = System.currentTimeMillis();
							}
							else {
								bytesPerSecond = bytesReceived / (((int)(System.currentTimeMillis() - bytesReceivedStart)) / 1000);
							}
							getActivity().runOnUiThread(done);
						}
						try {
							Thread.sleep(3000);
						} catch (Exception e) {}
					}
				}

				Runnable done = new Runnable() {
					public void run() {
						if (recivedCall != null && recivedCall.audioSubscriber != null) {
							receivedStatusText
									.setText("Отримано : "
											+ (recivedCall.audioSubscriber.overallBytesReceived / 1024)
											+ "Kb ( " + (bytesPerSecond / 1025)  + "Kb/s )");
						}
					}
				};

			}).start();
			received = true;
			
			startReceivedButton.setEnabled(false);
			stopReceivedButton.setEnabled(true);
		} else if(status.equals(AudioCallStatus.PUBLISH)){
			
			if (pablishCall != null) {
				pablishCall.finish();
			}

			pablishCall = new AudioCall(getActivity(), rtmp, streamTopic, streamTopic, status);
			wakeLock.acquire();
			pablishCall.start();
			capturing = true;
			
			startCaptureButton.setEnabled(false);
			stopCaptureButton.setEnabled(true);
		}

	}

	public void stopCall(AudioCallStatus status) {
		if(status.equals(AudioCallStatus.SUBSCRIBE)){
			startReceivedButton.setEnabled(true);
			stopReceivedButton.setEnabled(false);
			wakeLock.release();
			if (recivedCall != null) {
				recivedCall.finish();
				received = false;
				recivedCall = null;
			}
		} else if (status.equals(AudioCallStatus.PUBLISH)){
			startCaptureButton.setEnabled(true);
			stopCaptureButton.setEnabled(false);
			wakeLock.release();
			if (pablishCall != null) {
				pablishCall.finish();
				capturing = false;
				pablishCall = null;
			}
		}
	}
}
