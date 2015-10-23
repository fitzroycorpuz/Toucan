package com.toucan.chatservice;

import android.content.Context;

import com.toucan.sqlite.SQLiteHandler;
import com.toucan.utility.DateUtils;
import com.toucan.utility.DateUtils.DateFormatz;

public class OneComment {
	public boolean left;
	public boolean success;
	public String comment;

	public OneComment(boolean left, String comment, boolean success) {
		super();
		this.left = left;
		this.comment = comment;
		this.success = success;
	}
	
	public void saveOffline(Context context, String to, String date){
		
		SQLiteHandler db = new SQLiteHandler(context);
		db.openToWrite();
		String from = db.getEmail();
		db.insertReceivedMessage(from, to, comment, date);
		db.close();
	}

}