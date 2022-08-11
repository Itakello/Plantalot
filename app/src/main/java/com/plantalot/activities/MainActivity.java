package com.plantalot.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.plantalot.R;
import com.plantalot.database.DbPlants;
import com.plantalot.database.DbUsers;

public class MainActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		DbPlants.init(this);
		DbUsers.init();
	}
}
