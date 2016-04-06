package com.viral.twoliners.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
//import com.example.phptoandroid.*;

public class Asyncquotes extends Activity {

	private ProgressBar pb;
	private ProgressBar pb1;
	private HttpClient httpclient;
	private HttpPost httppost;
	private ListView lv;
	private ArrayAdapter<String> arrayAdapter;
	private JSONObject object;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asyncquotes);
		
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost("http://192.168.1.13/testandroid.php");
		lv = (ListView) findViewById(R.id.listView3);
		pb=(ProgressBar)findViewById(R.id.progressBar4);
		pb1=(ProgressBar)findViewById(R.id.progressBar1);
		new LongOperation().execute();
		
	}
	
	private class LongOperation  extends AsyncTask<String, Void, Void> {
        
 
         
        @Override
		protected void onPreExecute() {
            // NOTE: You can call UI Element here.
             
            //UI Element
           pb.setVisibility(View.VISIBLE);
           pb1.setVisibility(View.VISIBLE);
            
        }
 
        // Call after onPreExecute method
        @Override
		protected Void doInBackground(String... urls) {
        	try {

    			HttpResponse response = httpclient.execute(httppost);
    			String jsonResult = inputStreamToString(
    					response.getEntity().getContent()).toString();
    			Log.e("sgf", jsonResult);
    			System.out.println(jsonResult);
    			object = new JSONObject(jsonResult);
    			
    			
    			//Log.i("json value", object.toString());
    			// /JSONArray objects=object.getJSONArray("data");
    			
    		//	pb.setVisibility(View.INVISIBLE);
    			
    			
    			
             
        	} catch (JSONException e) {
    			e.printStackTrace();
    		} catch (ClientProtocolException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
			return null;
        }
         
        @Override
		protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.
             
            // Close progress dialog
            pb.setVisibility(View.INVISIBLE);
            final int len = object.length();

			arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
					R.layout.listitem, R.id.quotetext);
			
			for (int i = 0; i < len; i++) {
				try {
					arrayAdapter.add(object.getString("name" + i));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				lv.setAdapter(arrayAdapter);
				//Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
			}
        }
         
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
