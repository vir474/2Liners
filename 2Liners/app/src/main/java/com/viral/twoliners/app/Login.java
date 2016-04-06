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
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
//import com.example.phptoandroid.*;

public class Login extends Activity {
	
	private EditText uname;
	private EditText password;
	private Button login;
	private ImageButton logini;
	private Button reset;
	private HttpClient httpclient;
	private HttpPost httppost;
	private ProgressBar pb;
	private List<NameValuePair> nameValuePairs;
	private HttpResponse response;
	private String result;
	private Button upload;
	 private static int RESULT_LOAD_IMAGE = 1;
	 private String picpath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login);
			
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		String userid=pref.getString("userid", null);
		
		if(userid!=null)
		{
			((MyAppVar)getApplicationContext().getApplicationContext()).setValue(userid);
	    	Intent in=new Intent(Login.this, AllQuotes.class);
			
			startActivity(in);
			finish();
		}
		
		final TextView path=(TextView)findViewById(R.id.textView1);
		
		pb=(ProgressBar)findViewById(R.id.pb_login);
		
		uname=(EditText)findViewById(R.id.usernametxt);
		password=(EditText)findViewById(R.id.passwordtxt);
		upload=(Button)findViewById(R.id.uploadpic);
		
		upload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(
						Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						 
						startActivityForResult(i, RESULT_LOAD_IMAGE);
						
						
						
			}
		});
		
		
		
		login=(Button)findViewById(R.id.loginbutton);
//		logini=(ImageButton)findViewById(R.id.loginimageButton);
//		reset=(Button)findViewById(R.id.resetbtn);
		
		
//		reset.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				uname.setText(null);
//				password.setText(null);
//			}
//		});
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				path.setText(picpath);

		        Bitmap bitmap = BitmapFactory.decodeFile(picpath);           ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
	            byte [] byte_arr = stream.toByteArray();
	            String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
	            ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
	 
	        //    nameValuePairs.add(new BasicNameValuePair("image",image_str));
				if(uname.getText().toString().length()==0)
				{
					Toast.makeText(getApplicationContext(), "Enter Username", Toast.LENGTH_LONG).show();
					uname.setError("Username Null");
				}
				if(password.getText().toString().length()==0)
				{
					Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();
					password.setError("Password Null");
				}
				else
				{
					try{
						
						httpclient = new DefaultHttpClient();
						httppost = new HttpPost("http://10.0.2.2/android/testandroid.php");
					
						//add your data
				        nameValuePairs = new ArrayList<NameValuePair>(2);
				        
				        // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
				        nameValuePairs.add(new BasicNameValuePair("username",uname.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
				        nameValuePairs.add(new BasicNameValuePair("password",password.getText().toString().trim())); 
				        
				        nameValuePairs.add(new BasicNameValuePair("image",image_str));
				        
				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				       
						}
						catch(Exception e)
						{
							Log.e("error", e.toString());
						}
					new CheckLogin().execute();
					
					
				}
			}
			
		});
		
		/*logini.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(uname.getText().toString().length()==0)
				{
					Toast.makeText(getApplicationContext(), "Enter Username", Toast.LENGTH_LONG).show();
					uname.setError("Username Null");
				}
				if(password.getText().toString().length()==0)
				{
					Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();
					password.setError("Password Null");
				}
				else
				{
					try{
						
						httpclient = new DefaultHttpClient();
						httppost = new HttpPost("http://10.0.2.2/android/testandroid.php");
					
						//add your data
				        nameValuePairs = new ArrayList<NameValuePair>(2);
				        
				        // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
				        nameValuePairs.add(new BasicNameValuePair("username",uname.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
				        nameValuePairs.add(new BasicNameValuePair("password",password.getText().toString().trim())); 
				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				       
						}
						catch(Exception e)
						{
							Log.e("error", e.toString());
						}
					new CheckLogin().execute();
					
					
				}
			}
			
		});*/
		
		
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
		        
//		        ResponseHandler<String> responseHandler = new BasicResponseHandler();
//		        result = httpclient.execute(httppost, responseHandler);
		        
		        result = inputStreamToString(response.getEntity().getContent()).toString();
		       
		       // System.out.println("Response : " + result); 
    			
    			
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
		   // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
		    System.out.println("Response : " + result);
		    try{
		    	String fullResult=result.replace(' ', '0');
		    	Log.e("Result:",fullResult+" "+fullResult.length());
		    	fullResult=fullResult.replaceAll("hello", null);
		    	Log.e("Result:"," "+fullResult);
		    	Log.e("length", result.length()+"");
		    	int i=Integer.parseInt(result);
		    	((MyAppVar)getApplicationContext().getApplicationContext()).setValue(result);
		    	
		    	SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		    	Editor editor = pref.edit();
		    	editor.putString("userid", result);
		    	editor.commit();
		        
		    	Intent in=new Intent(Login.this, AllQuotes.class);
				
				startActivity(in);
				finish();
		    }
		    catch(Exception e)
		    {
		    	Toast.makeText(getApplicationContext(), "Wrong Username and Password", Toast.LENGTH_SHORT).show();
		    	 System.out.println("user : no"); 
		    }
		    
		     /*if(result.equalsIgnoreCase("no"))  
		     {
		    	 Toast.makeText(getApplicationContext(), "Wrong Username and Password", Toast.LENGTH_SHORT).show();
		    	 System.out.println("user : no"); 
		     }
		     else
			{
				Toast.makeText(getApplicationContext(), "logged in", Toast.LENGTH_SHORT).show();
				System.out.println("user : yes"); 
				
				Intent in=new Intent(Login.this, Home.class);
				
				startActivity(in);
			}*/
				
				//Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
			}
        }
}

