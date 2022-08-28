package com.plantalot.navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.plantalot.R;
import com.plantalot.database.DbPlants;


public class Nav {
	public static void gotoOrtaggio(String ortaggio, int prev_fragment, Context context, View view) {  // fixme best practice ?
		if (DbPlants.getOrtaggiNames().contains(ortaggio)) {
			Bundle bundle = new Bundle();
			bundle.putString("ortaggio", ortaggio);
			bundle.putInt("prev_fragment", prev_fragment);
			Navigation.findNavController(view).navigate(R.id.action_goto_ortaggio, bundle);
		} else {
			Toast.makeText(context, R.string.scheda_ortaggio_non_disponibile, Toast.LENGTH_SHORT).show();
		}
	}
}
