package com.toucan.utility;

import java.text.DateFormat;
import static java.util.concurrent.TimeUnit.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

public class DateUtils {
	
	
	public enum DateFormatz {
		
		DATE_FORMAT_0("yyyy-MM-dd hh:mm:ss"),
		DATE_FORMAT_1("dd/MM/yyyy hh:mm:ss"),
		DATE_FORMAT_2("MMMM dd, yyyy h:mm:ss aa"),
		DATE_FORMAT_3("EEEE, MMMM dd, yyyy h:mm:ss aa"),
		DATE_FORMAT_4("MM/dd/yyyy hh:mm:ss"),
		DATE_FORMAT_5("yyyy-MM-dd HH:mm:ss");
		
		private String format;

		DateFormatz(String format){
			this.format = format;
		}
		
	}
	
	
	public String convertStringToDate(String dateToConv){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date convertedDate = new Date();
		 String localTime = "";
		try {
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); 
			convertedDate = dateFormat.parse(dateToConv);
			
			localTime = convertedToLocale(convertedDate);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("DA",e.getLocalizedMessage());
		}
		
		return localTime;
	}
	
	public Date convertStringToDateToLocal(String dateToConv){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date convertedDate = new Date();
		 String localTime = "";
		try {
			dateFormat.setTimeZone(TimeZone.getDefault()); 
			convertedDate = dateFormat.parse(dateToConv);
			
			localTime = convertedToLocale(convertedDate);
			Date fromGmt = new Date(convertedDate.getTime() + TimeZone.getDefault().getOffset(convertedDate.getTime()));
			 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertedDate;
	}
	
	public Date convertDateToLocal(String dateToConv){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date convertedDate = new Date();
		 String localTime = "";
		 Date fromGmt = null;
		try {
			dateFormat.setTimeZone(TimeZone.getDefault()); 
			convertedDate = dateFormat.parse(dateToConv);
			
			localTime = convertedToLocale(convertedDate);
			fromGmt = new Date(convertedDate.getTime() + TimeZone.getDefault().getOffset(convertedDate.getTime()));
			 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fromGmt;
	}
	
	private String convertedToLocale(Date localeDate){
		String converted;
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

	    DateFormat formatter = new SimpleDateFormat("dd MMM � hh:mm a");    
	    formatter.setTimeZone(TimeZone.getDefault());  

	    converted = formatter.format(localeDate.getTime());
		return converted;
	}
	public String convertStringToYearDate(String dateToConv){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date convertedDate = new Date();
		 String localTime = "";
		try {
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); 
			convertedDate = dateFormat.parse(dateToConv);
			
			localTime = convertedToLocaleWithYear(convertedDate);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("DA",e.getLocalizedMessage());
		}
		
		return localTime;
	}
	private String convertedToLocaleWithYear(Date localeDate){
		String converted;
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

	    DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");    
	    formatter.setTimeZone(TimeZone.getDefault());  

	    converted = formatter.format(localeDate.getTime());
		return converted;
	}

	
	public String convertedToInstall(){
		Date localeDate = new Date();
		String converted;
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

	    DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");    
	    formatter.setTimeZone(TimeZone.getDefault());  

	    converted = formatter.format(localeDate.getTime());
		return converted;
	}
	
	public String convIntToLength(String len){
		  int parsedLen = Integer.parseInt(len);
		  int hr = parsedLen/3600;
		  int rem = parsedLen%3600;
		  int mn = rem/60;
		  int sec = rem%60;
		  String hrStr = (hr<10 ? "0" : "")+hr;
		  String mnStr = (mn<10 ? "0" : "")+mn;
		  String secStr = (sec<10 ? "0" : "")+sec; 
		  
		  if (hr==0){
			  return  mnStr +":"+ secStr;
		  }
		  else{
			  return hrStr  +":"+ mnStr +":"+ secStr;
		  }
	}
	public String convIntBaseToLength(int len){
		  int parsedLen = len;
		  int hr = parsedLen/3600;
		  int rem = parsedLen%3600;
		  int mn = rem/60;
		  int sec = rem%60;
		  String hrStr = (hr<10 ? "0" : "")+hr;
		  String mnStr = (mn<10 ? "0" : "")+mn;
		  String secStr = (sec<10 ? "0" : "")+sec; 
		  
		  if (hr==0){
			  return  mnStr +":"+ secStr;
		  }
		  else{
			  return hrStr  +":"+ mnStr +":"+ secStr;
		  }
	}
	public String getDate(){
		Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String strDate = sdf.format(c.getTime());
        
        return strDate;
	}
	
	public String convertDateToString(Date dateString){
	 //String dateString = "03/26/2012 11:49:00 AM";
	  String strDate = "";
	       
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	        strDate = sdf.format(dateString.getTime());
	    return strDate;
	}
	public String convertDateToStringToLocalTime(Date dateString){
		 //String dateString = "03/26/2012 11:49:00 AM";
		  String strDate = "";
		       
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        sdf.setTimeZone(TimeZone.getDefault());
		        strDate = sdf.format(dateString.getTime());
		    return strDate;
	}
	
	public Date convertStringToRawDate(String dateString){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		try {  
		    date = format.parse(dateString);  
		    System.out.println(date);  
		} catch (ParseException e) {  
		    // TODO Auto-generated catch block  
		    e.printStackTrace();  
		}
		    return date;
		}
	
	public String getDurationString(int seconds) {

	    int hours = seconds / 3600;
	    int minutes = (seconds % 3600) / 60;
	    seconds = seconds % 60;
	    
	    String hr;
	    String min;
	    String sec;
	    if (hours == 1){
	    	hr = " hour ";
	    }
	    else{
	    	hr = " hours ";
	    }
	    
	    if (minutes == 1){
	    	min = " minute ";
	    }
	    else{
	    	min = " minutes ";
	    }
	    
	    if (seconds == 1){
	    	sec = " second";
	    }
	    else{
	    	sec = " seconds";
	    }
	    
	    String lenSec = "";
	    if (seconds != 0){
	    	lenSec = seconds + sec;
	    }
	    if (minutes != 0){
	    	lenSec =  minutes + min + lenSec;
	    }
	    if (hours != 0){
	    	lenSec = hours + hr + lenSec;
		}
	    
	    if ((seconds == 0) && (minutes == 0) && (hours == 0)){
	    	lenSec = "No credits";
	    }
	    return lenSec + " left";
	}
	
	public Boolean checkIfToday(Date theDate){
		Date date = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		return fmt.format(theDate).equals(fmt.format(date));
	}
	
	public String getAge(Date dateOfBirth){
		Calendar dob = Calendar.getInstance();  
		dob.setTime(dateOfBirth);  
		Calendar today = Calendar.getInstance();  
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);  
		if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
			age--;  
		} 
		else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
		    && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
			age--;  
		}
		return age +"";
	}
	//retrieve difference between given date and now in hours
	public boolean hoursAgo(String datetime) {
	    Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(datetime);
			 Date now = Calendar.getInstance().getTime(); // Get time now
			  
			 long MAX_DURATION = MILLISECONDS.convert(20, MINUTES);

			 long duration = now.getTime() - date.getTime();
			 if (duration <= MAX_DURATION) {
				
				 return false;
				    
			 }
			 else{
				
				 return true;
			 }
		} catch (ParseException e) {
			
			return true;
			//return false to force update if failed to fetch date
		} // Parse into Date object
	   
	}
	
	public String getMinAgo(String datetime) {
	   // Date date = null;
		try {
			//date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(datetime);
			 Date now = Calendar.getInstance().getTime(); // Get time now
			 Date localTime = convertDateToLocal(datetime);
			
			
			 long duration = now.getTime() - localTime.getTime();
			 
			 int seconds = (int) (duration / 1000);
			 int minutes = (int) (duration / (1000*60));
			 int hours   = (int) (duration / (1000*60*60));
			 int days = (int) (duration / (1000*60*60*24));
			 int month = (int) ((duration / (1000*60*60*24))/30);
			
			 String retTime;
			 if ((seconds >= 0) && (seconds <= 59)){
				 retTime = "Just Now";
			 }
			 else if ((minutes >= 1) && (minutes <= 59)){
				 retTime = minutes + " Minutes Ago";
				 if ((minutes == 1)){
					 retTime = minutes + " Minute Ago";
				 }
			 }
			 else if ((hours >= 1) && (hours <= 48)){
				 retTime = hours + " Hours Ago";
				 if ((hours == 1)){
					 retTime = hours + " Hour Ago";
				 }
			 }
			 else if ((days >= 2) && (days <= 31)){
				 retTime = days + " Days Ago";
			 }
			 else if ((month >= 1) && (month <= 12)){
				 retTime = month + " Months Ago";
				 if ((month == 1)){
					 retTime = month + " Month Ago";
				 }
			 }
			 else{
				 retTime = convertStringToYearDate(datetime);
			 }
			 return retTime;
			
		} catch (Exception e) {
			String dateFormatted = convertStringToYearDate(datetime);
			return dateFormatted;
		} 
	   
	}
	
	public static String millisToSimpleDate(long now, DateFormatz df){
		SimpleDateFormat formatter = new SimpleDateFormat(df.format);

	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(now);
	    
	    return formatter.format(calendar.getTime());
	}
	
}
