package com.blinkedup.kooka;
//https://github.com/fitzroycorpuz/Toucan
import com.blinkedup.kooka.ChatHistoryListFragment.OnShowChatHistoryListener;
import com.toucan.objects.Users;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;


public class ChatHistoryActivity extends Activity implements OnShowChatHistoryListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_history);
		
		Fragment chatHistoryList = ChatHistoryListFragment.newInstance();
		getFragmentManager().beginTransaction()
		.add(R.id.frag_container, chatHistoryList, "ChatHistoryList")
		.commit();
		//RecyclerView
	}

	@Override
	public void onShowChatHistory(Users buddy) {
		Fragment chatHistoryDetailsFragment = ChatHistoryDetailsFragment.newInstance();
		getFragmentManager().beginTransaction()
		.add(R.id.frag_container, chatHistoryDetailsFragment, "ChatHistoryDetails")
		.addToBackStack(null)
		.commit();
	}
}
