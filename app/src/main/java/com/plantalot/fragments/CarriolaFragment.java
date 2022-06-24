package com.plantalot.fragments;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.plantalot.R;
import com.plantalot.adapters.CarriolaOrtaggiAdapter;
import com.plantalot.classes.User;
import com.plantalot.database.Db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
		setupToolbar();
		return view;
	}
	
	private void setupContent() {
		List<Pair<String, List<Pair<HashMap<String, Object>, Integer>>>> carriola = new ArrayList<>();
		List<String> ortaggi = new ArrayList<>(User.carriola.keySet());
		Collections.sort(ortaggi);
		for (String ortaggio : ortaggi) {
			List<Pair<HashMap<String, Object>, Integer>> carriolaVarieta = new ArrayList<>();
			List<String> varietas = new ArrayList<>(User.carriola.get(ortaggio).keySet());
			Collections.sort(varietas);
			for (String varieta : varietas) {
				carriolaVarieta.add(new Pair<>(Db.varieta.get(ortaggio).get(varieta), User.carriola.get(ortaggio).get(varieta)));
			}
			carriola.add(new Pair<>(ortaggio, carriolaVarieta));
		}
		
		RecyclerView ortaggiRecyclerView = view.findViewById(R.id.carriola_ortaggi_recycler);
		ortaggiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		CarriolaOrtaggiAdapter carriolaOrtaggiAdapter = new CarriolaOrtaggiAdapter(carriola);
		ortaggiRecyclerView.setAdapter(carriolaOrtaggiAdapter);
	}
	
	private void setupToolbar() {
		MaterialToolbar toolbar = view.findViewById(R.id.carriola_toolbar);
		toolbar.setNavigationOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_goto_home));
	}
	
}
