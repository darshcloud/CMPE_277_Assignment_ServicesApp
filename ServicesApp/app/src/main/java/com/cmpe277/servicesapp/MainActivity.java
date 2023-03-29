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

public class MainActivity extends Activity {

	IntentFilter intentFilter;
	private MyService serviceBinder;
	Intent i;
	Button btnDownload;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

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
				try {
					URL[] urls = new URL[] {
							new URL("https://static.googleusercontent.com/media/research.google.com/en//pubs/archive/45530.pdf"),
							new URL("https://hadoop.apache.org/docs/r1.2.1/hdfs_design.pdf"),
							new URL("https://pages.databricks.com/rs/094-YMS-629/images/LearningSpark2.0.pdf"),
							new URL("https://docs.aws.amazon.com/wellarchitected/latest/machine-learning-lens/wellarchitected-machine-learning-lens.pdf"),
							new URL("https://developers.snowflake.com/wp-content/uploads/2020/09/SNO-eBook-7-Reference-Architectures-for-Application-Builders-MachineLearning-DataScience.pdf")
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