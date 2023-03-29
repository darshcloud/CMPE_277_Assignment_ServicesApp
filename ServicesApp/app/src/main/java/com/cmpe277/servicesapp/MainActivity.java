package com.cmpe277.servicesapp;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;

public class MainActivity extends Activity {

	IntentFilter intentFilter;
	private MyService serviceBinder;
	Intent i;
	Button btnDownload;
	EditText editTextPDF1, editTextPDF2, editTextPDF3, editTextPDF4, editTextPDF5;
	String url1, url2, url3, url4, url5;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		editTextPDF1 = (EditText) findViewById(R.id.enterpdf1);
		editTextPDF2 = (EditText) findViewById(R.id.enterpdf2);
		editTextPDF3 = (EditText) findViewById(R.id.enterpdf3);
		editTextPDF4 = (EditText) findViewById(R.id.enterpdf4);
		editTextPDF5 = (EditText) findViewById(R.id.enterpdf5);

		//---intent to filter for file downloaded intent---
		intentFilter = new IntentFilter();
		intentFilter.addAction("FILE_DOWNLOADED_ACTION");

		//---register the receiver---
		registerReceiver(intentReceiver, intentFilter);

		btnDownload = (Button) findViewById(R.id.StartDownload);
		btnDownload.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// DEMO 1
				Intent intent = new Intent(getBaseContext(), MyService.class);
				url1 = editTextPDF1.getText().toString();
				url2 = editTextPDF2.getText().toString();
				url3 = editTextPDF3.getText().toString();
				url4 = editTextPDF4.getText().toString();
				url5 = editTextPDF5.getText().toString();
				try {
					URL[] urls = new URL[] {
							new URL(url1),
							new URL(url2),
							new URL(url3),
							new URL(url4),
							new URL(url5)
					};
					intent.putExtra("URLs", urls);


				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startService(intent);

			}
		});

	}

	private ServiceConnection connection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			//---called when the connection is made---
			serviceBinder = ((MyService.MyBinder)service).getService();

			try {
				URL[] urls = new URL[] {
						new URL("http://www.amazon.com/somefiles.pdf"),
						new URL("http://www.wrox.com/somefiles.pdf"),
						new URL("http://www.google.com/somefiles.pdf"),
						new URL("http://www.learn2develop.net/somefiles.pdf")};
				serviceBinder.urls = urls;

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			startService(i);
		}
		public void onServiceDisconnected(ComponentName className) {
			//---called when the service disconnects---
			serviceBinder = null;
		}
	};

	private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(getBaseContext(), "File downloaded!",
					Toast.LENGTH_LONG).show();
		}
	};
}