package com.viral.twoliners.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//import com.example.phptoandroid.*;
public class Home extends Activity {
	private Button user;
	private Button all;
	private Button newquote;
	private Button yourfav;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		user=(Button)findViewById(R.id.likebtn);
		all=(Button)findViewById(R.id.dislikebtn);
		newquote=(Button)findViewById(R.id.button3);
		yourfav=(Button)findViewById(R.id.button4);
		
		user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in=new Intent(Home.this, QuotesByUser.class);
				
				startActivity(in);
			}
		});
		
		all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in=new Intent(Home.this, AllQuotes.class);
				
				startActivity(in);
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			}
		});
		
		newquote.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in=new Intent(Home.this, NewQuote.class);
				
				startActivity(in);
			}
		});
		
		yourfav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in=new Intent(Home.this, YourFavourite.class);
				
				startActivity(in);
			}
		});
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
		}
		return false;
	}

}
