package com.eduframework.edroid.ui;

import java.util.ArrayList;
import java.util.List;

import org.atmosphere.wasync.Function;

import com.eduframework.edroid.R;
import com.eduframework.edroid.adapter.MessagesListAdapter;
import com.eduframework.edroid.dto.MessageDTO;
import com.eduframework.edroid.model.Message;
import com.eduframework.edroid.service.EduFrameworkAPIService;
import com.eduframework.edroid.service.EduFrameworkAPIServiceImpl;
import com.eduframework.edroid.service.EduFrameworkChatService;
import com.eduframework.edroid.util.AppConstants;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class ChatFragment extends Fragment {
	
	private Button btnSend;
	private EditText inputMsg;

	// Chat messages list adapter
	private MessagesListAdapter adapter;
	private List<Message> listMessages;
	private ListView listViewMessages;
	
	private EduFrameworkChatService eduService;
	
	private final Handler uiHandler = new Handler();
	
	public ChatFragment(EduFrameworkChatService eduService){
		this.eduService = eduService;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_find_people, container, false);
        
		btnSend = (Button) rootView.findViewById(R.id.btnSend);
		inputMsg = (EditText) rootView.findViewById(R.id.inputMsg);
		listViewMessages = (ListView) rootView.findViewById(R.id.list_view_messages);

		listMessages = new ArrayList<Message>();

		adapter = new MessagesListAdapter(getActivity(), listMessages);
		listViewMessages.setAdapter(adapter);
		
		final EduFrameworkAPIService eduAPIService = EduFrameworkAPIServiceImpl.getInstance(AppConstants.SERVER_ADDRESS);
		
		eduService.connectToMessagesStream(eduAPIService.getSecureToken(), AppConstants.SERVER_ADDRESS,
				new Function<MessageDTO>() {
					public void on(final MessageDTO message) {
						uiHandler.post(new Runnable() {
							public void run() {
								if(message.getIsArray()){
									appendMessages(message.getMessages());
								} else {
									if(message.getOneMessage().getMessageType().equals("chatMessage")){
										appendMessage(message.getOneMessage());
									}
								}
								
							}
						});
					}
				}, 
			
				new Function<String>() {
					public void on(String str) {
					}
				}
		);
		
		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				eduService.sendMessage(new Message(eduAPIService.getCurrentUser().getUserLogin(), inputMsg.getText().toString()));
				inputMsg.setText("");
			}
		});
         
        return rootView;
    }
	
	private void appendMessage(final Message message) {
				listMessages.add(message);
				adapter.notifyDataSetChanged();
	}
	
	private void appendMessages(final List<Message> messages) {
		listMessages.addAll(messages);
		adapter.notifyDataSetChanged();
}
}
