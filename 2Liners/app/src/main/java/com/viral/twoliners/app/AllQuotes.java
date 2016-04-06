package com.viral.twoliners.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.System;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
//import com.example.phptoandroid.*;
public class AllQuotes extends Activity {

	private ProgressBar pb;
	
	private HttpClient httpclient;
	private HttpPost httppost;
	private ListView lv;
	private ArrayAdapter<String> arrayAdapter;
	private JSONObject object;
	private LazyAdapter adapter;
	private Vector list;
	private Button user;
	private Button all;
	private Button newquote;
	private Button yourfav;
	private SlidingDrawer menu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quotes);
		
		/*httpclient = new DefaultHttpClient();
		httppost = new HttpPost("http://10.0.2.2/android/allquotes.php");*/
		lv = (ListView) findViewById(R.id.home_lv);
		pb=(ProgressBar)findViewById(R.id.home_pb);
		menu=(SlidingDrawer)findViewById(R.id.slidingDrawer1);
		
		user=(Button)findViewById(R.id.button1);
		all=(Button)findViewById(R.id.button3);
		newquote=(Button)findViewById(R.id.button4);
		yourfav=(Button)findViewById(R.id.button2);
		
		/*final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
		final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
		final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.translate);*/
		
		user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//arg0.startAnimation(animAlpha);
				
				Intent in=new Intent(AllQuotes.this, QuotesByUser.class);
				menu.animateClose();
				startActivity(in);
				finish();
			}
		});
		
		all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//v.startAnimation(animRotate);
				Intent in=new Intent(AllQuotes.this, AllQuotes.class);
				menu.animateClose();
				startActivity(in);
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
				finish();
			}
		});
		
		newquote.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			//	arg0.startAnimation(animTranslate);
				
				
				Intent in=new Intent(AllQuotes.this, NewQuote.class);
				menu.animateClose();
				startActivity(in);
				
			}
		});
		
		yourfav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in=new Intent(AllQuotes.this, YourFavourite.class);
			//	yourfav.setImageBitmap(null);
				/*yourfav.setBackgroundResource( R.anim.menubtnanim );
				
				final AnimationDrawable mailAnimation = (AnimationDrawable) yourfav.getBackground();*/
		//		 mailAnimation.start();
				/*yourfav.post(new Runnable() {
				    public void run() {
				        if ( mailAnimation != null )
				      }
				});*/
				menu.animateClose();
				startActivity(in);
				finish();
			}
		});
		
		new LoadQuotes().execute();
		
	}
	
	private class LoadQuotes  extends AsyncTask<String, Void, Void> {
        
 
         
        @Override
		protected void onPreExecute() {
            // NOTE: You can call UI Element here.
             
            //UI Element
           pb.setVisibility(View.VISIBLE);
          
            
        }
 
        // Call after onPreExecute method
        @Override
		protected Void doInBackground(String... urls) {
        	/*try {

    			HttpResponse response = httpclient.execute(httppost);
    			String jsonResult = inputStreamToString(
    					response.getEntity().getContent()).toString();
    			Log.i("sgf", jsonResult);
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
    		}*/
        	
        	try{
        		String f="allquotes";
        		Log.e("trupal",f);
        		list=MainData.getInstance().getPostDetails(f,((MyAppVar)getApplicationContext().getApplicationContext()).getValue());
        		((MyAppVar)getApplicationContext().getApplicationContext()).setList(list);
        	}
        	catch(Exception e)
        	{
        		Log.e("quoteexp",e.toString());
        	}
			return null;
        }
         
        @Override
		protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.
             
            // Close progress dialog
           
            
            
            adapter=new LazyAdapter(AllQuotes.this, list);
//            arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
//					R.layout.listitem, R.id.quotetext);
			
//			for (int i = 0; i < len; i++) {
//				try {
//					arrayAdapter.add(object.getString("quote" + i));
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
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
    				Intent i=new Intent(AllQuotes.this,Settingsactivity.class);
    				startActivity(i);
    				break;
    				
    			case R.id.adminmsg:
    				Intent in=new Intent(AllQuotes.this,AdminMsgs.class);
    				startActivity(in);
    				break;
    		}
    		return false;
    	}

	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	}

}
