package com.blinkedup.kooka;

import java.util.ArrayList;
import java.util.Date;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract.Presence;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blinkedup.kooka.R;
import com.toucan.JSON.Profile;
import com.toucan.sqlite.SQLiteHandler;
import com.toucan.utility.NiceDialog;
import com.toucan.utility.RoundedImageView;
import com.toucan.chatservice.ChatAdapterActivity;
import com.toucan.chatservice.OneComment;
import com.toucan.objects.Users;
import com.toucan.openfire.Account;
import com.toucan.openfire.XMPPLogic;

public class GroupChatMainActivity extends Activity {
	Button btnStartChat;
	EditText edBroad;
	MultiUserChat mMultiUserChat;
	String mNickName;
	SQLiteHandler db;
	Message msg;
	String strUser;
	int repeater;

	private XMPPConnection connection;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_home);
        setConnection();
        repeater = 0;
        
        db = new SQLiteHandler(getApplicationContext());
        
        btnStartChat = (Button) findViewById(R.id.btnStartLocChat);
        edBroad = (EditText) findViewById(R.id.txtBroadcast);
        btnStartChat.setOnClickListener(new View.OnClickListener() {
        	
			public void onClick(View view) {
				
				 connection = XMPPLogic.getInstance().getConnection();
				    
			       if (connection == null)  {
			    	   	Account ac = new Account();
			       		SQLiteHandler db = new SQLiteHandler(getApplicationContext());
			       		ac.LogInChatAccount(db.getUsername(), db.getPass(), db.getEmail());
			       		Log.e("Null Chat","ENTERED NULL CHAT");
			       		NiceDialog.promptDialog("Failed to connect to server", "Please try again.", GroupChatMainActivity.this, "error");		
			       }
			       else if (!connection.isConnected()) {
			    	   	Account ac = new Account();
			      		SQLiteHandler db = new SQLiteHandler(getApplicationContext());
			      		ac.LogInChatAccount(db.getUsername(), db.getPass(), db.getEmail());
			      		NiceDialog.promptDialog("Failed to connect to server", "Please try again.", GroupChatMainActivity.this, "error");
			       }
			       else{
			    	   
			    	   ArrayList < Users > us = new ArrayList < Users > ();

			   			us = db.getNearByUserDetails();
			    	    strUser = "";
			    	    
			    	   for (int j = 0; j < us.size(); j++) {
			   			//for (int x = 0;x < us.get(j).getDistance(); x++){
			   			//if (us.get(j).getDistance() >= i){
			    		strUser = us.get(j).getUserName();
			    		msg = new Message(strUser +"@vps.gigapros.com/Smack", Message.Type.chat);
						msg.setBody(edBroad.getText().toString());	
						connection.sendPacket(msg);
						Log.e("XMPPChatDemoActivity", "Sending broadcast to: "+strUser );
						//edBroad.setText("");
			    	   }
					}
				//Intent intentMes = new Intent(GroupChatHomeActivity.this, GroupChatMainActivity.class);
				//intentMes.putExtra("fname", fname);
				//startActivity(intentMes);
			}
		});
       
    }
	public void setConnection() {
		connection = XMPPLogic.getInstance().getConnection();
		if (connection != null) {
			// Add a packet listener to get messages sent to us
			PacketFilter filter = new MessageTypeFilter(Message.Type.normal);
			connection.addPacketListener(new PacketListener() {
				@Override
				public void processPacket(Packet packet) {
					final Message message = (Message) packet;
					if (message.getBody() != null) {
						String fromName = StringUtils.parseBareAddress(message
								.getFrom());
						Log.e("XMPPChatDemoActivity", "Text Recieved " + message.getBody()
								+ " from " + fromName );
					}
				}
			}, filter);
			connection.addPacketSendingListener(new PacketListener() {
				@Override
				public void processPacket(Packet packet) {
					final Message message = (Message) packet;
					if (message.getBody() != null) {
						String fromName = StringUtils.parseBareAddress(message
								.getFrom());
						Log.e("XMPPChatDemoActivity", "Text Sent " + message.getBody()
								+ " from " + fromName );
					}
				}
			}, filter);
			
		}
	}
}
