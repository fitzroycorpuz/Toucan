package com.toucan.utility;

import android.util.Log;

public class L {
	
	private static final String TAG = "TOUCAN";

	public static void debug(String msg) {
		Log.d(TAG, msg);
	}
	
	public static void error(String msg) {
		Log.e(TAG, msg);
	}

}
