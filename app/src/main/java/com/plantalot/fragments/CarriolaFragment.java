package com.plantalot.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.plantalot.MyApplication;
import com.plantalot.R;
import com.plantalot.adapters.CarriolaOrtaggiAdapter;
import com.plantalot.classes.Carriola;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.Orto;
import com.plantalot.classes.Varieta;
import com.plantalot.database.DbUsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class CarriolaFragment extends Fragment {
	
	private View view;
	private Giardino giardino;
	private Carriola carriola;
	private int totalArea, plantedArea, carriolaArea;
	
	private TextView areaValuesTv;
	private Button confirmBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		giardino = ((MyApplication) this.getActivity().getApplication()).user.getGiardinoCorrente();
		carriola = giardino.getCarriola();
		
		totalArea = giardino.calcArea();
		plantedArea = giardino.plantedArea();
		carriolaArea = carriola.calcArea();
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.carriola_fragment, container, false);
		areaValuesTv = view.findViewById(R.id.carriola_area_values);
		confirmBtn=	view.findViewById(R.id.carriola_confirm_btn);
		setupToolbar();
		updateOccupiedArea();
		if (carriola.notEmpty()) {
			confirmBtn.setVisibility(View.VISIBLE);
			view.findViewById(R.id.carriola_progressBar).setVisibility(View.VISIBLE);
			view.findViewById(R.id.carriola_text_vuota).setVisibility(View.GONE);
			setupContent(carriola.toList());
		} else {
			confirmBtn.setVisibility(View.GONE);
		}
		return view;
	}
	
	private static String format(int area) {
		return (area / 10000) + "," + Math.abs(area / 1000 - 10 * (area / 10000)) + " m²";
	}
	
	public void updateOccupiedArea() {
		updateOccupiedArea(0);
	}
	
	public void updateOccupiedArea(int update) {
		carriolaArea += update;
		int freeArea = totalArea - (plantedArea + carriolaArea);
		String text = ""
				+ format(totalArea) + "\n"
				+ format(plantedArea) + "\n"
				+ format(carriolaArea) + "\n"
				+ format(freeArea);
		areaValuesTv.setText(text);
	}
	
	private void setupContent(List<Pair<String, List<Pair<Varieta, Integer>>>> carriolaList) {
		Handler handler = new Handler();
		handler.post(() -> {
			view.findViewById(R.id.carriola_progressBar).setVisibility(View.GONE);
			RecyclerView ortaggiRecyclerView = view.findViewById(R.id.carriola_ortaggi_recycler);
			ortaggiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
			CarriolaOrtaggiAdapter carriolaOrtaggiAdapter = new CarriolaOrtaggiAdapter(carriolaList, giardino, this);
			ortaggiRecyclerView.setAdapter(carriolaOrtaggiAdapter);
			confirmBtn.setEnabled(giardino.getOrti().size() > 0);
		});
		
		confirmBtn.setOnClickListener(v -> {
			arrangeOrtaggi();
			carriola.clear();
			DbUsers.updateGiardino(giardino);
			Navigation.findNavController(view).navigate(R.id.action_goto_home_from_carriola);
		});
	}
	
	private void arrangeOrtaggi() {  // TODO
		Random rnd = new Random();
		HashMap<String, Orto> orti = giardino.getOrti();
		ArrayList<String> keys = new ArrayList<>(orti.keySet());
		for (String ortaggio : carriola.nomiOrtaggi()) {
			for (String varieta : carriola.nomiVarieta(ortaggio)) {
				int count = carriola.getPianteCount(ortaggio, varieta);
				if (count > 0) {
					int r = rnd.nextInt(orti.size());
					orti.get(keys.get(r)).addVarieta(ortaggio, varieta, count);
				}
			}
		}
		giardino.setOrti(orti);
		DbUsers.updateGiardino(giardino);
	}
	
	private void setupToolbar() {
		MaterialToolbar toolbar = view.findViewById(R.id.carriola_toolbar);
		toolbar.setNavigationOnClickListener(view -> Navigation.findNavController(view).popBackStack());
	}
	
}
