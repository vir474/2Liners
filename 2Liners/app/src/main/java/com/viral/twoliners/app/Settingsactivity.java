package com.viral.twoliners.app;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
//import com.example.phptoandroid.*;

public class Settingsactivity extends Activity {

	private Button upload;
	private Button save;
	private EditText username;
	private EditText password;
	private static int RESULT_LOAD_IMAGE = 1;
	private ProgressBar pb;
	 private String picpath;
	 private HttpClient httpclient;
		private HttpPost httppost;
		private List<NameValuePair> nameValuePairs;
		private HttpResponse response;
		private String result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settingsactivity);
		
		username=(EditText)findViewById(R.id.newusername);
		password=(EditText)findViewById(R.id.newpassword);
		pb=(ProgressBar)findViewById(R.id.settingspb);
		
		upload=(Button)findViewById(R.id.uploadpic);
		
		upload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(
				Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						 
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		
		save=(Button)findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String image_str = null;
				pb.setVisibility(View.VISIBLE);
				try{
					
				Bitmap bitmap = BitmapFactory.decodeFile(picpath);          
		        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
	            byte [] byte_arr = stream.toByteArray();
	            image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
	            ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
				}
				catch(Exception e)
				{
					Log.e("image", e+"");
					
				}
				pb.setVisibility(View.INVISIBLE);
				if(image_str==null)
				{
					Toast.makeText(getApplicationContext(), "No pic Selected", Toast.LENGTH_SHORT).show();
				}
				if(username.getText().toString().length()==0)
				{
					username.setError("Username Empty");
				}
				if(password.getText().toString().length()==0)
				{
					password.setError("Password Empty");
				}
				
				
				
				if(username.getText().toString().length()!=0 || password.getText().toString().length()!=0 || image_str!=null)
				{
try{
						
						httpclient = new DefaultHttpClient();
						httppost = new HttpPost("http://10.0.2.2/android/editprofile.php");
					
						//add your data
				        nameValuePairs = new ArrayList<NameValuePair>(4);
				        
				        // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
				        nameValuePairs.add(new BasicNameValuePair("username",username.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
				        nameValuePairs.add(new BasicNameValuePair("password",password.getText().toString().trim())); 
				        nameValuePairs.add(new BasicNameValuePair("userid",((MyAppVar)getApplicationContext().getApplicationContext()).getValue()));
				        nameValuePairs.add(new BasicNameValuePair("image",image_str));
				        
				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				       
						}
						catch(Exception e)
						{
							Log.e("editprofileerror", e.toString());
						}
					new CheckLogin().execute();
				}
				
				
			}
		});
		
		
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
				Intent i=new Intent(Settingsactivity.this,Settingsactivity.class);
				startActivity(i);
				break;
		}
		return false;
	}
	
private class CheckLogin  extends AsyncTask<String, Void, Void> {
        
		 
        
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

        		//Execute HTTP Post Request
		        response=httpclient.execute(httppost);
		        
		        result = inputStreamToString(response.getEntity().getContent()).toString();
		       
             
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
		   // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
		    System.out.println("Response : " + result);
		    try{
		    	
		    	
		        
				finish();
		    }
		    catch(Exception e)
		    {
		    	Toast.makeText(getApplicationContext(), "Wrong Username and Password", Toast.LENGTH_SHORT).show();
		    	 System.out.println("user : no"); 
		    }
		    
		     
			}
        }

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
     
    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaColumns.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        picpath=picturePath;
        cursor.close();
    }             
        // String picturePath contains the path of selected Image
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
