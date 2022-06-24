package com.plantalot.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.plantalot.R;
import com.plantalot.adapters.CarriolaOrtaggiAdapter;
import com.plantalot.adapters.OrtaggioCardListAdapter;
import com.plantalot.adapters.OrtaggioSpecsAdapter;
import com.plantalot.components.CircleButton;
import com.plantalot.components.OrtaggioSpecs;
import com.plantalot.database.Db;
import com.plantalot.utils.ColorUtils;
import com.plantalot.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class CarriolaFragment extends Fragment {
	
	private View view;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.carriola_fragment, container, false);
		setupContent();
		return view;
	}
	
	private void setupContent() {
		// FIXME
//		HashMap<String, HashSet<String>> ortaggi = new HashMap<>();
		List<Pair<String, List<String>>> ortaggi = new ArrayList<>(Arrays.asList(
				new Pair<>("Barbabietola", new ArrayList<>(Arrays.asList("Detroit"))),
				new Pair<>("Peperoncino", new ArrayList<>(Arrays.asList("Fatalii", "Habanero"))),
				new Pair<>("Aglio", new ArrayList<>(Arrays.asList("Rosso"))),
				new Pair<>("Zucca", new ArrayList<>(Arrays.asList("Marina"))),
				new Pair<>("Bietola", new ArrayList<>(Arrays.asList("Multicolor")))
		));
		
		RecyclerView ortaggiRecyclerView = view.findViewById(R.id.carriola_ortaggi_recycler);
		ortaggiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		CarriolaOrtaggiAdapter carriolaOrtaggiAdapter = new CarriolaOrtaggiAdapter(ortaggi);
		ortaggiRecyclerView.setAdapter(carriolaOrtaggiAdapter);
	}
	
	private void setupToolbar(HashMap<String, HashMap<String, Object>> ortaggio, HashMap<String, Object> pianta) {
//		MaterialToolbar toolbar = view.findViewById(R.id.ortaggio_fl_toolbar);
//		AppCompatActivity activity = (AppCompatActivity) getActivity();
//		assert activity != null;
//		activity.setSupportActionBar(toolbar);
//		final ActionBar actionBar = activity.getSupportActionBar();
//		if (actionBar != null) {
//			actionBar.setDisplayHomeAsUpEnabled(true);
//		}

//		toolbar.setNavigationOnClickListener(view -> {
//			int prev_frag_id = getArguments().getInt("prev_fragment");
//			NavController navController = Navigation.findNavController(view);
//			navController.popBackStack(prev_frag_id, false);
//		});
	}
	
}
