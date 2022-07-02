package com.plantalot.navigation;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;

import com.plantalot.R;
import com.plantalot.database.DbPlants;


public class Nav {
	public static void gotoOrtaggio(String ortaggio, int prev_fragment, View view) {  // fixme best practice ?
		if (DbPlants.ortaggiNames.contains(ortaggio)) {
			Bundle bundle = new Bundle();
			bundle.putString("ortaggio", ortaggio);
			bundle.putInt("prev_fragment", prev_fragment);
			Navigation.findNavController(view).navigate(R.id.action_goto_ortaggio, bundle);
		}
	}
}
