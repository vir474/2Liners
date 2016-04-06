package com.viral.twoliners.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
//import com.example.phptoandroid.*;


public class AdminMsgs extends Activity {
	

	private ProgressBar pb;
	private HttpClient httpclient;
	private HttpPost httppost;
	private ListView lv;
	private ArrayAdapter<String> arrayAdapter;
	private JSONObject object;
	private BaseAdapter adapter;
	HttpResponse response;
	private Button user;
	private Button all;
	private Button newquote;
	private Button yourfav;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_msgs);
		
		lv=(ListView)findViewById(R.id.amdinmsglv);
		pb=(ProgressBar)findViewById(R.id.adminsgpb);
	
		new LoadMsgs().execute();
	}
		
		private class LoadMsgs  extends AsyncTask<String, Void, Void> {
	        
			 
	         
	        @Override
			protected void onPreExecute() {
	            // NOTE: You can call UI Element here.
	             
	            //UI Element
	           pb.setVisibility(View.VISIBLE);
	          
	            
	        }
	 
	        // Call after onPreExecute method
	        @Override
			protected Void doInBackground(String... urls) {
	        	httpclient = new DefaultHttpClient();
	        	httppost = new HttpPost("http://10.0.2.2/android/adminmsgs.php");
	        	
	    		try {
	    			
	    			
	    			response = httpclient.execute(httppost);
	    			String jsonResult = inputStreamToString(
	    					response.getEntity().getContent()).toString();
	    			Log.i("sgf", jsonResult);
	    			System.out.println(jsonResult);
	    			object = new JSONObject(jsonResult);
	    
	        }
	    		catch (Exception e)
	    		{
	    			Log.e("msgs", e+"");
	    		}
	    		return null;
	        }
	         
	        @Override
			protected void onPostExecute(Void unused) {
	            // NOTE: You can call UI Element here.
	             
	            // Close progress dialog
	           
	            
	            
	            adapter=new BaseAdapter() {
	            	
					@Override
					public View getView(int arg0, View arg1, ViewGroup arg2) {
						// TODO Auto-generated method stub
						
						LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View inflatedView = inflater.inflate(R.layout.adminlistitem, null);
						TextView t1=(TextView)inflatedView.findViewById(R.id.adminmsg);
						
							try {
								t1.setText(object.getString("adminmsg"+arg0));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
						return inflatedView;
					}
					
					@Override
					public long getItemId(int arg0) {
						// TODO Auto-generated method stub
						return 0;
					}
					
					@Override
					public Object getItem(int arg0) {
						// TODO Auto-generated method stub
						try {
							return object.getString("adminmsg"+arg0);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}
					
					@Override
					public int getCount() {
						// TODO Auto-generated method stub
						return object.length();
					}
				};
//	            arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
//						R.layout.listitem, R.id.quotetext);
				
//				for (int i = 0; i < len; i++) {
//					try {
//						arrayAdapter.add(object.getString("quote" + i));
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
	            	pb.setVisibility(View.INVISIBLE);
					lv.setAdapter(adapter);
					//Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
				
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
	        
	        @Override
	    	public boolean onCreateOptionsMenu(Menu menu) {
	    		// Inflate the menu; this adds items to the action bar if it is present.
	    		getMenuInflater().inflate(R.menu.appmenu, menu);
	    		return true;
	    	}
		

	        @Override
	    	public boolean onOptionsItemSelected(MenuItem item) {
	    		// TODO Auto-generated method stub
	    		switch(item.getItemId())
	    		{
	    			case R.id.exit:
	    				finish();
	    				break;
	    				
	    			case R.id.settings:
	    				Intent i=new Intent(AdminMsgs.this,Settingsactivity.class);
	    				startActivity(i);
	    				break;
	    				
	    			case R.id.adminmsg:
	    				Intent in=new Intent(AdminMsgs.this,AdminMsgs.class);
	    				startActivity(in);
	    				break;
	    		}
	    		return false;
	    	}
}
