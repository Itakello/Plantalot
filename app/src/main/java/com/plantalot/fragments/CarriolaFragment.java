package com.plantalot.fragments;

import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.plantalot.R;
import com.plantalot.adapters.CarriolaOrtaggiAdapter;
import com.plantalot.classes.Carriola;
import com.plantalot.classes.Varieta;
import com.plantalot.database.DbPlants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class CarriolaFragment extends Fragment {
	
	private View view;
	private Carriola carriola;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		carriola = HomeFragment.user.getGiardinoCorrente().getCarriola();
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.carriola_fragment, container, false);
		setupToolbar();
		if (!carriola.isEmpty()) fetchDb();
		return view;
	}
	
	private void fetchDb() {
		view.findViewById(R.id.carriola_progressBar).setVisibility(View.VISIBLE);
		view.findViewById(R.id.carriola_text_vuota).setVisibility(View.GONE);
		List<Pair<String, List<Pair<Varieta, Integer>>>> carriolaList = new ArrayList<>();
		List<String> ortaggi = carriola.nomiOrtaggi();
		Collections.sort(ortaggi);
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		AtomicInteger counter = new AtomicInteger();
		for (String ortaggio : ortaggi) {
			List<Pair<Varieta, Integer>> carriolaVarieta = new ArrayList<>();
			List<String> varietas = carriola.nomiVarieta(ortaggio);
			Collections.sort(varietas);
			db.collection("varieta")
					.whereEqualTo(DbPlants.VARIETA_CLASSIFICAZIONE_ORTAGGIO, ortaggio)
					.whereIn(DbPlants.VARIETA_CLASSIFICAZIONE_VARIETA, varietas)
					.get().addOnSuccessListener(queryDocumentSnapshots -> {
						for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
							Varieta varietaObj = document.toObject(Varieta.class);
							carriolaVarieta.add(new Pair<>(
									varietaObj,
									carriola.get(ortaggio,varietaObj.getClassificazione_varieta()))
							);
						}
						carriolaList.add(new Pair<>(ortaggio, carriolaVarieta));
						
						// FIXME !!?
						if (counter.incrementAndGet() == ortaggi.size()) setupContent(carriolaList);
					});
		}
	}
	
	private void setupContent(List<Pair<String, List<Pair<Varieta, Integer>>>> carriolaList) {
		Handler handler = new Handler();
		handler.post(() -> {
			view.findViewById(R.id.carriola_progressBar).setVisibility(View.GONE);
			RecyclerView ortaggiRecyclerView = view.findViewById(R.id.carriola_ortaggi_recycler);
			ortaggiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
			CarriolaOrtaggiAdapter carriolaOrtaggiAdapter = new CarriolaOrtaggiAdapter(carriolaList, carriola);
			ortaggiRecyclerView.setAdapter(carriolaOrtaggiAdapter);
		});
	}
	
	private void setupToolbar() {
		MaterialToolbar toolbar = view.findViewById(R.id.carriola_toolbar);
		toolbar.setNavigationOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_goto_home));
	}
	
}
