package com.eduframework.edroid.ui;

import java.nio.ByteOrder;

import com.eduframework.edroid.R;
import com.eduframework.edroid.service.AudioCall;

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
	
	public static final String rtmpUrl = "rtmp://192.168.1.7:1935/oflaDemo";
	public static final String client1 = "test";
	public static final String client2 = "test";
	
	PowerManager.WakeLock wakeLock;

	private boolean capturing = false;
	private boolean receiving = false;

	private EditText rtmpServerUrlText;
	private EditText publishingTopicText;
	private EditText subscribingTopicText;
	private Button switchTopicButton;

	private Button startCaptureButton;
	private Button stopCaptureButton;

	private TextView receivedStatusText;

	private AudioCall audioCall;
	
	
	private int bytesReceived;
	private int bytesPerSecond;
	private long bytesReceivedStart;
	
	public AudioFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);
        
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 

		rtmpServerUrlText = (EditText) rootView.findViewById(R.id.rtmpServerUrl);
		rtmpServerUrlText.setText(rtmpUrl);

		publishingTopicText = (EditText) rootView.findViewById(R.id.publishingTopic);
		publishingTopicText.setText(client1);

		subscribingTopicText = (EditText) rootView.findViewById(R.id.subscribingTopic);
		subscribingTopicText.setText(client2);

		receivedStatusText = (TextView) rootView.findViewById(R.id.receivedStatusText);

		switchTopicButton = (Button) rootView.findViewById(R.id.switchTopicButton);
		switchTopicButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!capturing && !receiving) {
					Editable text1 = publishingTopicText.getText();
					Editable text2 = subscribingTopicText.getText();
					publishingTopicText.setText(text2);
					subscribingTopicText.setText(text1);
				} else {
					Toast.makeText(
							getActivity().getBaseContext(),
							"stop subscribing and publishing before switching topics",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		startCaptureButton = (Button) rootView.findViewById(R.id.startCaptureButton);
		startCaptureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!capturing) {
					startCall();
				}
			}
		});

		stopCaptureButton = (Button) rootView.findViewById(R.id.stopCaptureButton);
		stopCaptureButton.setEnabled(false);
		stopCaptureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopCall();
			}
		});

		PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"streamingWakelock");

		Log.d("---------------------->", "" + ByteOrder.nativeOrder());
		
        return rootView;
    }
	
	public void startCall() {

		String rtmp = rtmpServerUrlText.getText().toString().trim();
		String pTopic = publishingTopicText.getText().toString().trim();
		String sTopic = subscribingTopicText.getText().toString().trim();

		if (audioCall != null) {
			audioCall.finish();
		}

		audioCall = new AudioCall(getActivity(), rtmp, pTopic, sTopic);
		wakeLock.acquire();
		audioCall.start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (audioCall != null) {
					if (audioCall.audioSubscriber != null && audioCall.audioSubscriber.overallBytesReceived != 0) {
						bytesReceived = audioCall.audioSubscriber.overallBytesReceived;
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
					if (audioCall != null && audioCall.audioSubscriber != null) {
						receivedStatusText
								.setText("received "
										+ (audioCall.audioSubscriber.overallBytesReceived / 1024)
										+ "Kb ( " + (bytesPerSecond / 1025)  + "Kb/s )");
					}
				}
			};

		}).start();

		startCaptureButton.setEnabled(false);
		stopCaptureButton.setEnabled(true);

	}

	public void stopCall() {
		startCaptureButton.setEnabled(true);
		stopCaptureButton.setEnabled(false);
		wakeLock.release();
		if (audioCall != null) {
			audioCall.finish();
			audioCall = null;
		}
	}
}
