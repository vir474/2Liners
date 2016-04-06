package com.viral.twoliners.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class MainData {

	private HttpClient httpclient;
	private HttpPost httppost;

	private JSONObject object;
	private LazyAdapter adapter;
	public static MainData obj;
	private List<NameValuePair> nameValuePairs;
	private HttpResponse response;
	private String result;
	private Vector v;

	public static MainData getInstance() {
		if (obj == null) {
			obj = new MainData();
		}
		return obj;
	}

	public String authenticateUser(String username, String password) {
		try {

			httpclient = new DefaultHttpClient();
			httppost = new HttpPost("http://10.0.0.2/android/testandroid.php");

			// add your data
			nameValuePairs = new ArrayList<NameValuePair>(2);

			// Always use the same variable name for posting i.e the android
			// side variable name and php side variable name should be similar,
			nameValuePairs.add(new BasicNameValuePair("username", username)); // $Edittext_value
																				// =
																				// $_POST['Edittext_value'];
			nameValuePairs.add(new BasicNameValuePair("password", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		} catch (Exception e) {
			Log.e("error", e.toString());
		}

		try {
			response = httpclient.execute(httppost);

			// ResponseHandler<String> responseHandler = new
			// BasicResponseHandler();
			// result = httpclient.execute(httppost, responseHandler);

			result = inputStreamToString(response.getEntity().getContent())
					.toString();
		} catch (Exception e) {
			Log.e("response", e + "");
		}
		return result;
	}

	public Vector getPostDetails(String flag,String useridl) {
		v = new Vector();
		String image, username, likes, dislikes, quote, time, quoteid, rating,userid,like_flag,dislike_flag,ratingcount,fav_flag,ratingflag;

		httpclient = new DefaultHttpClient();
		
		Log.e("xyzabc", flag);
		try{
		if(flag=="allquotes")
		{
		httppost = new HttpPost("http://10.0.2.2/android/allquotes.php");
		nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("userid", useridl));
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		}
		else if(flag=="yourfavourites")
		{
			httppost = new HttpPost("http://10.0.2.2/android/yourfavourites.php");
			nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("userid", useridl));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		}
		else if(flag=="yourquotes")
		{
			httppost = new HttpPost("http://10.0.2.2/android/quotesbyuser.php");
			nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("userid", useridl));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		}
		else if(flag=="adminmsgs")
		{
			httppost = new HttpPost("http://10.0.2.2/android/amdinmsgs.php");
			
		}
		}
		catch(Exception e)
		{
			Log.e("namevaluepair", e+"");
		}
		

		HttpResponse response;
		try {
			
			
			response = httpclient.execute(httppost);
			String jsonResult = inputStreamToString(
					response.getEntity().getContent()).toString();
			Log.i("sgf", jsonResult);
			System.out.println(jsonResult);
			if(jsonResult.equalsIgnoreCase("no"))
			{
				return v;
			}
			object = new JSONObject(jsonResult);
		}

		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(flag=="adminmsgs")
		{
			for (int position = 0; position < object.length(); position++) {
				try {
					quote=object.getString("adminmsg"+position);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return v;
		}
		

		try {
			for (int position = 0; position < object.length() / 14; position++) {
				quote = object.getString("quote" + position);
				username = object.getString("username" + position);
				time = object.getString("time" + position);
				rating = object.getString("rating" + position);
				likes = object.getString("likes" + position);
				dislikes = object.getString("dislikes" + position);
				image = object.getString("image" + position);
				quoteid = object.getString("quoteid" + position);
				userid = object.getString("userid" + position);
				like_flag = object.getString("like_flag" + position);
				dislike_flag = object.getString("dislike_flag" + position);
				ratingcount = object.getString("ratingcount" + position);
				fav_flag=object.getString("fav_flag" + position);
				ratingflag=object.getString("ratingflag"+position);
				
				QuoteDetails obj = new QuoteDetails(image, username, likes,
						dislikes, quote, time, quoteid, rating,userid,like_flag,dislike_flag,ratingcount,fav_flag,ratingflag);

				v.add(obj);

			}
		} catch (Exception e) {
			Log.e("quotedetails", e.toString());
		}

		return v;

	}

	private StringBuilder inputStreamToString(InputStream is) {
		String rLine = "";
		StringBuilder answer = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try {
			while ((rLine = rd.readLine()) != null) {
				answer.append(rLine);
			}
		}

		catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}
	
	
}
