package com.viral.twoliners.app;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
//import com.example.phptoandroid.*;

public class Spinnermple extends Activity {
	
	private Spinner sp;
	String[] objects; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spinnerxmple);
		
		objects=new String[5];
		for(int i=0;i<5;i++)
		{
			objects[i]="String " + i;
		}
		
		sp=(Spinner)findViewById(R.id.spinner1);
		
		sp.setAdapter(new SpinnerAdapter() {
			
			@Override
			public void unregisterDataSetObserver(DataSetObserver arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void registerDataSetObserver(DataSetObserver arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public int getViewTypeCount() {
				// TODO Auto-generated method stub
				return objects.length;
			}
			
		
			
			@Override
			public int getItemViewType(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return objects.length;
			}
			
			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				// TODO Auto-generated method stub
				View vi=convertView;
				if(convertView==null)
					vi = inflater.inflate(R.layout.listitem, null);

				TextView text=(TextView)vi.findViewById(R.id.quotetext);
				text.setText(objects[position]);
				
				return vi;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup arg2) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				// TODO Auto-generated method stub
				View vi=convertView;
				if(convertView==null)
					vi = inflater.inflate(R.layout.listitem, null);

				TextView text=(TextView)vi.findViewById(R.id.quotetext);
				text.setText(objects[position]);
				
				return vi;
			}
		});
	}

	
}
