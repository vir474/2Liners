package com.viral.twoliners.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

//import com.example.phptoandroid.*;

import com.viral.twoliners.app.Asyncphp.LongOperation;

public class LazyAdapter extends BaseAdapter {

	Activity activity;
	// private String[] data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	JSONObject object;
	
	String flag;
	String act;
	private List<NameValuePair> nameValuePairs;
	public int currentquote, c;
	int increment = 0;
	Vector v;
	View vi;
	int i;
	int j;
	String deletequoteid; 
	Dialog myDialog;
	float rating;

	public LazyAdapter(Activity a, Vector o) {
		activity = a;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
		v = o;
		((MyAppVar) activity.getApplicationContext().getApplicationContext())
				.setList(v);

	}

	@Override
	public int getCount() {
		return v.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		vi = convertView;

		if (convertView == null)
			vi = inflater.inflate(R.layout.listitem, null);

		TextView quote = (TextView) vi.findViewById(R.id.quotetext);
		TextView quotetime = (TextView) vi.findViewById(R.id.quotetime);
		TextView uname = (TextView) vi.findViewById(R.id.quoteuname);
		final TextView like = (TextView) vi.findViewById(R.id.likes);
		final TextView dislike = (TextView) vi.findViewById(R.id.dislikes);
		final RatingBar rb = (RatingBar) vi.findViewById(R.id.ratingBar);
		final TextView ratingcount = (TextView) vi.findViewById(R.id.ratingcount);
		ImageView image = (ImageView) vi.findViewById(R.id.quoteimg);

		uname.setText(((QuoteDetails) v.elementAt(position)).username);
		quote.setText(((QuoteDetails) v.elementAt(position)).quote);
		quotetime.setText(((QuoteDetails) v.elementAt(position)).time);
		like.setText(((QuoteDetails) v.elementAt(position)).likes + " likes");
		dislike.setText(((QuoteDetails) v.elementAt(position)).dislikes
				+ " dislikes");
		rb.setRating(Float.parseFloat(((QuoteDetails) v.elementAt(position)).rating));
		ratingcount.setText(((QuoteDetails) v.elementAt(position)).ratingcount);
		// Log.e("trupal", ((QuoteDetails)v.elementAt(position)).quote);
		imageLoader.DisplayImage("http://10.0.2.2/android/images/"
				+ ((QuoteDetails) v.elementAt(position)).image, image);

		final int p = position;
		 rating=Float.parseFloat(((QuoteDetails) v.elementAt(position)).rating);

			if (Integer.parseInt(((QuoteDetails) v.elementAt(position)).ratingflag) == 1) {
				rb.setEnabled(false);

			} else

			{
				rb.setEnabled(true);
				// likebtn.setBackgroundResource(R.drawable.like);
			}
		
				rb.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View vt, MotionEvent event) {
						// TODO Auto-generated method stub
						
						currentquote = p;
						myDialog = new Dialog(activity);
						myDialog.setContentView(R.layout.my_custom_dialog);
						myDialog.setTitle("Rate Quote");
						RatingBar rate = (RatingBar) myDialog
								.findViewById(R.id.dialogratingBar);
						myDialog.show();
						rating = 0;
						rate.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

							@Override
							public void onRatingChanged(RatingBar ratingBar,
									float rate, boolean fromUser) {
								// TODO Auto-generated method stub
								rating = rate;
							}
						});

						Button finishbtn = (Button) myDialog
								.findViewById(R.id.dialogcancel);
						finishbtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								myDialog.dismiss();

							}
						});
						Button okbtn = (Button) myDialog
								.findViewById(R.id.dialogok);
						okbtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View vi) {
								flag = "rating";
								float stars;
								if(rating!=0)
								{
							//	q = f1;
								int rc = Integer.parseInt(((QuoteDetails) v
										.elementAt(p)).ratingcount);
								float rate = Float.parseFloat(((QuoteDetails) v
										.elementAt(p)).rating);

								System.out.println("rating" + rate + "rate"
										+ rc);
								stars = (((rc * rate) + rating) / (rc + 1));
								rb.setRating(stars);
								ratingcount.setText("(" + (1 + rc) + ")");
								((QuoteDetails) v.elementAt(p)).rating = String
										.valueOf(stars);
								((QuoteDetails) v.elementAt(p)).ratingcount
								= String
										.valueOf(1 + rc);

								System.out.println("rate" + stars + "ck" + rc);
								
								new LikeDislikeQuote().execute();
								rb.setEnabled(false);
								}
								myDialog.cancel();
							}

						});
						
						return false;
					}
				});
		

		Button likebtn = (Button) vi.findViewById(R.id.likebtn);
		likebtn.setOnClickListener(new OnClickListener() {
			// int lcin,ctrl=0;
			@Override
			public void onClick(View likev) {
				// TODO Auto-generated method stub
				flag = "like";
				currentquote = p;
				i = (Integer.parseInt(((QuoteDetails) v.elementAt(currentquote)).likes));
				j = i;

				if (Integer.parseInt(((QuoteDetails) v.elementAt(currentquote)).like_flag) == 1) {
					j = (j - 1);
					((QuoteDetails) v.elementAt(currentquote)).like_flag = String
							.valueOf(0);
				} else

				{

					j = j + 1;
					((QuoteDetails) v.elementAt(currentquote)).like_flag = String
							.valueOf(1);

				}
				((QuoteDetails) v.elementAt(currentquote)).likes = String
						.valueOf(j);

				like.setText(j + " Likes");
				new LikeDislikeQuote().execute();
			}
		});

		Button dislikebtn = (Button) vi.findViewById(R.id.dislikebtn);
		dislikebtn.setOnClickListener(new OnClickListener() {
			// int dcin,ctrd=0;
			@Override
			public void onClick(View dislikev) {
				// TODO Auto-generated method stub
				currentquote = p;
				// if(ctrd==0)dcin=(Integer.parseInt(((QuoteDetails)v.elementAt(currentquote)).dislikes));
				flag = "dislike";

				// ctrd++;
				i = (Integer.parseInt(((QuoteDetails) v.elementAt(currentquote)).dislikes));
				j = i;

				if (Integer.parseInt(((QuoteDetails) v.elementAt(currentquote)).dislike_flag) == 1) {
					j = (j - 1);
					((QuoteDetails) v.elementAt(currentquote)).dislike_flag = String
							.valueOf(0);
				} else

				{

					j = j + 1;
					((QuoteDetails) v.elementAt(currentquote)).dislike_flag = String
							.valueOf(1);

				}
				((QuoteDetails) v.elementAt(currentquote)).dislikes = String
						.valueOf(j);

				dislike.setText(j + " dislikes");
				new LikeDislikeQuote().execute();

			}
		});

		Button delete = (Button) vi.findViewById(R.id.deletebtn);
		Log.i("appvar",
				Integer.parseInt(((QuoteDetails) v.elementAt(position)).userid)
						+ " "
						+ Integer.parseInt(((MyAppVar) activity
								.getApplicationContext()
								.getApplicationContext()).getValue()));
		if (Integer.parseInt(((QuoteDetails) v.elementAt(position)).userid) == Integer
				.parseInt(((MyAppVar) activity.getApplicationContext()
						.getApplicationContext()).getValue())) {
			delete.setVisibility(View.VISIBLE);
		} else
			delete.setVisibility(View.INVISIBLE);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View deletev) {
				// TODO Auto-generated method stub
				currentquote = p;
				flag = "delete";

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						activity);

				// Setting Dialog Title
				alertDialog.setTitle("Confirm Delete...");

				// Setting Dialog Message
				alertDialog
						.setMessage("Are you sure you want delete this post?");

				// Setting Icon to Dialog
				alertDialog.setIcon(R.drawable.delete_on);

				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to execute after dialog
								deletequoteid = ((QuoteDetails) v.elementAt(p)).quoteid;
								new LikeDislikeQuote().execute();

								v.remove(p);
								notifyDataSetChanged();

							}
						});
				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to execute after dialog

								dialog.cancel();
							}
						});

				// Showing Alert Message
				alertDialog.show();

			}
		});

		final ToggleButton favbtn = (ToggleButton) vi.findViewById(R.id.favbtn);
		String fav_flag = ((QuoteDetails) v.elementAt(position)).fav_flag;

		if (Integer.parseInt(((QuoteDetails) v.elementAt(position)).fav_flag) == 1) {
			favbtn.setChecked(true);

		} else

		{
			favbtn.setChecked(false);
			// likebtn.setBackgroundResource(R.drawable.like);
		}

		favbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				flag = "fav";

				currentquote = p;

				if (favbtn.isChecked()) {

					favbtn.setChecked(true);
					// likebtn.setBackgroundResource(R.drawable.likechn);
				} else

				{

					favbtn.setChecked(false);
					// likebtn.setBackgroundResource(R.drawable.like);
				}

				new LikeDislikeQuote().execute();

			}
		});

		Button fbshare = (Button) vi.findViewById(R.id.fbshare);
		fbshare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				currentquote = p;
				((MyAppVar) activity.getApplicationContext()
						.getApplicationContext()).setfbpostid(currentquote);
				Intent i = new Intent(activity, ShareAppOnFB.class);
				activity.startActivity(i);
			}
		});

		return vi;
	}

	private class LikeDislikeQuote extends AsyncTask<String, Void, Void> {

		private HttpClient httpclient;
		private HttpPost httppost;

		// private ProgressBar pb;

		@Override
		protected void onPreExecute() {
			// NOTE: You can call UI Element here.

			// UI Element
			// pb.setVisibility(View.VISIBLE);
			httpclient = new DefaultHttpClient();

			if (flag == "like") {
				httppost = new HttpPost(
						"http://10.0.2.2/android/likedislikequote.php?flag=l");
			}

			else if (flag == "dislike") {
				httppost = new HttpPost(
						"http://10.0.2.2/android/likedislikequote.php?flag=d");
			} else if (flag == "delete") {
				httppost = new HttpPost(
						"http://10.0.2.2/android/likedislikequote.php?flag=r");
			} else if (flag == "fav") {
				httppost = new HttpPost(
						"http://10.0.2.2/android/likedislikequote.php?flag=f");
			} else if (flag == "rating") {
				System.out.println(rating+"");
				httppost = new HttpPost(
						"http://10.0.2.2/android/likedislikequote.php?flag=ra");
			}
			nameValuePairs = new ArrayList<NameValuePair>(2);

			// Always use the same variable name for posting i.e the android
			// side variable name and php side variable name should be similar,
			nameValuePairs.add(new BasicNameValuePair("userid",
					((MyAppVar) activity.getApplicationContext()
							.getApplicationContext()).getValue()));// $Edittext_value
																	// =
																	// $_POST['Edittext_value'];

			nameValuePairs.add(new BasicNameValuePair("quoteid",
					((QuoteDetails) v.elementAt(currentquote)).quoteid));
			if (flag == "delete")
				nameValuePairs.add(new BasicNameValuePair("quoteid",
						deletequoteid));
			if (flag == "rating")
				nameValuePairs.add(new BasicNameValuePair("rating",
						String.valueOf(rating)));

			try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// Call after onPreExecute method
		@Override
		protected Void doInBackground(String... urls) {
			try {

				HttpResponse response = httpclient.execute(httppost);
				String jsonResult = inputStreamToString(
						response.getEntity().getContent()).toString();

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {

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