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
<<<<<<< HEAD
//		Db.init(this);
=======
		Db.init(this);
>>>>>>> 1d6e065b4633e9abbdc6d4fa44fb7a1bdf38a59f
	}
}
