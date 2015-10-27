package com.blinkedup.kooka;
//https://github.com/fitzroycorpuz/Toucan
import com.blinkedup.kooka.ChatHistoryListFragment.OnShowChatHistoryListener;
import com.toucan.objects.Correspondent;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
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
	public void onShowChatHistory(Correspondent buddy) {
//		Fragment chatHistoryDetailsFragment = ChatHistoryDetailsFragment.newInstance();
//		getFragmentManager().beginTransaction()
//		.add(R.id.frag_container, chatHistoryDetailsFragment, "ChatHistoryDetails")
//		.addToBackStack(null)
//		.commit();
		
		
		Intent intentMes = new Intent(this, ChatActivity.class);
		intentMes.putExtra("email", buddy.getEmail());
		intentMes.putExtra("username", buddy.getUsername());
		intentMes.putExtra("fname", buddy.getFname());
		startActivity(intentMes);
	}
}
