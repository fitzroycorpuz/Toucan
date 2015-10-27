package com.toucan.objects;

import java.util.ArrayList;
import java.util.List;

import com.toucan.chatservice.OneComment;
import com.toucan.sqlite.SQLiteHandler;

import android.content.Context;
import android.util.Log;

public class Correspondent {
	
	private long id 						= -1;
	private String username 				= "";
	private String email 					= "";
	private String fname 					= "";
	
	private List<OneComment> conversation 	= new ArrayList<OneComment>();
	
	public Correspondent(long id, String username, String email, String fname){
		
		this.id = id;
		this.username = username;
		this.email = email;
		this.fname = fname;
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}
	
	public void addMessage(OneComment msg){
		
		conversation.add(msg);
	}

	public List<OneComment> getConversation() {
		return conversation;
	}
	
	public void clearExistingConversation() {
		conversation.clear();
		
	}
	
	public void saveOffline(Context context) {
		
		//don't save this session if there is no conversation happen 
		if(conversation.size()== 0 || conversation.isEmpty())return;
		
		
		SQLiteHandler db = new SQLiteHandler(context);
		db.openToRead();
		
		//correspondent does not exist in db
		if(id == -1){
			//save correspondent 
			id = db.saveCorrespondentOffline(username, email, fname);
			
		}
		db.close();
		
		//save messages to db
		Log.e("Correspondent", "id: " + id);
		for(OneComment comment : conversation){
			comment.saveOffline(context, id);
		}
		
		//db.saveMessageOffline(left, comment, success, date);
		
		
		
	}

	public boolean isExisting(Context context) {
		
		SQLiteHandler db = new SQLiteHandler(context);
		db.openToRead();
		id = db.downloadCorrespondentId(username, email, fname);
		db.close();
		
		if(id != -1)return true;
		
		return false;
		
		
	}

	public void downloadOfflineMessages(Context context) {
		
		SQLiteHandler db = new SQLiteHandler(context);
		db.openToRead();
		
		conversation.addAll(db.downloadMessages(id));
		
		db.close();
	}

	public static List<Correspondent> downloadAllOffline(Context context){
		
		SQLiteHandler db = new SQLiteHandler(context);
		db.openToRead();
		List<Correspondent> correspondents = db.downloadAllCorrespondents();
		db.close();
		
		return correspondents;
	}

}
