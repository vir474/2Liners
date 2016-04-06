package com.viral.twoliners.app;

import java.util.Vector;

import android.app.Application;

public class MyAppVar extends Application {
	
	private String appvar;
	private Vector v;
	private int fbpostid;

public String getValue(){
	return this.appvar;
 
	}
public Vector getList(){
	return this.v;
}
public void setValue(String b){
	this.appvar = b;
	}

public void setList(Vector v){
	this.v = v;
	}

public int getfbpostid()
{
	return this.fbpostid;
}

public void setfbpostid(int fbpostid)
{
	this.fbpostid=fbpostid;
}
}
