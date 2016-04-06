package com.viral.twoliners.app;


import java.util.Vector;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import facebook.android.AsyncFacebookRunner;
import facebook.android.BaseRequestListener;
import facebook.android.DialogError;
import facebook.android.Facebook;
import facebook.android.Facebook.DialogListener;
import facebook.android.FacebookError;
import facebook.android.SessionStore;
//import com.example.phptoandroid.*;

/**
 * This example shows how to connect to Facebook, display authorization dialog
 * and save user's access token and username.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 *         http://www.londatiga.net
 */

public class ShareAppOnFB extends Activity {
	private Facebook mFacebook;
	private CheckBox mFacebookBtn;
	private ProgressDialog mProgress;
	private Button btnContinue;
	private static final String[] PERMISSIONS = new String[] {
			"publish_stream", "read_stream", "offline_access" };

	private static final String APP_ID = "446781738736433";

	private Handler mRunOnUi = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			requestWindowFeature(Window.FEATURE_NO_TITLE);

			setContentView(R.layout.facebookmain);

			mFacebookBtn = (CheckBox) findViewById(R.id.cb_facebook);
			btnContinue = (Button) findViewById(R.id.button1);

			mProgress = new ProgressDialog(this);
			mFacebook = new Facebook(APP_ID);

			SessionStore.restore(mFacebook, this);

			if (mFacebook.isSessionValid()) {
				mFacebookBtn.setChecked(true);

				String name = SessionStore.getName(this);
				name = (name.equals("")) ? "Unknown" : name;

				Log.e("FB name:", name);

				mFacebookBtn.setText("  Facebook (" + name + ")");

//				btnContinue.setVisibility(View.VISIBLE);

				

			} else {
				Toast.makeText(
						getApplicationContext(),
						"Your facebook account is not connected.\n To connect with facebook please press on facebook.",
						Toast.LENGTH_LONG).show();
				btnContinue.setVisibility(View.INVISIBLE);
			}

			mFacebookBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onFacebookClick();
				}
			});

			btnContinue.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mFacebookBtn.isChecked()) {

						// Intent i = new Intent(ShareAppOnFB.this,
						// AppPost.class);
						// finish();
						// startActivity(i);
						int postid=((MyAppVar)getApplicationContext().getApplicationContext()).getfbpostid();
						Vector list=((MyAppVar)getApplicationContext().getApplicationContext()).getList();
						postToFacebook(((QuoteDetails)list.elementAt(postid)).quote);

					} else {
						Toast.makeText(
								getApplicationContext(),
								"your facebook account is not connected. press on facebook.",
								Toast.LENGTH_LONG).show();
					}
				}
			});
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Error: " + e.toString(),
					Toast.LENGTH_LONG).show();
		}

	}

	private void postToFacebook(String review) {
		mProgress.setMessage("Posting ...");
		mProgress.show();

		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(mFacebook);

		Bundle params = new Bundle();

		params.putString("message", review);
		params.putString("name", "2Liners");
		params.putString("caption", "from 2liners");
/*		params.putString("link",
				"redirection page");
		params.putString("description", "2Liners Discription");
		params.putString(
				"picture",
				"Thumbnail Path to display");
*/
		mAsyncFbRunner.request("me/feed", params, "POST",
				new WallPostListener());

	}

	private final class WallPostListener extends BaseRequestListener {
		public void onComplete(final String response) {
			mRunOnUi.post(new Runnable() {
				@Override
				public void run() {
					mProgress.cancel();

					Toast.makeText(ShareAppOnFB.this,
							"Sucessfully Posted to Facebook",
							Toast.LENGTH_SHORT).show();
					/*Intent i = new Intent(ShareAppOnFB.this,
							MainActivity.class);*/
					finish();
					//startActivity(i);
				}
			});
		}
	}

	private void onFacebookClick() {
		if (mFacebook.isSessionValid()) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage("Delete current Facebook connection?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									fbLogout();

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();

									mFacebookBtn.setChecked(true);
								}
							});

			final AlertDialog alert = builder.create();

			alert.show();
		} else {
			mFacebookBtn.setChecked(false);

			mFacebook.authorize(this, PERMISSIONS, -1,
					new FbLoginDialogListener());
		}
	}

	private final class FbLoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			SessionStore.save(mFacebook, ShareAppOnFB.this);

			mFacebookBtn.setText("  Facebook (No Name)");
			mFacebookBtn.setChecked(true);
			// mFacebookBtn.setTextColor(Color.WHITE);

			getFbName();
		}

		public void onFacebookError(FacebookError error) {
			Toast.makeText(ShareAppOnFB.this, "Facebook connection failed",
					Toast.LENGTH_SHORT).show();

			mFacebookBtn.setChecked(false);
		}

		public void onError(DialogError error) {
			Toast.makeText(ShareAppOnFB.this, "Facebook connection failed",
					Toast.LENGTH_SHORT).show();

			mFacebookBtn.setChecked(false);
		}

		public void onCancel() {
			mFacebookBtn.setChecked(false);
		}
	}

	private void getFbName() {
		mProgress.setMessage("Finalizing ...");
		mProgress.show();

		new Thread() {
			@Override
			public void run() {
				String name = "";
				int what = 1;

				try {
					String me = mFacebook.request("me");

					JSONObject jsonObj = (JSONObject) new JSONTokener(me)
							.nextValue();
					name = jsonObj.getString("name");
					what = 0;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				mFbHandler.sendMessage(mFbHandler.obtainMessage(what, name));
			}
		}.start();
	}

	private void fbLogout() {
		mProgress.setMessage("Disconnecting from Facebook");
		mProgress.show();

		new Thread() {
			@Override
			public void run() {
				SessionStore.clear(ShareAppOnFB.this);

				int what = 1;

				try {
					mFacebook.logout(ShareAppOnFB.this);

					what = 0;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what));
			}
		}.start();
	}

	private Handler mFbHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();

			if (msg.what == 0) {
				String username = (String) msg.obj;
				username = (username.equals("")) ? "No Name" : username;

				SessionStore.saveName(username, ShareAppOnFB.this);

				mFacebookBtn.setText("  Facebook (" + username + ")");

				// Intent i = new Intent(ShareAppOnFB.this, AppPost.class);
				// finish();
				// startActivity(i);

				btnContinue.setVisibility(View.VISIBLE);

				Toast.makeText(ShareAppOnFB.this,
						"Connected to Facebook as " + username,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ShareAppOnFB.this, "Connected to Facebook",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();

			if (msg.what == 1) {
				Toast.makeText(ShareAppOnFB.this, "Facebook logout failed",
						Toast.LENGTH_SHORT).show();
			} else {
				mFacebookBtn.setChecked(false);
				mFacebookBtn.setText("  Facebook (Not connected)");
				// mFacebookBtn.setTextColor(Color.GRAY);
			//	Intent i = new Intent(ShareAppOnFB.this, MainActivity.class);
				finish();
			//	startActivity(i);

				Toast.makeText(ShareAppOnFB.this, "Disconnected from Facebook",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
}