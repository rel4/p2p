package com.example.p2p;

import com.limaoso.phonevideo.p2p.P2PConfig;
import com.limaoso.phonevideo.p2p.P2PManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class Main1Activity extends Activity {
	// String uri = "limaoso://btih:0059f8b3f7454c8ad8fa49024310d74afd5119f7";

	String uri = "limaoso://btih:157e4baedecafae969023a827d12744689a9e4ce";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		P2PConfig.getInstance().init(
				getApplicationContext());
		findViewById(R.id.bt).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				P2PManager.getInstance(getApplicationContext()).cacheP2PFile(
						uri);

			}
		});

	}

	@Override
	protected void onStop() {
		super.onStop();
		P2PManager.getInstance(getApplicationContext()).deleteFile(uri);
	}
}
