package com.blinkedup.kooka;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.blinkedup.kooka.R;
import com.devspark.appmsg.AppMsg;
import com.devspark.appmsg.AppMsg.Style;
import com.toucan.JSON.JSONParser;
import com.toucan.configuration.AppConfig;
import com.toucan.configuration.AppController;
import com.toucan.objects.Users;
import com.toucan.sqlite.SQLiteHandler;
import com.toucan.utility.DateUtils;
import com.toucan.utility.MyLocation;
import com.toucan.utility.MyLocation.LocationResult;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import org.apache.commons.lang.StringUtils;


public class AroundMeActivity extends Activity implements OnRefreshListener {
	private static final String TAG = AroundMeActivity.class.getSimpleName();
	//Button btnUpdate;
	LocationManager locationManager;
//	MyLocationListener locationListener;
	float ftLatitude = 0;
	float ftLongitude = 0;
	Handler mHandler;
	GridView grid;
	CustomGrid adapter;
	SQLiteHandler db;
	float i = 0;
	ArrayList < String > web = new ArrayList < String > ();
	ArrayList < String > availabilty = new ArrayList < String > ();
	ArrayList < Integer > distance = new ArrayList < Integer > ();
	ArrayList < Integer > imageId = new ArrayList < Integer > ();

	ArrayList < String > arr_fname = new ArrayList < String > ();
	ArrayList < String > arr_age = new ArrayList < String > ();
	ArrayList < String > arr_uname = new ArrayList < String > ();
	ArrayList < String > arr_gender = new ArrayList < String > ();
	ArrayList < String > arr_looking_type = new ArrayList < String > ();
	ArrayList < String > arr_date_seen = new ArrayList < String > ();
	ArrayList < String > arr_about = new ArrayList < String > ();
	ArrayList < String > arr_email = new ArrayList < String > ();
	ArrayList < String > arr_status = new ArrayList < String > ();

	SwipeRefreshLayout mSwipeRefreshLayout;

	boolean gps_enabled = false;
	boolean network_enabled = false;
	boolean hasGps = false;
	float longitude = 0;
	float latitude = 0;
	String gpsProvider = "";

	String ins_user = "";
	float ins_latitude = 0;
	float ins_longitude = 0;
	String existingUsers;
	
	DateUtils du;
	private AsyncTask < String, String, String > mTask;

	JSONParser jsonParser = new JSONParser();

	private static final String TAG_SUCCESS = "success";

	JSONParser jParser = new JSONParser();
	ArrayList < HashMap < String, String >> userList;
	JSONArray nearby_users = null;

	// JSON Node names
	private static final String TAG_GEO_SUCCESS = "success";

	private static final String TAG_GEO = "geo";
	private static final String TAG_GEO_PID = "id";
	private static final String TAG_GEO_USER = "user";
	private static final String TAG_GEO_LATITITUDE = "latitude";
	private static final String TAG_GEO_LONGI = "longitude";
	private static final String TAG_GEO_PROVIDER = "gps_provider";
	private static final String TAG_GEO_DATE_CREATE = "date_create";
	private static final String TAG_GEO_FNAME = "firstname";
	private static final String TAG_GEO_LNAME = "lastname";
	private static final String TAG_GEO_BIRTHDAY = "birthday";
	private static final String TAG_GEO_GENDER = "gender";
	private static final String TAG_GEO_DISTANCE = "geo_distance";

	private static final String TAG_GEO_ABOUTME = "about_me";
	private static final String TAG_GEO_LOOKING_TYPE = "looking_type";
	private static final String TAG_GEO_STATUS = "status";

	private static final String TAG_GEO_EMAIL = "email_address";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_around_me);


		
		
		du = new DateUtils();
		db = new SQLiteHandler(this);
		db.openToWrite();
		userList = new ArrayList < HashMap < String, String >> ();

		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
		mSwipeRefreshLayout.setColorSchemeResources(
		R.color.niagara,
		R.color.buttercup,
		R.color.niagara
		);
		mSwipeRefreshLayout.setBackgroundColor(getResources().getColor(R.color.carrara));
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {@Override
			public void onRefresh() {

				new Handler().postDelayed(new Runnable() {@Override
					public void run() {	
						getNewLoc();
					}
				}, 2000);
			}
		});

		web.add(0, "You");
		distance.add(0, 0);
		imageId.add(0, R.drawable.pic_sample_girl);
		availabilty.add(0, "Online");
		adapter = new CustomGrid(AroundMeActivity.this, web, imageId, availabilty, distance);
		 mHandler = new Handler()
			{
			    public void handleMessage(android.os.Message msg)
			    {			    
			    	
			    	if (msg.what == 1){
			    		mSwipeRefreshLayout.setRefreshing(false);
			    		makeNotify("Failed To Retrieve GPS Location", AppMsg.STYLE_ALERT);
				    }
			    	else{
			    		Log.e("UU","CANNOT");
			    	}
			 	}
			};
			
			grid = (GridView) findViewById(R.id.grid);

		grid.setAdapter(adapter);
		grid.setBackgroundColor(Color.WHITE);
		grid.setVerticalSpacing(1);
		grid.setHorizontalSpacing(1);
		
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView <? > parent, View view,
			int position, long id) {
				ArrayList < Users > us = new ArrayList < Users > ();
				try {
					//  Toast.makeText(AroundMeActivity.this, "You Clicked at " +arr_fname.get(position) , Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(AroundMeActivity.this, PeopleProfileActivity.class);
					Log.e("ccc", arr_about.get(position));
					//intent.putExtra("TAG_GEO_PID", us.get(position).getId());
					intent.putExtra("TAG_GEO_USER", arr_uname.get(position));
					intent.putExtra("TAG_GEO_EMAIL", arr_email.get(position));

					intent.putExtra("TAG_GEO_FNAME", arr_fname.get(position));
					intent.putExtra("TAG_GEO_AGE", arr_age.get(position));
					intent.putExtra("TAG_GEO_GENDER", arr_gender.get(position));
					intent.putExtra("TAG_GEO_DISTANCE", "25");
					intent.putExtra("TAG_GEO_ABOUTME", arr_about.get(position));
					intent.putExtra("TAG_GEO_LOOKING_TYPE", arr_looking_type.get(position));
					intent.putExtra("TAG_GEO_STATUS", arr_status.get(position));

					startActivity(intent);
				} catch (Exception e) {}
				// finish();
			}
		});

		updateGrid("0");
		
		
	}
	
	private void getNewLoc(){
		String dtUpdate = db.getLocationDateUpdate();
    	if ((dtUpdate == "")||(du.hoursAgo(dtUpdate))){
    		Log.e("LOCATION INTELLIGENCE","Update needed...");
    		LocationResult locationResult = new LocationResult(){
			    @Override
			    public void gotLocation(Location location){
			    	if (location != null){
			    		ftLatitude = (float) location.getLatitude();
					    ftLongitude =   (float) location.getLongitude();
					    latitude = ftLatitude;
					    longitude = ftLongitude;
					    db.insertLocation(longitude, latitude);
					    SendLocToServer();
					    
			    	}
			    	else{
			    		//Looper.prepare();
			    		
			    		try{
			    			
			    			mHandler.sendEmptyMessage(1);
			    		}
			    		catch(Exception e){
			    			Log.e("FAIL","FAILED A:LL");
			    		}
			    	//	makeNotify("Failed To Retrieve GPS Location", AppMsg.STYLE_INFO);
				//		mSwipeRefreshLayout.setRefreshing(false);
			    	}
			    }
			};
			
		PackageManager packMan = getPackageManager();
		hasGps = packMan.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);

		MyLocation myLocation = new MyLocation();
		boolean availLoc = myLocation.getLocation(this, locationResult);
		if (availLoc == false){
			makeNotify("GPS Services Unavailable", AppMsg.STYLE_ALERT);
			mSwipeRefreshLayout.setRefreshing(false);
		}
    	}
    	else{
    		Log.e("LOCATION INTELLIGENCE","Getting db location...");
    		ftLatitude = Float.parseFloat(db.getLocationLatitude());
		    ftLongitude =   Float.parseFloat(db.getLocationLongitude());
		    latitude = ftLatitude;
		    longitude = ftLongitude;
		    SendLocToServer();
    	}
	}
	
	private void updateGrid(String sentType) {
		ArrayList < Users > us = new ArrayList < Users > ();
		ArrayList<Users> list = new ArrayList<Users>();
		list = db.getNearByUserDetails();
		Comparator<Users> comparator = new Comparator<Users>() {
			@Override
			public int compare(Users lhs, Users rhs) {
				// TODO Auto-generated method stub
				return lhs.getDistance() - rhs.getDistance(); // use your logic
			}
		};
		Collections.sort(list, comparator); 
		
		
		us = list;
		for (int j = 0; j < us.size(); j++) {
			
			if (sentType.equals("1")) {
				if (us.get(j).getShown().equals("0")) {
					imageId.add(j, R.drawable.pic_sample_girl);
					availabilty.add(j, "GG1");
					web.add(j, displayGridCellName(us.get(j).getFName(), us.get(j).getUserName()) + ", " + displayAge(us.get(j).getAge()));
					distance.add(j, us.get(j).getDistance());

					arr_uname.add(j, us.get(j).getUserName());
					arr_fname.add(j, displayGridCellName(us.get(j).getFName(),  us.get(j).getUserName()));
					arr_age.add(j, us.get(j).getAge());
					arr_gender.add(j, us.get(j).getGender());
					arr_looking_type.add(j, us.get(j).getLookingType());
					//	arr_date_seen= new ArrayList<String>();
					arr_about.add(j, us.get(j).getAboutMe());
					arr_email.add(j, us.get(j).getEmail());
					arr_status.add(j, us.get(j).getStatus());

					//break;
				} else {
					availabilty.set(j, "GG2");
					web.set(j, displayGridCellName(us.get(j).getFName(),us.get(j).getUserName()) + ", " + displayAge(us.get(j).getAge()));
					distance.set(j, us.get(j).getDistance());
					arr_uname.add(j, us.get(j).getUserName());
					arr_fname.add(j, displayGridCellName(us.get(j).getFName(), us.get(j).getUserName()));
					arr_age.add(j, us.get(j).getAge());
					arr_gender.add(j, us.get(j).getGender());
					arr_looking_type.add(j, us.get(j).getLookingType());
					//	arr_date_seen= new ArrayList<String>();
					arr_about.add(j, us.get(j).getAboutMe());
					arr_email.add(j, us.get(j).getEmail());
					arr_status.add(j, us.get(j).getStatus());
				}
			}
			else if (sentType.equals("0")) {
				imageId.add(j, R.drawable.pic_sample_girl);
				availabilty.add(j, "Busy");
				web.add(j, displayGridCellName(us.get(j).getFName(), us.get(j).getUserName()) + ", " + displayAge(us.get(j).getAge()));
				distance.add(j, us.get(j).getDistance());
				arr_uname.add(j, us.get(j).getUserName());
				arr_fname.add(j, displayGridCellName(us.get(j).getFName(), us.get(j).getUserName()));
				arr_age.add(j, us.get(j).getAge());
				arr_gender.add(j, us.get(j).getGender());
				arr_looking_type.add(j, us.get(j).getLookingType());
				//	arr_date_seen= new ArrayList<String>();
				arr_about.add(j, us.get(j).getAboutMe());
				arr_email.add(j, us.get(j).getEmail());
				arr_status.add(j, us.get(j).getStatus());
			}
			
		}
	
		adapter.notifyDataSetChanged();
		mSwipeRefreshLayout.setRefreshing(false);
	}

	private String displayGridCellName(String fname, String user) {
		if (fname.equals("")) {
			return user;
		} else {
			return fname;
		}
	}

	private String displayAge(String age) {
		if (age.length() < 4) {
			return age;
		} else {
			return "";
		}
	}

	private void SendLocToServer() {
		String tag_string_req = "getgeo";
		StringRequest strReq = new StringRequest(Method.POST,
		AppConfig.URL_GETGEO, new Response.Listener < String > () {

			@Override
			public void onResponse(String response) {
				Log.e(TAG, "SAVE GEO Response: " + response.toString());

				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");
					if (!error) {
						// User successfully stored in MySQL
						// Now store the user in sqlite
						GetNearbyUsers();
						Log.e("JSON", "User geo stored on mySql");
						// finish();
					} else {

						// Error occurred in registration. Get the error
						// message
						Log.e("JSON", "Error occurred in registration");
						String errorMsg = jObj.getString("error_msg");


					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Error: " + error.getMessage());
				makeNotify("Cannot connect to server", AppMsg.STYLE_ALERT);
				// Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
				mSwipeRefreshLayout.setRefreshing(false);
				//hideDialog();
			}
		}) {@Override
			protected Map < String, String > getParams() {
				// Posting params to register url
				Map < String, String > params = new HashMap < String, String > ();

				ins_user = db.getLoggedInID();
				ins_latitude = latitude;
				ins_longitude = longitude;
				String ins_g_provider = gpsProvider;
				Date dateNow = new Date();

				String curDate = du.convertDateToString(dateNow);

				params.put("tag", "getgeo");
				params.put("p_user", ins_user);
				params.put("p_longitude", ins_longitude + "");
				params.put("p_latitude", ins_latitude + "");
				params.put("p_gps_provider", ins_g_provider);
				params.put("p_date_update", curDate);
				//   params.put("$user_id", longitude +"");
				// params.put("latitude", latitude +"");


				return params;
			}
		};
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}


	private void makeNotify(CharSequence con, Style style) {
		AppMsg.makeText(this, con, style).show();
	}

	private void GetNearbyUsers() {
		// Tag used to cancel the request
		String tag_string_req = "collect";
		StringRequest strReq = new StringRequest(Method.POST,
		AppConfig.URL_NEARBY, new Response.Listener < String > () {

			@Override
			public void onResponse(String response) {
				Log.e(TAG, "GET GEO Response: " + response.toString());
				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");
					if (!error) {
						// User successfully stored in MySQL
						// Now store the user in sqlite
						Log.e("JSON", "GEO COLLECTED");
						// finish();

						nearby_users = jObj.getJSONArray("geo");


						existingUsers = db.getExistingOnDBUsers();


						//JSONObject json = new JSONObject(jsonString);
						//JSONArray jArray = jObj.getJSONArray("geo");

						Log.e("LOG", "*****JARRAY*****" + nearby_users.length());


						///////////////////////
						if (nearby_users.length() == 0) {
							mSwipeRefreshLayout.setRefreshing(false);
						} else {
							for (int i = 0; i < nearby_users.length(); i++) {
								JSONObject c = nearby_users.getJSONObject(i);

								// Storing each json item in variable
								String id = c.getString(TAG_GEO_PID);
								String uname = c.getString(TAG_GEO_USER);
								String fname = c.getString(TAG_GEO_FNAME);
								String lname = c.getString(TAG_GEO_LNAME);

								String status = c.getString(TAG_GEO_STATUS);
								String about_me = c.getString(TAG_GEO_ABOUTME);
								String looking_type = c.getString(TAG_GEO_LOOKING_TYPE);
								String email_address = c.getString(TAG_GEO_EMAIL);

								Date bday = du.convertStringToDateToLocal(c.getString(TAG_GEO_BIRTHDAY));
								String age = du.getAge(bday);
								int distance = Math.round(Float.parseFloat(c.getString(TAG_GEO_DISTANCE)));
								String sex = c.getString(TAG_GEO_GENDER);


								boolean containerContainsContent = StringUtils.containsIgnoreCase(existingUsers, "." + id + ".");
								if (containerContainsContent == true) {
									db.updateUser(id, uname, distance, fname, lname, age, sex, "", "2012-12-12 09:09:09", 1, about_me, looking_type, status, email_address);
									Log.e("Log", "Updated");
								} else {
									db.insertNearbyUser(id, uname, distance, fname, lname, age, sex, "", "2012-12-12 09:09:09", 0, about_me, looking_type, status, email_address);
									Log.e("Log", "Inserted");
								}

								if (i == nearby_users.length() - 1) {
									updateGrid("1");
								}

							}
						}
					} else {
						 makeNotify("Error occurred while collecting users", AppMsg.STYLE_ALERT);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(),
				error.getMessage(), Toast.LENGTH_LONG).show();
				//hideDialog();
				mSwipeRefreshLayout.setRefreshing(false);
			}
		}) {@Override
			protected Map < String, String > getParams() {
				// Posting params to register url
				Map < String, String > params = new HashMap < String, String > ();

				ins_latitude = latitude;
				ins_longitude = longitude;

				params.put("tag", "collect");
				params.put("pid", ins_user);
				params.put("longitude", ins_longitude + "");
				params.put("latitude", +ins_latitude + "");

				// Log.e("MAP", ins_user + " + "+ins_longitude+" : "+ins_latitude);
				// params.put("latitude", latitude +"");


				return params;
			}
		};
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}
	
	protected void onPause() {
		super.onPause();
		//locationManager.removeUpdates(locationListener);
		Log.e("ON PAUSE", "APP PAUSE");
	}
}