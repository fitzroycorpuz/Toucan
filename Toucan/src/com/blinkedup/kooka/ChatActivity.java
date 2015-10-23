package com.blinkedup.kooka;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import com.blinkedup.kooka.R;
import com.toucan.chatservice.ChatAdapterActivity;
import com.toucan.chatservice.OneComment;
import com.toucan.openfire.Account;
import com.toucan.openfire.XMPPLogic;
import com.toucan.sqlite.SQLiteHandler;
import com.toucan.utility.DateUtils;
import com.toucan.utility.NiceDialog;
import com.toucan.utility.DateUtils.DateFormatz;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ChatActivity extends Activity {
	private com.toucan.chatservice.ChatAdapterActivity adapter;

	public boolean isMine;
	private XMPPConnection connection;
	private Handler mHandler = new Handler();

	private EditText recipient;
	private EditText textMessage;
	private ListView listview;
	
	String int_mes;
	String int_broad;
	String int_b_date;
	Intent intentMes;
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; goto parent activity.
	            this.finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layoutmain);

		if (android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.HONEYCOMB) {
			 ActionBar actionBar = getActionBar();
			 actionBar.setHomeButtonEnabled(true);
			 actionBar.setDisplayHomeAsUpEnabled(true);
		}
		else{
			 Log.e("NOTICE","Device cannot handle ActionBar");
		}
		
		
		setConnection();
		recipient = (EditText) this.findViewById(R.id.toET);
		textMessage = (EditText) this.findViewById(R.id.chatET);
		listview = (ListView) this.findViewById(R.id.listMessages);
		
		adapter = new ChatAdapterActivity(getApplicationContext(), R.layout.listitem);
		listview.setAdapter(adapter);
	
		intentMes = getIntent(); 
		//String email = intentMes.getStringExtra("email");
		final String username = intentMes.getStringExtra("username");
		int_mes = "";
		int_broad = "";
		int_b_date = "";
		
		try{
			 int_mes = intentMes.getStringExtra("INTENT_MESSAGE");
			 int_broad = intentMes.getStringExtra("INTENT_MESSAGE_TYPE");
			 int_b_date = intentMes.getStringExtra("INTENT_MESSAGE_DATE");
		}
		catch(Exception e){}
		
		
      //  String fname = intentMes.getStringExtra("fname");
        
        recipient.setText(username + "@vps.gigapros.com/Smack", TextView.BufferType.EDITABLE);
        recipient.setVisibility(0);
//Smack
        if (int_broad != null && int_broad.equals("BROADCAST")){
			int_broad = "";
			textMessage.setText("In reply to: '"+int_mes+"',\nPosted "+int_b_date+"\n\n");
			textMessage.setSelection(textMessage.getText().length());
        }
        else{
        	textMessage.setText("");
        }
		// Set a listener to send a chat text message
		Button send = (Button) this.findViewById(R.id.sendBtn);
		send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				//String to = username + "@vps.gigapros.com/Smack";
				final String to = recipient.getText().toString();
				final String text = textMessage.getText().toString();
				textMessage.setText("");
				
				

				Log.e("XMPPChatDemoActivity", "Sending text " + text + " to " + to);
				Message msg = new Message(to, Message.Type.chat);
				msg.setBody(text);	
				
					connection = XMPPLogic.getInstance().getConnection();
					 if ((connection == null)||(!connection.isConnected()))  {	
				       		Account ac = new Account();
				       		
				        	SQLiteHandler db = new SQLiteHandler(getApplicationContext());
				    		db.openToWrite();
				    		
				            ac.LogInChatAccount(db.getUsername(), db.getPass(), db.getEmail());
				            
				            Log.e("ADDING AFTER LOGIN","g");
				            
				           // connection.sendPacket(msg);
							//messages.add(connection.getUser() + ":");
							adapter.add(new OneComment(true, text,false));
							listview.setAdapter(adapter);
							
							db.close();
							
				       }
				      
				       else{
				    		connection.sendPacket(msg);
							//messages.add(connection.getUser() + ":");
				    		OneComment comment = new OneComment(true, text, true);
							adapter.add(comment);
							
							listview.setAdapter(adapter);
							Log.e("ADD","ayayay");
							
							//save newly created message to db
							comment.saveOffline(
									getApplicationContext(), 
									StringUtils.parseBareAddress(to), 
									DateUtils.millisToSimpleDate(
											System.currentTimeMillis(), 
											DateFormatz.DATE_FORMAT_5));
							
				       }
			}
		});

	}

	/**
	 * Called by Settings dialog when a connection is establised with the XMPP
	 * server
	 * 
	 * @param connection
	 */
	public void setConnection() {
		connection = XMPPLogic.getInstance().getConnection();
		if (connection != null) {
			// Add a packet listener to get messages sent to us
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			connection.addPacketListener(new PacketListener() {
				@Override
				public void processPacket(Packet packet) {
					final Message message = (Message) packet;
					if (message.getBody() != null) {
						final String fromName = StringUtils.parseBareAddress(message
								.getFrom());
						
						Log.i("XMPPChatDemoActivity", "Text Recieved " + message.getBody()
								+ " from " + fromName );
						
				
						//messages.add(fromName + ":");
						
						
						// Add the incoming message to the list view
						mHandler.post(new Runnable() {
							public void run() {
								
								OneComment comment = new OneComment(false, message.getBody(), true);
								adapter.add(comment);
								listview.setAdapter(adapter);
								
								comment.saveOffline(
										getApplicationContext(), 
										fromName, 
										DateUtils.millisToSimpleDate(
												System.currentTimeMillis(), 
												DateFormatz.DATE_FORMAT_5));
								
							}
						});
					}
				}
			}, filter);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//connection.disconnect();
		//Log.e("NULLIFIED","");
	}
	
	
}