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
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
//import com.example.phptoandroid.*;

public class JSONExampleActivity extends Activity {
private ProgressBar pb;

	private ArrayAdapter<String> arrayAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		
		String ip=String.format("%d.%d.%d.%d", 
				(ipAddress & 0xff), 
				(ipAddress >> 8 & 0xff), 
				(ipAddress >> 16 & 0xff),
				(ipAddress >> 24 & 0xff));
		Log.i("IPaddress",ip);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://192.168.1.13/testandroid.php");
		ListView lv = (ListView) findViewById(R.id.listView1);
		pb=(ProgressBar)findViewById(R.id.progressBar1);
		
		try {

			HttpResponse response = httpclient.execute(httppost);
			String jsonResult = inputStreamToString(
					response.getEntity().getContent()).toString();
			Log.e("sgf", jsonResult);
			System.out.println(jsonResult);
			JSONObject object = new JSONObject(jsonResult);
			
			
			//Log.i("json value", object.toString());
			// /JSONArray objects=object.getJSONArray("data");
			final int len = object.length();

			arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
					R.layout.listitem, R.id.quotetext);
			for (int i = 0; i < len; i++) {
				arrayAdapter.add(object.getString("name" + i));
			}
			
			
			
			pb.setVisibility(View.INVISIBLE);
			
			
			lv.setAdapter(arrayAdapter);

			// final String names[]=new String[len];
			//
			// for(int i=0;i < len;i++)
			// {
			// names[i]=object.getString("name"+i);
			// } 
			//
			// // textView.setText(name);
			// lv.setAdapter(new ListAdapter() {
			//
			// @Override
			// public void unregisterDataSetObserver(DataSetObserver arg0) {
			// // TODO Auto-generated method stub
			//
			// }
			//
			// @Override
			// public void registerDataSetObserver(DataSetObserver arg0) {
			// // TODO Auto-generated method stub
			//
			// }
			//
			// @Override
			// public boolean isEmpty() {
			// // TODO Auto-generated method stub
			// return false;
			// }
			//
			// @Override
			// public boolean hasStableIds() {
			// // TODO Auto-generated method stub
			// return false;
			// }
			//
			// @Override
			// public int getViewTypeCount() {
			// // TODO Auto-generated method stub
			// return len;
			// }
			//
			// @Override
			// public View getView(int position, View arg1, ViewGroup arg2) {
			// // TODO Auto-generated method stub
			// LayoutInflater
			// inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// View inflatedView = inflater.inflate(R.layout.listitem, null);
			// TextView t1=(TextView)inflatedView.findViewById(R.id.textView2);
			// t1.setText(names[position]);
			// t1.setTextColor(Color.WHITE);
			// final int p=position;
			// inflatedView.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// Toast.makeText(getApplicationContext(), names[p],
			// Toast.LENGTH_SHORT).show();
			// }
			// });
			// return inflatedView;
			// }
			//
			// @Override
			// public int getItemViewType(int arg0) {
			// // TODO Auto-generated method stub
			// return 0;
			// }
			//
			// @Override
			// public long getItemId(int arg0) {
			// // TODO Auto-generated method stub
			// return 0;
			// }
			//
			// @Override
			// public Object getItem(int arg0) {
			// // TODO Auto-generated method stub
			// return null;
			// }
			//
			// @Override
			// public int getCount() {
			// // TODO Auto-generated method stub
			// return len;
			// }
			//
			// @Override
			// public boolean isEnabled(int arg0) {
			// // TODO Auto-generated method stub
			// return false;
			// }
			//
			// @Override
			// public boolean areAllItemsEnabled() {
			// // TODO Auto-generated method stub
			// return false;
			// }
			// });
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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