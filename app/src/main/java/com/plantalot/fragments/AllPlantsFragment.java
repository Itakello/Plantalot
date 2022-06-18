package com.plantalot.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.R;
import com.plantalot.adapters.AllPlantsDrawerAdapter;
import com.plantalot.adapters.OrtaggioCardListAdapter;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.User;
import com.plantalot.database.Db;
import com.plantalot.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class AllPlantsFragment extends Fragment {
	
	private Giardino giardino;
	private static User user;
	private View view;
	private final HashMap<String, Set<String>> activeFilters = new HashMap<>();
	private boolean backdropShown = false;
	DisplayMetrics displayMetrics = new DisplayMetrics();
	
	public final static String INFO_RAGGRUPPA = "Raggruppa";
	private final static String INFO_FAMIGLIA = "Famiglie";
	private final static String INFO_NOME = "Nome";
	private final static String INFO_TRAPIANTO = "Mesi per il trapianto";
	private final static String INFO_RACCOLTA = "Giorni per la raccolta";
	private final static String INFO_DIST_PIANTE = "Distanza fra piante";
	private final static String INFO_DIST_FILE = "Distanza fra file";
	private final static String INFO_DIMENSIONI = "Dimensioni";
	private final static String INFO_ESPOSIZIONE = "Esposizione";
	private final static String INFO_ROTAZIONE = "Anni di rotazione";
	private final static String INFO_PACK = "Piante per pack";
	
	private final static HashMap<String, List<Pair<Integer, Integer>>> groups = new HashMap<>();
	
	private final static List<Pair<String, List<String>>> chips = new ArrayList<>(Arrays.asList(
			new Pair<>(INFO_RAGGRUPPA, Arrays.asList(INFO_FAMIGLIA, INFO_NOME, INFO_RACCOLTA, INFO_DIST_PIANTE, INFO_DIST_FILE, INFO_PACK)),  /*, "Produzione", "Anni di rotazione", "Piante per pack"*/
			new Pair<>(INFO_FAMIGLIA, Db.famiglie),
			new Pair<>(INFO_TRAPIANTO, Arrays.asList("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "Questo mese")),
			new Pair<>(INFO_RACCOLTA, new ArrayList<>()),
			new Pair<>(INFO_DIMENSIONI, Arrays.asList("Piccole", "Medie", "Grandi")),
			new Pair<>(INFO_ESPOSIZIONE, Arrays.asList("Sole", "Mezz'ombra")),
			new Pair<>(INFO_PACK, new ArrayList<>())
//			new Pair<>(INFO_ROTAZIONE, Arrays.asList("2", "3", "4", "5", "7")),  // TODO
//			new Pair<>("Giardini e preferiti", Arrays.asList("Tutto", "", "Preferiti")),  // TODO
	));
	
	static {
		groups.put(Db.VARIETA_DISTANZE_PIANTE, new ArrayList<>(Arrays.asList(
				new Pair<>(5, 10),
				new Pair<>(15, 20),
				new Pair<>(25, 30),
				new Pair<>(40, 45),
				new Pair<>(70, 100)
		)));
		groups.put(Db.VARIETA_DISTANZE_FILE, new ArrayList<>(Arrays.asList(
				new Pair<>(15, 30),
				new Pair<>(35, 40),
				new Pair<>(45, 50),
				new Pair<>(100, 200)
		)));
		groups.put(Db.VARIETA_RACCOLTA_AVG, new ArrayList<>(Arrays.asList(
				new Pair<>(10, 40),
				new Pair<>(45, 60),
				new Pair<>(65, 80),
				new Pair<>(85, 95),
				new Pair<>(100, 150)
		)));
		groups.put(Db.VARIETA_ALTRO_PACK, new ArrayList<>(Arrays.asList(
				new Pair<>(1, 1),
				new Pair<>(4, 4),
				new Pair<>(6, 6),
				new Pair<>(9, 9),
				new Pair<>(12, 12),
				new Pair<>(16, 16)
		)));
		
		for (Pair<String, List<String>> chip : chips) {
			switch (chip.first) {
				case INFO_PACK:
					for (Pair<Integer, Integer> pair : groups.get(Db.VARIETA_ALTRO_PACK)) {
						chip.second.add(pair.first.toString());
					}
					break;
				case INFO_RACCOLTA:
					for (Pair<Integer, Integer> pair : groups.get(Db.VARIETA_RACCOLTA_AVG)) {
						chip.second.add(pair.first + " - " + pair.second);
					}
					break;
			}
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		for (Pair<String, List<String>> chip : chips) {
			activeFilters.put(chip.first, new HashSet<>());
		}
		activeFilters.get(INFO_RAGGRUPPA).add(INFO_FAMIGLIA);
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.all_plants_fragment, container, false);
		setupContent();
		setupToolbar();
		setupFilters();
		return view;
	}
	
	private void gruopByRange(List<Pair<String, List<String>>> cards, String key, String udm) {
		for (Pair<Integer, Integer> dists : groups.get(key)) {
			String tmp = udm;
			if (Objects.equals(udm, " piante") && Objects.equals(dists.first, 1)) {
				udm = " pianta";
			}
			String title = dists.first + (!Objects.equals(dists.second, dists.first) ? " - " + dists.second : "") + udm;
			udm = tmp;
			cards.add(new Pair<>(title, new ArrayList<>()));
		}
		for (String ortaggio : Db.ortaggiNames) {
			System.err.println(ortaggio);
			int d = ((Long) Db.ortaggi.get(ortaggio).get(key)).intValue();
			for (int i = 0; i < groups.get(key).size(); i++) {
				Pair<Integer, Integer> dists = groups.get(key).get(i);
				if (d >= dists.first && d <= dists.second) {
					cards.get(i).second.add(ortaggio);
				}
			}
		}
	}
	
	private void gruopByNome(List<Pair<String, List<String>>> cards) {
		for (char c = 'A'; c <= 'Z'; c++) {
			cards.add(new Pair<>("" + c, new ArrayList<>()));
		}
		for (String ortaggio : Db.ortaggiNames) {
			cards.get(ortaggio.charAt(0) - 'A').second.add(ortaggio);
		}
	}
	
	private void gruopByFamiglia(List<Pair<String, List<String>>> cards) {
		HashMap<String, List<String>> famiglieMap = new HashMap<>();
		for (String ortaggio : Db.ortaggiNames) {
			String famiglia = (String) Db.ortaggi.get(ortaggio).get(Db.VARIETA_TASSONOMIA_FAMIGLIA);
			if (famiglieMap.get(famiglia) == null) {
				famiglieMap.put(famiglia, new ArrayList<>());
			}
			famiglieMap.get(famiglia).add(ortaggio);
		}
		List<String> altro = new ArrayList<>();
		for (String famiglia : Db.famiglie) {
			if (Objects.equals(famiglia, "Crucifere")) {
				famiglieMap.get(famiglia).remove("Altri cavoli");
				famiglieMap.get(famiglia).add("Altri cavoli");
			}
			if (famiglieMap.get(famiglia).size() == 1) {
				altro.add(famiglieMap.get(famiglia).get(0));
				famiglieMap.remove(famiglia);
			} else if (famiglieMap.get(famiglia).size() > 1) {
				cards.add(new Pair<>(famiglia, famiglieMap.get(famiglia)));
			}
		}
		Collections.sort(altro);
		cards.add(new Pair<>("Altro", altro));
	}
	
	private void setupContent() {
		String activeGroup = (new ArrayList<>(activeFilters.get(INFO_RAGGRUPPA))).get(0);
		((TextView) view.findViewById(R.id.all_plants_fl_title)).setText(activeGroup);
		ArrayList<Pair<String, List<String>>> cards = new ArrayList<>();
		switch (activeGroup) {
			case INFO_FAMIGLIA:
				gruopByFamiglia(cards);
				break;
			case INFO_NOME:
				gruopByNome(cards);
				break;
			case INFO_RACCOLTA:
				gruopByRange(cards, Db.VARIETA_RACCOLTA_AVG, " giorni");
				break;
			case INFO_DIST_PIANTE:
				gruopByRange(cards, Db.VARIETA_DISTANZE_PIANTE, " cm");
				break;
			case INFO_DIST_FILE:
				gruopByRange(cards, Db.VARIETA_DISTANZE_FILE, " cm");
				break;
			case INFO_PACK:
				gruopByRange(cards, Db.VARIETA_ALTRO_PACK, " piante");
				break;
		}
		RecyclerView cardsRecyclerView = view.findViewById(R.id.all_plants_fl_card_list_recycler);
		cardsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtaggioCardListAdapter ortaggioCardListAdapter = new OrtaggioCardListAdapter(cards);
		cardsRecyclerView.setAdapter(ortaggioCardListAdapter);
	}
	
	private void setupFilters() {
		RecyclerView drawerRecyclerView = view.findViewById(R.id.all_plants_bl_drawer_recycler);
		drawerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		AllPlantsDrawerAdapter drawerAdapter = new AllPlantsDrawerAdapter(getContext(), chips, activeFilters, INFO_RAGGRUPPA);
		drawerRecyclerView.setAdapter(drawerAdapter);
	}
	
	private void setupToolbar() {
		Toolbar toolbar = view.findViewById(R.id.all_plants_bl_toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		if (activity != null) {
			activity.setSupportActionBar(toolbar);
		}
	}
	
	@Override
	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
		getActivity().getMenuInflater().inflate(R.menu.all_plants_bl_toolbar_menu, menu);
		menu.findItem(R.id.search).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionExpand(MenuItem menuItem) {
				return true;
			}
			
			@Override
			public boolean onMenuItemActionCollapse(MenuItem menuItem) {
				return true;
			}
		});
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setQueryHint("Cerca un ortaggio");
		
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		menu.findItem(R.id.filter).setOnMenuItemClickListener(menuItem -> backdropAnimation());
	}
	
	private boolean backdropAnimation() {
		
		int translateY = displayMetrics.heightPixels - Utils.dp2px(56 + 48 + 8, getContext());
		int interval = 300;
		
		backdropShown = !backdropShown;
		
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.removeAllListeners();
		animatorSet.end();
		animatorSet.cancel();
		
		Interpolator interpolator = new AccelerateDecelerateInterpolator();
		View frontLayer = view.findViewById(R.id.all_plants_backdrop_frontlayer);
		ObjectAnimator animator = ObjectAnimator.ofFloat(frontLayer, "translationY", backdropShown ? translateY : 0);
		animator.setDuration(interval);
		animator.setInterpolator(interpolator);
		animatorSet.play(animator);
		animator.start();
		
		// FIXME double click
		Handler handler = new Handler();
		Runnable runnable;
		if (backdropShown) {
			runnable = () -> view.findViewById(R.id.all_plants_bl_drawer_top_divider).setVisibility(View.VISIBLE);
			interval = 20;
		} else {
			runnable = () -> view.findViewById(R.id.all_plants_bl_drawer_top_divider).setVisibility(View.INVISIBLE);
			interval -= 20;
		}
		handler.postAtTime(runnable, System.currentTimeMillis() + interval);
		handler.postDelayed(runnable, interval);
		System.out.println(activeFilters);
		if (!backdropShown) {
			setupContent();
		}
		
		return false;
	}
}
