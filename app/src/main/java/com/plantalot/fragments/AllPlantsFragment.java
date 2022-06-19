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
import com.plantalot.adapters.AllPlantsCardListAdapter;
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
	
	private static final HashMap<String, String> titles = new HashMap<>();
	
	private static final HashMap<String, List<Pair<Integer, Integer>>> groups = new HashMap<>();
	private static final List<String> months = new ArrayList<>(Arrays.asList("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII"));
	public static final String RAGGRUPPA = "Raggruppa";
	
	private static final List<Pair<String, List<String>>> chips = new ArrayList<>(Arrays.asList(
			new Pair<>(RAGGRUPPA, new ArrayList<>()),  /*, "Produzione", "Anni di rotazione", "Piante per pack"*/
			new Pair<>(Db.VARIETA_TASSONOMIA_FAMIGLIA, Db.famiglie),
			new Pair<>(Db.VARIETA_TRAPIANTI_MESI, months),
			new Pair<>(Db.VARIETA_RACCOLTA_AVG, new ArrayList<>()),
			new Pair<>(Db.VARIETA_DISTANZE_PIANTE, new ArrayList<>()),
			new Pair<>(Db.VARIETA_DISTANZE_FILE, new ArrayList<>()),
			new Pair<>(Db.VARIETA_ALTRO_PACK, new ArrayList<>())
//			new Pair<>(Db.VARIETA_ALTRO_MEZZOMBRA, Arrays.asList("Sole", "Mezz'ombra"))  // TODO
//			new Pair<>(CHIP_ROTAZIONE, Arrays.asList("2", "3", "4", "5", "7")),  // TODO
//			new Pair<>("Giardini e preferiti", Arrays.asList("Tutto", "", "Preferiti")),  // TODO
	));
	
	static {
		titles.put(RAGGRUPPA, RAGGRUPPA);
		titles.put(Db.VARIETA_TASSONOMIA_FAMIGLIA, "Famiglia");
		titles.put(Db.VARIETA_TASSONOMIA_SPECIE, "Nome");
		titles.put(Db.VARIETA_TRAPIANTI_MESI, "Mesi per il trapianto");
		titles.put(Db.VARIETA_RACCOLTA_AVG, "Giorni alla raccolta");
		titles.put(Db.VARIETA_DISTANZE_PIANTE, "Distanza fra piante");
		titles.put(Db.VARIETA_DISTANZE_FILE, "Distanza fra file");
		titles.put(Db.VARIETA_ALTRO_PACK, "Piante per pack");
//		titles.put(Db.ESPOSIZIONE, "Esposizione");
//		titles.put(Db.ROTAZIONE, "Anni di rotazione");
		
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
				case RAGGRUPPA:
					chip.second.addAll(Arrays.asList(
							Db.VARIETA_TASSONOMIA_FAMIGLIA,
							Db.VARIETA_TASSONOMIA_SPECIE,
							Db.VARIETA_RACCOLTA_AVG,
							Db.VARIETA_DISTANZE_PIANTE,
							Db.VARIETA_DISTANZE_FILE,
							Db.VARIETA_ALTRO_PACK));
					break;
				case Db.VARIETA_ALTRO_PACK:
					for (Pair<Integer, Integer> pair : groups.get(Db.VARIETA_ALTRO_PACK)) {
						chip.second.add(pair.first.toString());
					}
					break;
				case Db.VARIETA_DISTANZE_PIANTE:
					setChip(chip, Db.VARIETA_DISTANZE_PIANTE);
					break;
				case Db.VARIETA_DISTANZE_FILE:
					setChip(chip, Db.VARIETA_DISTANZE_FILE);
					break;
				case Db.VARIETA_RACCOLTA_AVG:
					setChip(chip, Db.VARIETA_RACCOLTA_AVG);
					break;
			}
		}
	}
	
	private static void setChip(Pair<String, List<String>> chip, String key) {
		for (Pair<Integer, Integer> pair : groups.get(key)) {
			chip.second.add(pair.first + " - " + pair.second);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		for (Pair<String, List<String>> chip : chips) {
			activeFilters.put(chip.first, new HashSet<>());
		}
		activeFilters.get(RAGGRUPPA).add(Db.VARIETA_TASSONOMIA_FAMIGLIA);
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.all_plants_fragment, container, false);
		setupContent();
		setupToolbar();
		setupFilters();
		return view;
	}
	
	private boolean respectsFilters(HashMap<String, Object> ortaggio) {
		if (!(activeFilters.get(Db.VARIETA_TASSONOMIA_FAMIGLIA).isEmpty()
				|| activeFilters.get(Db.VARIETA_TASSONOMIA_FAMIGLIA).contains((String) ortaggio.get(Db.VARIETA_TASSONOMIA_FAMIGLIA)))) {
			return false;
		}
		boolean ok = activeFilters.get(Db.VARIETA_TRAPIANTI_MESI).isEmpty();
		if (!ok) {
			for (String month : activeFilters.get(Db.VARIETA_TRAPIANTI_MESI)) {
				if (!ok && number(((List) ortaggio.get(Db.VARIETA_TRAPIANTI_MESI)).get(months.indexOf(month))) == 1) {
					ok = true;
				}
			}
		}
		if (!ok) return false;
		if (filterByRange(ortaggio, Db.VARIETA_RACCOLTA_AVG)) return false;
		if (filterByRange(ortaggio, Db.VARIETA_DISTANZE_PIANTE)) return false;
		if (filterByRange(ortaggio, Db.VARIETA_DISTANZE_FILE)) return false;
		return activeFilters.get(Db.VARIETA_ALTRO_PACK).isEmpty()
				|| activeFilters.get(Db.VARIETA_ALTRO_PACK).contains(ortaggio.get(Db.VARIETA_ALTRO_PACK).toString());
	}
	
	private boolean filterByRange(HashMap<String, Object> ortaggio, String key) {
		if (activeFilters.get(key).isEmpty()) return false;
		for (String range : activeFilters.get(key)) {
			if (((Long) ortaggio.get(key)).intValue() >= Integer.parseInt(range.split(" - ")[0])
					&& ((Long) ortaggio.get(key)).intValue() <= Integer.parseInt(range.split(" - ")[1])) {
				return false;
			}
		}
		return true;
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
		for (String ortaggio : filteredOrtaggi()) {
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
		for (String ortaggio : filteredOrtaggi()) {
			cards.get(ortaggio.charAt(0) - 'A').second.add(ortaggio);
		}
	}
	
	private void gruopByFamiglia(List<Pair<String, List<String>>> cards) {
		HashMap<String, List<String>> famiglieMap = new HashMap<>();
		for (String ortaggio : filteredOrtaggi()) {
			String famiglia = (String) Db.ortaggi.get(ortaggio).get(Db.VARIETA_TASSONOMIA_FAMIGLIA);
			if (famiglieMap.get(famiglia) == null) {
				famiglieMap.put(famiglia, new ArrayList<>());
			}
			famiglieMap.get(famiglia).add(ortaggio);
		}
		List<String> altro = new ArrayList<>();
		for (String famiglia : Db.famiglie) {
			if (famiglieMap.get(famiglia) != null) {
//				if (Objects.equals(famiglia, "Crucifere")) {  // fixme ?
//					famiglieMap.get(famiglia).remove("Altri cavoli");
//					famiglieMap.get(famiglia).add("Altri cavoli");
//				}
//				if (famiglieMap.get(famiglia).size() == 1) {
//					altro.add(famiglieMap.get(famiglia).get(0));
//					famiglieMap.remove(famiglia);
//				} else if (famiglieMap.get(famiglia).size() > 1) {
				cards.add(new Pair<>(famiglia, famiglieMap.get(famiglia)));
//				}
			}
		}
		Collections.sort(altro);
		cards.add(new Pair<>("Altro", altro));
	}
	
	private double number(Object num) {  // FIXME
		switch (num.getClass().getName()) {
			case "java.lang.Double":
				return (Double) num;
			case "java.lang.Long":
				return ((Long) num).doubleValue();
		}
		return 0;
	}
	
	private List<String> filteredOrtaggi() {
		List<String> filtered = new ArrayList<>();
		for (String ortaggioName : Db.ortaggiNames) {
			HashMap<String, Object> ortaggio = Db.ortaggi.get(ortaggioName);
			if (respectsFilters(ortaggio)) filtered.add(ortaggioName);
		}
		return filtered;
	}
	
	private void setupContent() {
		String activeGroup = (new ArrayList<>(activeFilters.get(RAGGRUPPA))).get(0);
//		System.out.println(activeGroup);
		((TextView) view.findViewById(R.id.all_plants_fl_header_title)).setText(titles.get(activeGroup));
		ArrayList<Pair<String, List<String>>> cards = new ArrayList<>();
		switch (activeGroup) {
			case Db.VARIETA_TASSONOMIA_FAMIGLIA:
				gruopByFamiglia(cards);
				break;
			case Db.VARIETA_TASSONOMIA_SPECIE:
				gruopByNome(cards);
				break;
			case Db.VARIETA_RACCOLTA_AVG:
				gruopByRange(cards, Db.VARIETA_RACCOLTA_AVG, " giorni");
				break;
			case Db.VARIETA_DISTANZE_PIANTE:
				gruopByRange(cards, Db.VARIETA_DISTANZE_PIANTE, " cm");
				break;
			case Db.VARIETA_DISTANZE_FILE:
				gruopByRange(cards, Db.VARIETA_DISTANZE_FILE, " cm");
				break;
			case Db.VARIETA_ALTRO_PACK:
				gruopByRange(cards, Db.VARIETA_ALTRO_PACK, " piante");
				break;
		}
		RecyclerView cardsRecyclerView = view.findViewById(R.id.all_plants_fl_card_list_recycler);
		cardsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		AllPlantsCardListAdapter allPlantsCardListAdapter = new AllPlantsCardListAdapter(cards);
		cardsRecyclerView.setAdapter(allPlantsCardListAdapter);
	}
	
	private void setupFilters() {
		RecyclerView drawerRecyclerView = view.findViewById(R.id.all_plants_bl_drawer_recycler);
		drawerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		AllPlantsDrawerAdapter drawerAdapter = new AllPlantsDrawerAdapter(getContext(), chips, activeFilters, RAGGRUPPA, this, titles);
		drawerRecyclerView.setAdapter(drawerAdapter);
	}
	
	public void showResultsNumber() {
		((TextView) view.findViewById(R.id.all_plants_fl_header_title)).setText("Mostra " + filteredOrtaggi().size() + " risultati");
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
//		menu.findItem(R.id.search).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//			@Override
//			public boolean onMenuItemActionExpand(MenuItem menuItem) {
//				return true;
//			}
//
//			@Override
//			public boolean onMenuItemActionCollapse(MenuItem menuItem) {
//				return true;
//			}
//		});
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setQueryHint("Cerca un ortaggio");
		
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		menu.findItem(R.id.filter).setOnMenuItemClickListener(menuItem -> backdropAnimation());
		view.findViewById(R.id.all_plants_fl_header).setOnClickListener(view -> backdropAnimation());
	}
	
	private boolean backdropAnimation() {
		
		int translateY = displayMetrics.heightPixels - Utils.dp2px(56 + 48 + 8, getContext());
		int interval = 200;
		
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
//		Handler handler = new Handler();
//		Runnable runnable;
		if (backdropShown) {
//			interval = 20;
//			runnable = () -> view.findViewById(R.id.all_plants_bl_drawer_top_divider).setVisibility(View.INVISIBLE);
//			view.findViewById(R.id.all_plants_bl_toolbar).setVisibility(View.GONE);
			view.findViewById(R.id.all_plants_fl_header_arrow).setVisibility(View.VISIBLE);
			showResultsNumber();
		} else {
//			interval -= 20;
//			runnable = () -> view.findViewById(R.id.all_plants_bl_drawer_top_divider).setVisibility(View.INVISIBLE);
//			view.findViewById(R.id.all_plants_bl_toolbar).setVisibility(View.VISIBLE);
			view.findViewById(R.id.all_plants_fl_header_arrow).setVisibility(View.GONE);
		}
//		handler.postAtTime(runnable, System.currentTimeMillis() + interval);
//		handler.postDelayed(runnable, interval);
//		System.out.println(activeFilters);
		if (!backdropShown) {
			setupContent();
		}
		
		return false;
	}
}
