package com.plantalot.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.plantalot.R;


public class MainActivity extends AppCompatActivity {

	public boolean drawer_visible = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.topAppBar);
		View drawer = findViewById(R.id.navigationDrawer);
		drawer.setVisibility(View.GONE);

		setSupportActionBar(toolbar);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (drawer_visible) {
					drawer.setVisibility(View.GONE);
				} else {
					drawer.setVisibility(View.VISIBLE);
				}
				drawer_visible = !drawer_visible;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.top_app_bar, menu);
		return true;
	}
}
