package com.blinkedup.kooka;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blinkedup.kooka.R;
import com.toucan.sqlite.SQLiteHandler;
import com.toucan.utility.RoundedImageView;

public class PeopleProfileActivity extends Activity {

	private ImageView imgProfile;
	private Button btnMessage;
	private TextView txtDistance;
	private TextView txtFName;
	private TextView txtAge;
	private TextView txtGender;
	private TextView txtStatus;
	private TextView txtAboutMe;
	private TextView txtLookingType;


	String username;
	String email;
	String fname;
	String age;
	String gender;
	String distance;
	String about_me;
	String looking_type;
	String status;

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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_profile);

		imgProfile = (ImageView) findViewById(R.id.img_profile);
		RoundedImageView riv = new RoundedImageView(this);
		Bitmap rawImage = BitmapFactory.decodeResource(this.getResources(),
		R.drawable.pic_sample_girl);

		Bitmap circImage = riv.getCroppedBitmap(rawImage, 400);
		imgProfile.setImageBitmap(circImage);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);
		} else {
			Log.e("NOTICE", "Device cannot handle ActionBar");
		}


		txtFName = (TextView) findViewById(R.id.user_fname);
		//txtLName = (TextView) findViewById(R.id.email);
		txtAge = (TextView) findViewById(R.id.user_age);
		txtGender = (TextView) findViewById(R.id.user_gender);
		txtLookingType = (TextView) findViewById(R.id.user_looking_for);
		txtStatus = (TextView) findViewById(R.id.user_status);
		txtAboutMe = (TextView) findViewById(R.id.user_about);

		Intent intent = getIntent();
		username = intent.getStringExtra("TAG_GEO_USER");
		email = intent.getStringExtra("TAG_GEO_EMAIL");
		fname = intent.getStringExtra("TAG_GEO_FNAME");
		age = intent.getStringExtra("TAG_GEO_AGE");
		gender = intent.getStringExtra("TAG_GEO_GENDER");
		distance = intent.getStringExtra("TAG_GEO_DISTANCE");
		about_me = intent.getStringExtra("TAG_GEO_ABOUTME");
		looking_type = intent.getStringExtra("TAG_GEO_LOOKING_TYPE");
		status = intent.getStringExtra("TAG_GEO_STATUS");


		//txtLName = (TextView) findViewById(R.id.email);

		if (age.length() < 4) {
			txtFName.setText(fname + ",");
			txtAge.setText(age);
		} else {
			txtFName.setText(fname);
			txtAge.setText("");
		}
		if (gender.length() == 0) {
			if ((looking_type.length() == 0) || (looking_type.equals("null"))) {
				txtGender.setText("");
				txtLookingType.setText("");
			} else {
				txtGender.setText("");
				txtLookingType.setText("Looking for " + looking_type);
			}
		} else {
			if ((looking_type.length() == 0) || (looking_type.equals("null"))) {
				txtGender.setText(gender);
				txtLookingType.setText("");
			} else {
				txtGender.setText(gender + ",");
				txtLookingType.setText("Looking for " + looking_type);
			}
		}

		if ((about_me.length() == 0) || (about_me.equals("null"))) {
			txtAboutMe.setText("No status yet");
		} else {
			txtAboutMe.setText(about_me);
		}

		txtStatus.setText(status);


		btnMessage = (Button) findViewById(R.id.btn_mes);
		btnMessage.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent intentMes = new Intent(PeopleProfileActivity.this, ChatActivity.class);
				intentMes.putExtra("email", email);
				intentMes.putExtra("username", username);
				intentMes.putExtra("fname", fname);
				startActivity(intentMes);
			}
		});


	}

}