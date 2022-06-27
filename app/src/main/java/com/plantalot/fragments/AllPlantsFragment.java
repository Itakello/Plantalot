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

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class AllPlantsFragment extends Fragment {
	
	private FirebaseFirestore db = FirebaseFirestore.getInstance();
	private Query query;
	
	private View view;
	private Toolbar toolbar;
	private AllPlantsFiltersAdapter filterAdapter;
	private AllPlantsCardListAdapter cardAdapter;
	private AllPlantsSearchAdapter searchAdapter;
	
	private boolean isBackdropShown = false;
	private boolean isSearchShown = false;
	
	private final ArrayList<Pair<String, List<String>>> cards = new ArrayList<>();
	private final HashMap<String, Set<String>> activeFilters = new HashMap<>();
	private final DisplayMetrics displayMetrics = new DisplayMetrics();
	
	private final List<String> filteredOrtaggi = new ArrayList<>(/*Db.getOrtaggiNames()*/);
	private final List<String> searchTextList = new ArrayList<>(Collections.nCopies(Db.getOrtaggiNames().size(), ""));
	private final List<String> searchResultsOrtaggi = new ArrayList<>();
	private final HashMap<String, List<String>> searchResultsVarieta = new HashMap<>();
	
	public static final String RAGGRUPPA = "Raggruppa";
	private static final HashMap<String, String> titles = new HashMap<>();
	private static final HashMap<String, List<Pair<Integer, Integer>>> ranges = new HashMap<>();
	private static final List<String> OMBRA = new ArrayList<>(Arrays.asList("Consigliata", "Tollerata", "Tollerata in estate", "Non tollerata"));
	private static final List<String> MONTHS = new ArrayList<>(Arrays.asList("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII"));
	
	private static final List<Pair<String, List<String>>> chips = new ArrayList<>(Arrays.asList(
			new Pair<>(RAGGRUPPA, new ArrayList<>()),  /*, "Produzione", "Anni di rotazione", "Piante per pack"*/
			new Pair<>(Db.VARIETA_TASSONOMIA_FAMIGLIA, Db.getFamiglieNames()),
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
		
		ranges.put(Db.VARIETA_DISTANZE_PIANTE, new ArrayList<>(Arrays.asList(
				new Pair<>(5, 10),
				new Pair<>(15, 20),
				new Pair<>(25, 30),
				new Pair<>(40, 45),
				new Pair<>(70, 100)
		)));
		ranges.put(Db.VARIETA_DISTANZE_FILE, new ArrayList<>(Arrays.asList(
				new Pair<>(15, 30),
				new Pair<>(35, 40),
				new Pair<>(45, 50),
				new Pair<>(100, 200)
		)));
		ranges.put(Db.VARIETA_RACCOLTA_AVG, new ArrayList<>(Arrays.asList(
				new Pair<>(10, 40),
				new Pair<>(45, 60),
				new Pair<>(65, 80),
				new Pair<>(85, 95),
				new Pair<>(100, 150)
		)));
		ranges.put(Db.VARIETA_ALTRO_PACK, new ArrayList<>(Arrays.asList(
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
					for (Pair<Integer, Integer> pair : ranges.get(Db.VARIETA_ALTRO_PACK)) {
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
		view = inflater.inflate(R.layout.all_plants_fragment, container, false);
		toolbar = view.findViewById(R.id.all_plants_bl_toolbar);
		
		cardAdapter = new AllPlantsCardListAdapter(cards, view.findViewById(R.id.all_plants_fl_progressBar));
		RecyclerView cardsRecyclerView = view.findViewById(R.id.all_plants_fl_card_list_recycler);
		cardsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		cardsRecyclerView.setAdapter(cardAdapter);
		
		setupToolbar();
		setupSubheader();
		Handler handler = new Handler();
		handler.post(this::queryDb);  // FIXME !!!!
		handler.post(this::setupSearch);
		handler.postDelayed(this::setupFilters, 300);
		handler.postDelayed(this::searchTextInit, 300);
//		view.findViewById(R.id.all_plants_fl_progressBar).setVisibility(View.VISIBLE);
		return view;
	}
	
	private static void setChip(Pair<String, List<String>> chip, String field) {
		for (Pair<Integer, Integer> pair : ranges.get(field)) {
			chip.second.add(pair.first + " - " + pair.second);
		}
	}

//	@NonNull
//	private List<String> filteredOrtaggi() {
//		List<String> filtered = new ArrayList<>();
//		for (String ortaggioName : Db.getOrtaggiNames()) {
//			HashMap<String, Object> ortaggio = Db.ortaggi.get(ortaggioName);
//			if (respectsFilters(ortaggio)) filtered.add(ortaggioName);
//		}
//		return filtered;
//	}
	
	private boolean respectsFilters(HashMap<String, Object> ortaggio) {
//		if (!(activeFilters.get(Db.VARIETA_TASSONOMIA_FAMIGLIA).isEmpty()
//				|| activeFilters.get(Db.VARIETA_TASSONOMIA_FAMIGLIA).contains((String) ortaggio.get(Db.VARIETA_TASSONOMIA_FAMIGLIA)))) {
//			return false;
//		}
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
	
	private boolean filterByRange(HashMap<String, Object> ortaggio, String field) {
		if (activeFilters.get(field).isEmpty()) return false;
		for (String range : activeFilters.get(field)) {
			if (((Long) ortaggio.get(field)).intValue() >= Integer.parseInt(range.split(" - ")[0])
					&& ((Long) ortaggio.get(field)).intValue() <= Integer.parseInt(range.split(" - ")[1])) {
				return false;
			}
		}
		return true;
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	public void queryDb() {
		query = db.collection("ortaggi");
		
		
		
		for (String field : new ArrayList<>(Arrays.asList(
				Db.VARIETA_TASSONOMIA_FAMIGLIA,
				Db.VARIETA_ALTRO_TOLLERA_MEZZOMBRA
		))) {
			if (!activeFilters.get(field).isEmpty()) {
				query = query.whereIn(field, new ArrayList<>(activeFilters.get(field)));
			}
		}
		
		String fieldTmp = Db.VARIETA_ALTRO_PACK;
		ArrayList<Integer> activeFiltersPack = new ArrayList<>();
		for (String str : activeFilters.get(fieldTmp)) activeFiltersPack.add(Integer.parseInt(str));
		if (!activeFilters.get(fieldTmp).isEmpty()) {
			query = query.whereIn(fieldTmp, new ArrayList<>(activeFiltersPack));
		}
		
		for (String field : new ArrayList<>(Arrays.asList(
				Db.VARIETA_RACCOLTA_AVG,
				Db.VARIETA_DISTANZE_PIANTE,
				Db.VARIETA_DISTANZE_FILE
		))) {
			if (!activeFilters.get(field).isEmpty()) {
				List<Integer> multiRange = new ArrayList<>();
				for (String range : activeFilters.get(field)) {  // FIXME ?
					int start = Integer.parseInt(range.split(" - ")[0]);
					int end = Integer.parseInt(range.split(" - ")[1]);
					multiRange.addAll(IntStream.rangeClosed(start, end).filter(x -> x % 5 == 0).boxed().collect(Collectors.toList()));
				}
				query = query.whereIn(field, multiRange);
			}
		}
		
		if (!activeFilters.get(Db.VARIETA_TRAPIANTI_MESI).isEmpty()) {
			query = query.whereArrayContainsAny(Db.VARIETA_TRAPIANTI_MESI, new ArrayList<>(activeFilters.get(Db.VARIETA_TRAPIANTI_MESI)));
		}
		
		query.get().addOnSuccessListener(queryDocumentSnapshots -> {
			filteredOrtaggi.clear();
			for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
				filteredOrtaggi.add(document.get(Db.VARIETA_CLASSIFICAZIONE_ORTAGGIO).toString());
			}
			setupContent();
		});
	}
	
	private void gruopByNome() {
		for (char c = 'A'; c <= 'Z'; c++) {
			cards.add(new Pair<>("" + c, new ArrayList<>()));
		}
		for (String ortaggio : filteredOrtaggi) {
			cards.get(ortaggio.charAt(0) - 'A').second.add(ortaggio);
		}
		cardAdapter.notifyDataSetChanged();
	}
	
	AtomicInteger counter = new AtomicInteger();
	
	private void gruopByRange(String field, String udm) {
		for (Pair<Integer, Integer> dists : ranges.get(field)) {
			String tmp = udm;
			if (Objects.equals(udm, " piante") && Objects.equals(dists.first, 1)) {  // FIXME plurals
				udm = " pianta";
			}
			String title = dists.first + (!Objects.equals(dists.second, dists.first) ? " - " + dists.second : "") + udm;
			udm = tmp;
			cards.add(new Pair<>(title, new ArrayList<>()));
		}
		counter.set(0);
		for (int i = 0; i < ranges.get(field).size(); i++) {
			Pair<Integer, Integer> range = ranges.get(field).get(i);
			Query q = query.whereGreaterThanOrEqualTo(field, range.first)
					.whereLessThanOrEqualTo(field, range.second);
			addToGroup(q, i, ranges.get(field).size());
		}
	}
	
	private void gruopByField(String field) {
		for (Pair<String, List<String>> chip : chips) {
			if (Objects.equals(chip.first, field)) {
				counter.set(0);
				for (int i = 0; i < chip.second.size(); i++) {
					String value = chip.second.get(i);
					cards.add(new Pair<>(value, new ArrayList<>()));
					Query q = query.whereEqualTo(field, value);
					addToGroup(q, i, chip.second.size());
				}
			}
		}
	}
	
	private void addToGroup(Query q, int row, int size) {
		q.get().addOnSuccessListener(queryDocumentSnapshots -> {
			List<String> group = new ArrayList<>();
			for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
				group.add((String) document.get(Db.VARIETA_CLASSIFICAZIONE_ORTAGGIO));
			}
			cards.get(row).second.addAll(group);
			if (counter.incrementAndGet() == size) cardAdapter.notifyDataSetChanged();  // FIXME !!?
		});
	}
	
	public void setupSubheader() {
		if (isBackdropShown) {
			String title = "Mostra " + filteredOrtaggi.size() + " risultati";
			((TextView) view.findViewById(R.id.all_plants_fl_subheader)).setText(title);
		} else {
			((TextView) view.findViewById(R.id.all_plants_fl_subheader)).setText(titles.get((new ArrayList<>(activeFilters.get(RAGGRUPPA))).get(0)));
		}
	}
	
	@SuppressLint("NotifyDataSetChanged")
	private void setupContent() {
		setupSubheader();
		String activeGroup = (new ArrayList<>(activeFilters.get(RAGGRUPPA))).get(0);
		cards.clear();
		
		switch (activeGroup) {
			case Db.VARIETA_TASSONOMIA_FAMIGLIA:
			case Db.VARIETA_ALTRO_TOLLERA_MEZZOMBRA:
				gruopByField(activeGroup);
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
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	private void setOnMenuItemsClickListeners(Menu menu) {
		menu.findItem(R.id.search).setOnMenuItemClickListener(menuItem -> {
			if (!isSearchShown) {
				isSearchShown = true;
				view.findViewById(R.id.all_plants_bl_search_recycler).setVisibility(View.VISIBLE);
				toolbar.setTitle("Cerca");
				toolbar.setNavigationIcon(R.drawable.ic_round_close_24);
				toolbar.setNavigationOnClickListener(view -> backdropBehaviour());
				backdropBehaviour();
			}
			return true;
		});
		
		menu.findItem(R.id.filter).setOnMenuItemClickListener(menuItem -> {
			view.findViewById(R.id.all_plants_bl_filters_recycler).setVisibility(View.VISIBLE);
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
				handler.post(this::queryDb);
			}
			return true;
		});
		
		view.findViewById(R.id.all_plants_fl_header).setOnClickListener(view -> backdropBehaviour(true));
	}
	
	@SuppressLint("NotifyDataSetChanged")
	@RequiresApi(api = Build.VERSION_CODES.N)
	private void setupSearch() {
		RecyclerView drawerRecyclerView = view.findViewById(R.id.all_plants_bl_search_recycler);
		drawerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		searchAdapter = new AllPlantsSearchAdapter(getContext(), searchResultsOrtaggi, searchResultsVarieta, searchTextList, R.id.allPlantsFragment);
		drawerRecyclerView.setAdapter(searchAdapter);
		searchTextInit();
	}
	
	@SuppressLint("NotifyDataSetChanged")
	@RequiresApi(api = Build.VERSION_CODES.N)
	private void searchTextInit() {
		searchTextChange("");
	}
	
	@SuppressLint("NotifyDataSetChanged")
	@RequiresApi(api = Build.VERSION_CODES.N)
	private void searchTextChange(String searchText) {
		List<String> results = Db.getOrtaggiNames()
				.stream()
				.filter(o -> searchText.isEmpty() || (o.toLowerCase()).contains(searchText.toLowerCase()))
				.collect(Collectors.toList());
		List<String> resultsVarieta = Db.getVarietaNames().keySet()
				.stream()
				.filter(v -> !results.contains(Db.getVarietaNames().get(v)) && (v.toLowerCase()).contains(searchText.toLowerCase()))
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
			String ortaggio = Db.getVarietaNames().get(varietaName);
			if (varietaMap.get(ortaggio) == null) {
				varieta.add(ortaggio);
				varietaMap.put(ortaggio, new ArrayList<>());
			}
			varietaMap.get(ortaggio).add(varietaName);
		}
		
		searchResultsOrtaggi.clear();
		searchResultsOrtaggi.addAll(sorted);
		searchResultsOrtaggi.addAll(basket);
		searchResultsOrtaggi.addAll(varieta);
		searchResultsVarieta.clear();
		searchResultsVarieta.putAll(varietaMap);
		
		searchAdapter.notifyDataSetChanged();
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
		
		getActivity().getMenuInflater().inflate(R.menu.all_plants_bl_toolbar_menu, menu);
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		menu.findItem(R.id.search).setVisible(!isBackdropShown || isSearchShown);
		menu.findItem(R.id.filter).setVisible(!isBackdropShown);
		menu.findItem(R.id.reset).setVisible(isBackdropShown && !isSearchShown);
		menu.findItem(R.id.done).setVisible(isBackdropShown && !isSearchShown);
		
		setOnMenuItemsClickListeners(menu);
		if (isSearchShown) menu.findItem(R.id.search).expandActionView();
		
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setQueryHint("Cerca un ortaggio");
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {
				searchTextList.clear();
				searchTextList.addAll(Collections.nCopies(Db.getOrtaggiNames().size(), newText.toLowerCase()));
				searchTextChange(newText);
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
		
		if (isBackdropShown) {
			view.findViewById(R.id.all_plants_fl_header_arrow).setVisibility(View.VISIBLE);
		} else {
			isSearchShown = false;
			view.findViewById(R.id.all_plants_fl_header_arrow).setVisibility(View.GONE);
			toolbar.setTitle("Piante");
			toolbar.setNavigationIcon(R.drawable.ic_round_arrow_back_24);
			toolbar.setNavigationOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_goto_home));  // FIXME best practice ?
			Handler handler = new Handler();
			handler.postDelayed(() -> {
				view.findViewById(R.id.all_plants_bl_filters_recycler).setVisibility(View.GONE);
				view.findViewById(R.id.all_plants_bl_search_recycler).setVisibility(View.GONE);
			}, interval);
		}
		
		return false;
	}
	
}
