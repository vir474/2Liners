package com.viral.twoliners.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
//import com.example.phptoandroid.*;

public class NewQuote extends Activity {
	
	private EditText quote;
	private Button post;
	private ProgressBar pb;
	private HttpClient httpclient;
	private HttpPost httppost;
	
	private List<NameValuePair> nameValuePairs;
	private String jsonResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.newquote);
		
		quote=(EditText)findViewById(R.id.newquote);
		post=(Button)findViewById(R.id.postbtn);
		
		post.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				httpclient = new DefaultHttpClient();
				httppost = new HttpPost("http://10.0.2.2/android/postquote.php");
				
				pb=(ProgressBar)findViewById(R.id.newquote_pb);
				
				nameValuePairs = new ArrayList<NameValuePair>(2);
		        
		        // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
		        nameValuePairs.add(new BasicNameValuePair("userid",((MyAppVar)getApplicationContext().getApplicationContext()).getValue()));  // $Edittext_value = $_POST['Edittext_value'];
		        nameValuePairs.add(new BasicNameValuePair("quote", quote.getText().toString()));
		        
		        try {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				new PostQuote().execute();
			}
		});
	}
	
	private class PostQuote  extends AsyncTask<String, Void, Void> {
        
		 
        
        @Override
		protected void onPreExecute() {
            // NOTE: You can call UI Element here.
             
            //UI Element
           pb.setVisibility(View.VISIBLE);
          
            
        }
 
        // Call after onPreExecute method
        @Override
		protected Void doInBackground(String... urls) {
        	try {

    			HttpResponse response = httpclient.execute(httppost);
    			jsonResult = inputStreamToString(
    					response.getEntity().getContent()).toString();
    			
    			
        	
    			//Log.i("json value", object.toString());
    			// /JSONArray objects=object.getJSONArray("data");
    			
    		//	pb.setVisibility(View.INVISIBLE);
    			
    			
    			
             
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
				 
				 if(jsonResult.equalsIgnoreCase("done"))
	    			{
	    				Toast.makeText(getApplicationContext(), "Your Quote has been Posted", Toast.LENGTH_SHORT).show();
	    			}
	    			else
	    				Toast.makeText(getApplicationContext(), "Due to some error we were unable to post ! ", Toast.LENGTH_SHORT).show();
				
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

}
