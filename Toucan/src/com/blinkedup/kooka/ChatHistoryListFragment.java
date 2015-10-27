package com.blinkedup.kooka;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.toucan.objects.Correspondent;
import com.toucan.objects.Users;
import com.toucan.sqlite.SQLiteHandler;
import com.toucan.utility.DividerItemDecoration;

public class ChatHistoryListFragment extends Fragment {
	
	private OnShowChatHistoryListener mCallback;
	
	public static ChatHistoryListFragment newInstance() {
		ChatHistoryListFragment fragment = new ChatHistoryListFragment();
		return fragment;
	}

	public ChatHistoryListFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		
		try{
			mCallback = (OnShowChatHistoryListener)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.getClass().getSimpleName()+" must implement OnShowChatHistoryListener interface");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_chat_history_list, container, false);
		
		RecyclerView rvChatHistory = (RecyclerView)v.findViewById(R.id.rv_chat_history);
		rvChatHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
		rvChatHistory.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		List<Correspondent> buddys = new ArrayList<Correspondent>();
		
		
		
		
		buddys.addAll(Correspondent.downloadAllOffline(getActivity()));
		
		
		ChatHistoryAdapter adapter = new ChatHistoryAdapter(getActivity(), buddys, mCallback);
		rvChatHistory.setAdapter(adapter);
		return v;
	}
	
	private class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ViewHolder>{
		
		private LayoutInflater inflater;
		private List<Correspondent> buddys;
		private OnShowChatHistoryListener listener;


		public ChatHistoryAdapter(Context context, List<Correspondent> buddys, OnShowChatHistoryListener listener){
			this.inflater = LayoutInflater.from(context);
			this.buddys = buddys;
			this.listener = listener;
		}
		@Override
		public int getItemCount() {
			
			return this.buddys.size();
		}

		@Override
		public void onBindViewHolder(ViewHolder vh, int position) {
			
			vh.tvBuddys.setText(buddys.get(position).getUsername());
			vh.tvEmail.setText(buddys.get(position).getEmail());
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
			View itemView = inflater.inflate(R.layout.row_chat_history, parent, false);
			return new ViewHolder(itemView, position);
		}
		
		
		class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

			 TextView tvBuddys ;
			 TextView tvEmail;
			private int position;
			 

			public ViewHolder(View view, int position) {
				super(view);
				this.position = position;
				tvBuddys =(TextView)view.findViewById(R.id.tv_buddys_name);
				tvEmail =(TextView)view.findViewById(R.id.tv_buddys_email);
				view.setOnClickListener(this);
			}

			@Override
			public void onClick(View v) {
				
				Correspondent buddy = buddys.get(position);
				listener.onShowChatHistory(buddy);
			}
			
		}
	}
	
	public interface OnShowChatHistoryListener{
		
		public void onShowChatHistory(Correspondent buddy);
	}

}
