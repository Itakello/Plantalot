package com.plantalot.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.R;
import com.plantalot.adapters.AllPlantsCardListAdapter;
import com.plantalot.adapters.AllPlantsFiltersAdapter;
import com.plantalot.adapters.AllPlantsSearchAdapter;
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
import java.util.stream.Collectors;


public class AllPlantsFragment extends Fragment {
	
	private View view;
	private Toolbar toolbar;
	private AllPlantsFiltersAdapter filterAdapter;
	private AllPlantsCardListAdapter cardAdapter;
	
	private boolean isBackdropShown = false;
	private boolean isSearchShown = false;
	
	private final ArrayList<Pair<String, List<String>>> cards = new ArrayList<>();
	private final HashMap<String, Set<String>> activeFilters = new HashMap<>();
	private final DisplayMetrics displayMetrics = new DisplayMetrics();
	
	public static final String RAGGRUPPA = "Raggruppa";
	private static final HashMap<String, String> titles = new HashMap<>();
	private static final HashMap<String, List<Pair<Integer, Integer>>> groups = new HashMap<>();
	private static final List<String> MONTHS = new ArrayList<>(Arrays.asList("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII"));
	private static final List<String> OMBRA = new ArrayList<>(Arrays.asList("Consigliata", "Tollerata", "Tollerata in estate", "Non tollerata"));
	
	private static final List<Pair<String, List<String>>> chips = new ArrayList<>(Arrays.asList(
			new Pair<>(RAGGRUPPA, new ArrayList<>()),  /*, "Produzione", "Anni di rotazione", "Piante per pack"*/
			new Pair<>(Db.VARIETA_TASSONOMIA_FAMIGLIA, Db.famiglie),
			new Pair<>(Db.VARIETA_TRAPIANTI_MESI, MONTHS),
			new Pair<>(Db.VARIETA_RACCOLTA_AVG, new ArrayList<>()),
			new Pair<>(Db.VARIETA_DISTANZE_PIANTE, new ArrayList<>()),
			new Pair<>(Db.VARIETA_DISTANZE_FILE, new ArrayList<>()),
			new Pair<>(Db.VARIETA_ALTRO_PACK, new ArrayList<>()),
			new Pair<>(Db.VARIETA_ALTRO_TOLLERA_MEZZOMBRA, OMBRA)
//			new Pair<>(CHIP_ROTAZIONE, Arrays.asList("2", "3", "4", "5", "7")),          // TODO
//			new Pair<>("Giardini e preferiti", Arrays.asList("Tutto", "", "Preferiti"))  // TODO
	));
	
	static {
		titles.put(RAGGRUPPA, RAGGRUPPA);
		titles.put(Db.VARIETA_TASSONOMIA_FAMIGLIA, "Famiglie");
		titles.put(Db.VARIETA_TASSONOMIA_SPECIE, "Nome");
		titles.put(Db.VARIETA_TRAPIANTI_MESI, "Mesi per il trapianto");
		titles.put(Db.VARIETA_RACCOLTA_AVG, "Giorni alla raccolta");
		titles.put(Db.VARIETA_DISTANZE_PIANTE, "Distanza fra piante");
		titles.put(Db.VARIETA_DISTANZE_FILE, "Distanza fra file");
		titles.put(Db.VARIETA_ALTRO_PACK, "Piante per pack");
		titles.put(Db.VARIETA_ALTRO_TOLLERA_MEZZOMBRA, "Mezz'ombra");
//.		titles.put(Db.ROTAZIONE, "Anni di rotazione");  // TODO
		
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
							Db.VARIETA_ALTRO_TOLLERA_MEZZOMBRA,
							Db.VARIETA_ALTRO_PACK));
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
				case Db.VARIETA_ALTRO_PACK:
					for (Pair<Integer, Integer> pair : groups.get(Db.VARIETA_ALTRO_PACK)) {
						chip.second.add(pair.first.toString());
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
		activeFilters.get(RAGGRUPPA).add(Db.VARIETA_TASSONOMIA_FAMIGLIA);
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.all_plants_fragment, container, false);
		toolbar = view.findViewById(R.id.all_plants_bl_toolbar);
		
		cardAdapter = new AllPlantsCardListAdapter(cards);
		RecyclerView cardsRecyclerView = view.findViewById(R.id.all_plants_fl_card_list_recycler);
		cardsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		cardsRecyclerView.setAdapter(cardAdapter);
		
		setupToolbar();
		setupSubheader();
		Handler handler = new Handler();
		handler.post(this::setupContent);
		handler.postDelayed(this::setupFilters, 100);
		return view;
	}
	
	private static void setChip(Pair<String, List<String>> chip, String key) {
		for (Pair<Integer, Integer> pair : groups.get(key)) {
			chip.second.add(pair.first + " - " + pair.second);
		}
	}
	
	@NonNull
	private List<String> filteredOrtaggi() {
		List<String> filtered = new ArrayList<>();
		for (String ortaggioName : Db.ortaggiNames) {
			HashMap<String, Object> ortaggio = Db.ortaggi.get(ortaggioName);
			if (respectsFilters(ortaggio)) filtered.add(ortaggioName);
		}
		return filtered;
	}
	
	private boolean respectsFilters(HashMap<String, Object> ortaggio) {
		if (!(activeFilters.get(Db.VARIETA_TASSONOMIA_FAMIGLIA).isEmpty()
				|| activeFilters.get(Db.VARIETA_TASSONOMIA_FAMIGLIA).contains((String) ortaggio.get(Db.VARIETA_TASSONOMIA_FAMIGLIA)))) {
			return false;
		}
		boolean ok = activeFilters.get(Db.VARIETA_TRAPIANTI_MESI).isEmpty();
		if (!ok) {
			for (String month : activeFilters.get(Db.VARIETA_TRAPIANTI_MESI)) {
				if (!ok && Utils.number(((List<?>) ortaggio.get(Db.VARIETA_TRAPIANTI_MESI)).get(MONTHS.indexOf(month))) == 1) {
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
	
	private void gruopByRange(String key, String udm) {
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
			int d = ((Long) Db.ortaggi.get(ortaggio).get(key)).intValue();
			for (int i = 0; i < groups.get(key).size(); i++) {
				Pair<Integer, Integer> dists = groups.get(key).get(i);
				if (d >= dists.first && d <= dists.second) {
					cards.get(i).second.add(ortaggio);
				}
			}
		}
	}
	
	private void gruopByNome() {
		for (char c = 'A'; c <= 'Z'; c++) {
			cards.add(new Pair<>("" + c, new ArrayList<>()));
		}
		for (String ortaggio : filteredOrtaggi()) {
			cards.get(ortaggio.charAt(0) - 'A').second.add(ortaggio);
		}
	}
	
	private void gruopByString(String field, List<String> strList) {
		HashMap<String, List<String>> strMap = new HashMap<>();
		for (String ortaggio : filteredOrtaggi()) {
			String str = (String) Db.ortaggi.get(ortaggio).get(field);
			if (strMap.get(str) == null) strMap.put(str, new ArrayList<>());
			strMap.get(str).add(ortaggio);
		}
		for (String str : strList) {
			if (strMap.get(str) != null) {
				cards.add(new Pair<>(str, strMap.get(str)));
			}
		}
	}
	
	@SuppressLint("NotifyDataSetChanged")
	public void setupContent() {
		setupSubheader();
		String activeGroup = (new ArrayList<>(activeFilters.get(RAGGRUPPA))).get(0);
		cards.clear();
		switch (activeGroup) {
			case Db.VARIETA_TASSONOMIA_FAMIGLIA:
				gruopByString(activeGroup, Db.famiglie);
				break;
			case Db.VARIETA_ALTRO_TOLLERA_MEZZOMBRA:
				gruopByString(activeGroup, OMBRA);
				break;
			case Db.VARIETA_TASSONOMIA_SPECIE:
				gruopByNome();
				break;
			case Db.VARIETA_RACCOLTA_AVG:
				gruopByRange(Db.VARIETA_RACCOLTA_AVG, " giorni");
				break;
			case Db.VARIETA_DISTANZE_PIANTE:
				gruopByRange(Db.VARIETA_DISTANZE_PIANTE, " cm");
				break;
			case Db.VARIETA_DISTANZE_FILE:
				gruopByRange(Db.VARIETA_DISTANZE_FILE, " cm");
				break;
			case Db.VARIETA_ALTRO_PACK:
				gruopByRange(Db.VARIETA_ALTRO_PACK, " piante");
				break;
		}
<<<<<<< HEAD
		RecyclerView cardsRecyclerView = view.findViewById(R.id.all_plants_fl_card_list_recycler);
		cardsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtaggioCardListAdapter ortaggioCardListAdapter = new OrtaggioCardListAdapter(cards, R.id.allPlantsFragment);
		cardsRecyclerView.setAdapter(ortaggioCardListAdapter);
=======
		cardAdapter.notifyDataSetChanged();
	}
	
	public void setupSubheader() {
		if (isBackdropShown) {
			String title = "Mostra " + filteredOrtaggi().size() + " risultati";
			((TextView) view.findViewById(R.id.all_plants_fl_subheader)).setText(title);
		} else {
			((TextView) view.findViewById(R.id.all_plants_fl_subheader)).setText(titles.get((new ArrayList<>(activeFilters.get(RAGGRUPPA))).get(0)));
		}
>>>>>>> 1d6e065b4633e9abbdc6d4fa44fb7a1bdf38a59f
	}
	
	private void setupFilters() {
		RecyclerView drawerRecyclerView = view.findViewById(R.id.all_plants_bl_filters_recycler);
		drawerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		filterAdapter = new AllPlantsFiltersAdapter(getContext(), activeFilters, chips, RAGGRUPPA, this, titles);
		drawerRecyclerView.setAdapter(filterAdapter);
	}
	
	private void setupToolbar() {
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		if (activity != null) activity.setSupportActionBar(toolbar);
		toolbar.setNavigationOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_goto_home));  // FIXME best practice ?
	}
	
	private void setOnMenuItemsClickListeners(Menu menu) {
		menu.findItem(R.id.search).setOnMenuItemClickListener(menuItem -> {
			if (!isSearchShown) {
				isSearchShown = true;
				view.findViewById(R.id.all_plants_bl_filters_recycler).setVisibility(View.GONE);
				view.findViewById(R.id.all_plants_bl_search_recycler).setVisibility(View.VISIBLE);
				toolbar.setTitle("Ricerca");
				toolbar.setNavigationIcon(R.drawable.ic_round_close_24);
				toolbar.setNavigationOnClickListener(view -> backdropBehaviour());
				backdropBehaviour();
			}
			return true;
		});
		
		menu.findItem(R.id.filter).setOnMenuItemClickListener(menuItem -> {
			toolbar.setTitle("Filtra");
			toolbar.setNavigationIcon(R.drawable.ic_round_close_24);
			toolbar.setNavigationOnClickListener(view -> backdropBehaviour());
			return backdropBehaviour();
		});
		
		menu.findItem(R.id.done).setOnMenuItemClickListener(menuItem -> backdropBehaviour());
		
		menu.findItem(R.id.reset).setOnMenuItemClickListener(menuItem -> {
			boolean isChanged = false;
			for (String filter : activeFilters.keySet()) {
				if (!activeFilters.get(filter).isEmpty() && !Objects.equals(filter, RAGGRUPPA)) {
					isChanged = true;
					activeFilters.get(filter).clear();
				}
			}
			if (isChanged) {
				filterAdapter.notifyItemRangeChanged(1, chips.size() - 1);
				Handler handler = new Handler();
				handler.post(this::setupContent);
//				setupContent();
			}
			return true;
		});
		
		view.findViewById(R.id.all_plants_fl_header).setOnClickListener(view -> backdropBehaviour(true));
	}
	
	@SuppressLint("NotifyDataSetChanged")
	@RequiresApi(api = Build.VERSION_CODES.N)  // fixme ???
	private void searchTextChange(String searchText, List<String> filteredOrtaggi, HashMap<String, List<String>> filteredVarieta, AllPlantsSearchAdapter searchAdapter) {
		
		List<String> results = Db.ortaggiNames
				.stream()
				.filter(o -> searchText.isEmpty() || (o.toLowerCase()).contains(searchText.toLowerCase()))
				.collect(Collectors.toList());
		List<String> resultsVarieta = Db.varietaNames.keySet()
				.stream()
				.filter(v -> !results.contains(Db.varietaNames.get(v)) && (v.toLowerCase()).contains(searchText.toLowerCase()))
				.collect(Collectors.toList());
		
		List<String> sorted = new ArrayList<>();
		List<String> basket = new ArrayList<>();
		List<String> varieta = new ArrayList<>();
		HashMap<String, List<String>> varietaMap = new HashMap<>();
		
		for (String ortaggio : results) {
			if ((ortaggio.toLowerCase()).startsWith(searchText.toLowerCase())) {
				sorted.add(ortaggio);
			} else {
				basket.add(ortaggio);
			}
			varietaMap.put(ortaggio, new ArrayList<>());
		}
		for (String varietaName : resultsVarieta) {
			String ortaggio = Db.varietaNames.get(varietaName);
			if (varietaMap.get(ortaggio) == null) {
				varieta.add(ortaggio);
				varietaMap.put(ortaggio, new ArrayList<>());
			}
			varietaMap.get(ortaggio).add(varietaName);
		}
		
		filteredOrtaggi.clear();
		filteredOrtaggi.addAll(sorted);
		filteredOrtaggi.addAll(basket);
		filteredOrtaggi.addAll(varieta);
		filteredVarieta.clear();
		filteredVarieta.putAll(varietaMap);
		
		searchAdapter.notifyDataSetChanged();
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N)  // fixme ???
	@Override
	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
		
		getActivity().getMenuInflater().inflate(R.menu.all_plants_bl_toolbar_menu, menu);
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		List<String> filteredOrtaggi = new ArrayList<>();
		List<String> searchTextList = new ArrayList<>(Collections.nCopies(Db.ortaggiNames.size(), ""));
		HashMap<String, List<String>> filteredVarieta = new HashMap<>();
		
		RecyclerView drawerRecyclerView = view.findViewById(R.id.all_plants_bl_search_recycler);
		drawerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		AllPlantsSearchAdapter searchAdapter = new AllPlantsSearchAdapter(getContext(), filteredOrtaggi, filteredVarieta, searchTextList, R.id.allPlantsFragment);
		drawerRecyclerView.setAdapter(searchAdapter);
		
		menu.findItem(R.id.search).setVisible(!isBackdropShown || isSearchShown);
		menu.findItem(R.id.filter).setVisible(!isBackdropShown);
		menu.findItem(R.id.reset).setVisible(isBackdropShown && !isSearchShown);
		menu.findItem(R.id.done).setVisible(isBackdropShown && !isSearchShown);
		
		setOnMenuItemsClickListeners(menu);
		if (isSearchShown) menu.findItem(R.id.search).expandActionView();
		
		searchTextChange("", filteredOrtaggi, filteredVarieta, searchAdapter);
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setQueryHint("Cerca un ortaggio");
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {
				searchTextList.clear();
				searchTextList.addAll(Collections.nCopies(Db.ortaggiNames.size(), newText.toLowerCase()));
				searchTextChange(newText, filteredOrtaggi, filteredVarieta, searchAdapter);
				return true;
			}
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				System.out.println(query);
				return false;
			}
		});
	}
	
	private boolean backdropBehaviour() {
		return backdropBehaviour(false);
	}
	
	@SuppressLint("Recycle")
	private boolean backdropBehaviour(boolean closeOnly) {
		
		if (!isBackdropShown && closeOnly) return false;
		isBackdropShown = !isBackdropShown;
		
		setupSubheader();
		
		int translateY = displayMetrics.heightPixels - Utils.dp2px(56 + 48 + 8, getContext());
		int interval = 200;
		AnimatorSet animatorSet = new AnimatorSet();
		Interpolator interpolator = new AccelerateDecelerateInterpolator();
		
		getActivity().invalidateOptionsMenu();
		
		animatorSet.removeAllListeners();
		animatorSet.end();
		animatorSet.cancel();
		
		View frontLayer = view.findViewById(R.id.all_plants_backdrop_frontlayer);
		
		ObjectAnimator animator = ObjectAnimator.ofFloat(frontLayer, "translationY", isBackdropShown ? translateY : 0);
		animator.setDuration(interval);
		animator.start();
		if (interpolator != null) {
			animator.setInterpolator(interpolator);
		}
		animatorSet.play(animator);
		animator.start();

//.		Handler handler = new Handler();
//.		Runnable runnable;
		if (isBackdropShown) {
			view.findViewById(R.id.all_plants_fl_header_arrow).setVisibility(View.VISIBLE);
//			setupContent();
//.			interval = 20;
//.			runnable = () -> view.findViewById(R.id.all_plants_bl_drawer_top_divider).setVisibility(View.INVISIBLE);
//.			view.findViewById(R.id.all_plants_bl_toolbar).setVisibility(View.GONE);
		} else {
			isSearchShown = false;
			view.findViewById(R.id.all_plants_fl_header_arrow).setVisibility(View.GONE);
			view.findViewById(R.id.all_plants_bl_filters_recycler).setVisibility(View.VISIBLE);
			view.findViewById(R.id.all_plants_bl_search_recycler).setVisibility(View.GONE);
			toolbar.setTitle("Piante");
			toolbar.setNavigationIcon(R.drawable.ic_round_arrow_back_24);
			toolbar.setNavigationOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_goto_home));  // FIXME best practice ?
//.			interval -= 20;
//.			runnable = () -> view.findViewById(R.id.all_plants_bl_drawer_top_divider).setVisibility(View.INVISIBLE);
//.			view.findViewById(R.id.all_plants_bl_toolbar).setVisibility(View.VISIBLE);
		}


//.		handler.postAtTime(runnable, System.currentTimeMillis() + interval);
//.		handler.postDelayed(runnable, interval);
		
		return false;
	}
	
}
