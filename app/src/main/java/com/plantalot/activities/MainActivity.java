package com.plantalot.activities;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.plantalot.R;
import com.plantalot.database.DbPlants;
import com.plantalot.database.DbUsers;

public class MainActivity extends AppCompatActivity {
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	}
}
