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
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
//import com.example.phptoandroid.*;

public class Register extends Activity {
	
	private EditText uname;
	private EditText password;
	private Button login;
	private HttpClient httpclient;
	private HttpPost httppost;
	private ProgressBar pb;
	private List<NameValuePair> nameValuePairs;
	private HttpResponse response;
	private String result;
	private Button upload;
	 private static int RESULT_LOAD_IMAGE = 1;
	 private String picpath;
	 private TextView email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register);
			
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		String userid=pref.getString("userid", null);
		
		if(userid!=null)
		{
			((MyAppVar)getApplicationContext().getApplicationContext()).setValue(userid);
	    	Intent in=new Intent(Register.this, AllQuotes.class);
			
			startActivity(in);
			finish();
		}
		
		email=(TextView)findViewById(R.id.emailid);
		
		email.setText(UserEmailFetcher.getEmail(getApplicationContext()));
		
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

		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// email.setText(picpath);
				String image_str = null ;
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
					Toast.makeText(getApplicationContext(), "Please select a pic from gallery", Toast.LENGTH_SHORT).show();
				}
	 
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
				if(email.getText().toString().length()==0)
				{
					Toast.makeText(getApplicationContext(), "Email not Found, Please Check Your Primary email on Device", Toast.LENGTH_LONG).show();
				}
				
				if( uname.getText().toString().length() !=0 && password.getText().toString().length()!=0 && email.getText().toString().length()!=0 && image_str!=null)
					
				
				{
					try{
						
						httpclient = new DefaultHttpClient();
						httppost = new HttpPost("http://10.0.2.2/android/createprofile.php");
					
						//add your data
				        nameValuePairs = new ArrayList<NameValuePair>(4);
				        
				        // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
				        nameValuePairs.add(new BasicNameValuePair("username",uname.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
				        nameValuePairs.add(new BasicNameValuePair("password",password.getText().toString().trim())); 
				        nameValuePairs.add(new BasicNameValuePair("email",email.getText().toString().trim()));
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
		    	
		    	int i=Integer.parseInt(result);
		    	((MyAppVar)getApplicationContext().getApplicationContext()).setValue(result);
		    	
		    	SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		    	Editor editor = pref.edit();
		    	editor.putString("userid", result);
		    	editor.commit();
		        
		    	Intent in=new Intent(Register.this, AllQuotes.class);
				
				startActivity(in);
				finish();
		    }
		    catch(Exception e)
		    {
		    	if(result.equalsIgnoreCase("no"))
		    	{
		    	Toast.makeText(getApplicationContext(), "Wrong Username and Password", Toast.LENGTH_SHORT).show();
		    	 System.out.println("user : no");
		    	}
		    	else
		    	{
		    		
		    		uname.setFocusable(false);
		    		uname.setText(result.trim());
		    		upload.setVisibility(View.INVISIBLE);
		    		JSONObject object=null; 
		    		try {
						object = new JSONObject(result);
						((MyAppVar)getApplicationContext().getApplicationContext()).setValue(object.getString("userid"));
				    	
				    	SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
				    	Editor editor = pref.edit();
				    	editor.putString("userid", object.getString("userid"));
				    	editor.commit();
				    	
				    	
				    	
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    		
		    		Toast.makeText(getApplicationContext(), "You are already Registered", Toast.LENGTH_LONG).show();
		    		Intent i=new Intent(Register.this,AllQuotes.class);
		    		startActivity(i);
		    		finish();
		    		
		    	}
		    }
		    
		     
			}
        }
}

class UserEmailFetcher {

	  static String getEmail(Context context) {
	    AccountManager accountManager = AccountManager.get(context); 
	    Account account = getAccount(accountManager);

	    if (account == null) {
	      return null;
	    } else {
	      return account.name;
	    }
	  }

	  private static Account getAccount(AccountManager accountManager) {
	    Account[] accounts = accountManager.getAccountsByType("com.google");
	    Account account;
	    if (accounts.length > 0) {
	      account = accounts[0];      
	    } else {
	      account = null;
	    }
	    return account;
	  }
	}

