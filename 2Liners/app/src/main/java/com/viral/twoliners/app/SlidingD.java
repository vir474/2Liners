package com.viral.twoliners.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//import com.example.phptoandroid.*;

public class SlidingD extends Activity {
	private Button handle;
	private Button user;
	private Button all;
	private Button newquote;
	private Button yourfav;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidingd);
		
		handle=(Button)findViewById(R.id.handle);
		user=(Button)findViewById(R.id.likebtn);
		all=(Button)findViewById(R.id.dislikebtn);
		newquote=(Button)findViewById(R.id.button3);
		yourfav=(Button)findViewById(R.id.button4);
		
user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in=new Intent(SlidingD.this, QuotesByUser.class);
				
				startActivity(in);
			}
		});
		
		all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in=new Intent(SlidingD.this, AllQuotes.class);
				
				startActivity(in);
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			}
		});
		
		newquote.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in=new Intent(SlidingD.this, NewQuote.class);
				
				startActivity(in);
			}
		});
		
		yourfav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in=new Intent(SlidingD.this, YourFavourite.class);
				
				startActivity(in);
			}
		});
		
	}

	
}
