package com.blinkedup.kooka;
import org.jivesoftware.smack.AccountManager;
import com.blinkedup.kooka.R;
import com.toucan.openfire.Account;
import com.toucan.openfire.XMPPLogic;
import com.toucan.sqlite.SQLiteHandler;
import com.toucan.sqlite.SessionManager;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;

public class TabHostActivity extends TabActivity {
	private SessionManager session;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_host);
		
		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		
		session = new SessionManager(getApplicationContext());
		SQLiteHandler db = new SQLiteHandler(this);
		db.openToWrite();
		db.updateBroadcasting(0);
		db.updateBroadcastTicker(0);
		
		if (!session.isLoggedIn()) {
			intent = new Intent(TabHostActivity.this, MainSignInActivity.class);
			startActivity(intent);
			finish();
		} else {
			Account ac = new Account();
			ac.LogInChatAccount(db.getUsername(), db.getPass(), db.getEmail());
			XMPPLogic.getInstance().getConnection();
			
			intent = new Intent().setClass(this, AroundMeActivity.class);
			spec = tabHost.newTabSpec("home").setIndicator("", res.getDrawable(R.drawable.ic_tab_people)).setContent(intent);
			tabHost.addTab(spec);
			
			intent = new Intent().setClass(this, ChatHistoryActivity.class);
			spec = tabHost.newTabSpec("home3").setIndicator("", res.getDrawable(R.drawable.ic_tab_chat)).setContent(intent);
			tabHost.addTab(spec);
			
			intent = new Intent().setClass(this, GroupChatHomeActivity.class);
			spec = tabHost.newTabSpec("home1").setIndicator("", res.getDrawable(R.drawable.ic_tab_group)).setContent(intent);
			tabHost.addTab(spec);
			
			intent = new Intent().setClass(this, UserProfileActivity.class);
			spec = tabHost.newTabSpec("home2").setIndicator("", res.getDrawable(R.drawable.ic_tab_profile)).setContent(intent);
			tabHost.addTab(spec);
			
			tabHost.setCurrentTab(0);
			tabHost.setup();
			int heightValue = 45;
			
			for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
				tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = (int)(heightValue * this.getResources().getDisplayMetrics().density);
				tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#D2D7D3"));
				tabHost.getTabWidget().setDividerDrawable(null);
			}
		}
	}
}