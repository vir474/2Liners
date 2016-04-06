package com.viral.twoliners.app;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
//import com.example.phptoandroid.*;



public class Asyncphp extends Activity {
	
	private ProgressBar pb;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
     
        super.onCreate(savedInstanceState);
        setContentView(R.layout.async_layout);
         
        
                String serverURL = "http://192.168.1.13/android.php";
                 pb=(ProgressBar)findViewById(R.id.progressBar2);
                // Create Object and call AsyncTask execute Method
                new LongOperation().execute(serverURL);
                  
    }
     
     
    // Class with extends AsyncTask class
     class LongOperation  extends AsyncTask<String, Void, Void> {
         
        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        
         
        TextView uiUpdate = (TextView) findViewById(R.id.textView3);
         
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
                 
                // Call long running operations here (perform background computation)
                // NOTE: Don't call UI Element here.
                 
                // Server url call by GET method
                HttpGet httpget = new HttpGet(urls[0]);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = Client.execute(httpget, responseHandler);
                 
            } catch (ClientProtocolException e) {
                Log.e("clientprotocal", e.getMessage());
                cancel(true);
            } catch (IOException e) {
            	Log.e("IOException", e.getMessage());
                cancel(true);
            }
             
            return null;
        }
         
        @Override
		protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.
             
            // Close progress dialog
            pb.setVisibility(View.INVISIBLE);
            uiUpdate.setText(Content);
        }
         
    }
}