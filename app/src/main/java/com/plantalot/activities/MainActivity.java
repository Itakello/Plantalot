package com.plantalot.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.plantalot.R;
import com.plantalot.database.Db;

public class MainActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
//		Db.init(this);
	}
}
