package com.toucan.chatservice;

import android.content.Context;

import com.toucan.sqlite.SQLiteHandler;
import com.toucan.utility.DateUtils;
import com.toucan.utility.DateUtils.DateFormatz;

public class OneComment {
	public boolean left;
	public boolean success;
	public String comment;
	public String date;

	public OneComment(boolean left, String comment, boolean success) {
		super();
		this.left = left;
		this.comment = comment;
		this.success = success;
	}
	
	public OneComment(boolean left, String comment, boolean success, String date) {
		super();
		this.left = left;
		this.comment = comment;
		this.success = success;
		this.date = date;
	}
	
	public void saveOffline(Context context, long correspondentId){
		
		SQLiteHandler db = new SQLiteHandler(context);
		db.openToWrite();
		db.saveMessageOffline(correspondentId, left, comment,success, date);
		db.close();
		
	}

}