// TODO savedInstanceState !!!!!!!
// TODO separate fragments (filter, search)

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
import com.plantalot.classes.Ortaggio;
import com.plantalot.database.DbPlants;
import com.plantalot.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class AllPlantsFragment extends Fragment {
	
	// FIXME global to local variables
	
	private final FirebaseFirestore db = FirebaseFirestore.getInstance();
	
	private View view;
	private Menu menu;
	private Toolbar toolbar;
	private AllPlantsFiltersAdapter filterAdapter;
	private AllPlantsCardListAdapter cardAdapter;
	private AllPlantsSearchAdapter searchAdapter;
	
	private boolean isBackdropShown = false;
	private boolean isSearchShown = false;
	
	private final ArrayList<Pair<String, List<String>>> cards = new ArrayList<>();
	private final HashMap<String, Set<String>> activeFilters = new HashMap<>();
	private final DisplayMetrics displayMetrics = new DisplayMetrics();
	
	private List<Ortaggio> filteredOrtaggi = new ArrayList<>();
	private final List<String> searchTextList = new ArrayList<>(Collections.nCopies(Db.getOrtaggiNames().size(), ""));
	private final List<String> searchResultsOrtaggi = new ArrayList<>();
	private final HashMap<String, List<String>> searchResultsVarieta = new HashMap<>();
	
	public static final String RAGGRUPPA = "Raggruppa";
	private static final HashMap<String, String> titles = new HashMap<>();
	private static final HashMap<String, List<Pair<Integer, Integer>>> ranges = new HashMap<>();
	private static final List<String> OMBRA = new ArrayList<>(Arrays.asList(
			"Consigliata", "Tollerata", "Tollerata in estate", "Non tollerata"));
	private static final List<String> MONTHS = new ArrayList<>(Arrays.asList(
			"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII"));
	
	private static final List<Pair<String, List<String>>> chips = new ArrayList<>(Arrays.asList(
			new Pair<>(RAGGRUPPA, new ArrayList<>()),  /*, "Produzione", "Anni di rotazione", "Piante per pack"*/
			new Pair<>(DbPlants.VARIETA_TASSONOMIA_FAMIGLIA, DbPlants.getFamiglieNames()),
			new Pair<>(DbPlants.VARIETA_TRAPIANTI_MESI, MONTHS),
			new Pair<>(DbPlants.VARIETA_RACCOLTA_AVG, new ArrayList<>()),
			new Pair<>(DbPlants.VARIETA_DISTANZE_PIANTE, new ArrayList<>()),
			new Pair<>(DbPlants.VARIETA_DISTANZE_FILE, new ArrayList<>()),
			new Pair<>(DbPlants.VARIETA_ALTRO_PACK, new ArrayList<>()),
			new Pair<>(DbPlants.VARIETA_ALTRO_TOLLERA_MEZZOMBRA, OMBRA)
//			new Pair<>(CHIP_ROTAZIONE, Arrays.asList("2", "3", "4", "5", "7")),          // TODO
//			new Pair<>("Giardini e preferiti", Arrays.asList("Tutto", "", "Preferiti"))  // TODO
	));
	
	private static final Map<String, Integer> filtersCount = new HashMap<>();
	private static final HashMap<String, String> UDM = new HashMap<>();
	
	private int translateY;
	
	
	static {
		titles.put(RAGGRUPPA, RAGGRUPPA);
		titles.put(DbPlants.VARIETA_TASSONOMIA_FAMIGLIA, "Famiglie");
		titles.put(DbPlants.VARIETA_TASSONOMIA_SPECIE, "Nome");
		titles.put(DbPlants.VARIETA_TRAPIANTI_MESI, "Mesi per il trapianto");
		titles.put(DbPlants.VARIETA_RACCOLTA_AVG, "Giorni alla raccolta");
		titles.put(DbPlants.VARIETA_DISTANZE_PIANTE, "Distanza fra piante");
		titles.put(DbPlants.VARIETA_DISTANZE_FILE, "Distanza fra file");
		titles.put(DbPlants.VARIETA_ALTRO_PACK, "Piante per pack");
		titles.put(DbPlants.VARIETA_ALTRO_TOLLERA_MEZZOMBRA, "Mezz'ombra");
//.		titles.put(Db.ROTAZIONE, "Anni di rotazione");  // TODO
		
		ranges.put(DbPlants.VARIETA_DISTANZE_PIANTE, new ArrayList<>(Arrays.asList(
				new Pair<>(5, 10),
				new Pair<>(15, 20),
				new Pair<>(25, 30),
				new Pair<>(40, 45),
				new Pair<>(70, 100)
		)));
		ranges.put(DbPlants.VARIETA_DISTANZE_FILE, new ArrayList<>(Arrays.asList(
				new Pair<>(15, 30),
				new Pair<>(35, 40),
				new Pair<>(45, 50),
				new Pair<>(100, 200)
		)));
		ranges.put(DbPlants.VARIETA_RACCOLTA_AVG, new ArrayList<>(Arrays.asList(
				new Pair<>(10, 40),
				new Pair<>(45, 60),
				new Pair<>(65, 80),
				new Pair<>(85, 95),
				new Pair<>(100, 150)
		)));
		ranges.put(DbPlants.VARIETA_ALTRO_PACK, new ArrayList<>(Arrays.asList(
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
							DbPlants.VARIETA_TASSONOMIA_FAMIGLIA,
							DbPlants.VARIETA_TASSONOMIA_SPECIE,
							DbPlants.VARIETA_RACCOLTA_AVG,
							DbPlants.VARIETA_DISTANZE_PIANTE,
							DbPlants.VARIETA_DISTANZE_FILE,
							DbPlants.VARIETA_ALTRO_TOLLERA_MEZZOMBRA,
							DbPlants.VARIETA_ALTRO_PACK));
					break;
				case DbPlants.VARIETA_DISTANZE_PIANTE:
					setChip(chip, DbPlants.VARIETA_DISTANZE_PIANTE);
					break;
				case DbPlants.VARIETA_DISTANZE_FILE:
					setChip(chip, DbPlants.VARIETA_DISTANZE_FILE);
					break;
				case DbPlants.VARIETA_RACCOLTA_AVG:
					setChip(chip, DbPlants.VARIETA_RACCOLTA_AVG);
					break;
				case DbPlants.VARIETA_ALTRO_PACK:
					for (Pair<Integer, Integer> pair : ranges.get(DbPlants.VARIETA_ALTRO_PACK)) {
						chip.second.add(pair.first.toString());
					}
					break;
			}
		}
		
		for (Pair<String, List<String>> chip : chips) {
			filtersCount.put(chip.first, chip.second.size());
		}
		
		UDM.put(Db.VARIETA_RACCOLTA_AVG, "giorni");
		UDM.put(Db.VARIETA_DISTANZE_PIANTE, "cm");
		UDM.put(Db.VARIETA_DISTANZE_FILE, "cm");
		UDM.put(Db.VARIETA_ALTRO_PACK, "piante");
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		for (Pair<String, List<String>> chip : chips) {
			activeFilters.put(chip.first, new HashSet<>());
		}
		activeFilters.get(RAGGRUPPA).add(DbPlants.VARIETA_TASSONOMIA_FAMIGLIA);
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
		
		view.findViewById(R.id.all_plants_backdrop_frontlayer).post(
				() -> translateY = view.getHeight() - Utils.dp2px(56 + 48 + 8, getContext())
		);
		
		Handler handler = new Handler();
		handler.post(this::setupToolbar);
		handler.post(this::setupSubheader);
		handler.post(this::queryDb);
		handler.postDelayed(this::setupFilters, 300);
		handler.postDelayed(this::setupSearch, 300);
		handler.postDelayed(this::searchTextInit, 300);
		return view;
	}

// FIXME !!!!
//	@Override
//	public void onResume() {
//		super.onResume();
//		if (isSearchShown) {
//			System.out.println("EEEEEEEEEEEEEEEEE");
//			view.findViewById(R.id.all_plants_bl_search_recycler).setVisibility(View.VISIBLE);
//			isBackdropShown = false;
//			backdropBehaviour();
//		}
//	}
	
	private static void setChip(Pair<String, List<String>> chip, String field) {
		for (Pair<Integer, Integer> pair : ranges.get(field)) {
			chip.second.add(pair.first + " - " + pair.second);
		}
	}
	
	
	//========[ FILTERS ]========//
	
	// Firestore ha troppe restrizioni sulle queries D:
	// Questo metodo applica, se possibile, soltanto la query legata al field con il minor numero
	// di fitri attivi rapportati al numero di filtri totali per quel field, quindi quella che
	// presumibilmente restituisce il minor numero di risultati.
	// I restanti filtri sono applicati client-side sui risultati della query.
	@RequiresApi(api = Build.VERSION_CODES.N)
	public void queryDb() {
		Query query = db.collection("ortaggi");
		
		double minActiveFilters = 9999;
		String minField = "";
		for (String field : activeFilters.keySet()) {
			double ratio = (double) activeFilters.get(field).size() / (double) filtersCount.get(field);
			if (!Objects.equals(field, RAGGRUPPA) && !activeFilters.get(field).isEmpty() && ratio < minActiveFilters) {
				minActiveFilters = ratio;
				minField = field;
			}
		}
		
		if (!minField.isEmpty() && activeFilters.get(minField).size() <= 10) {
			switch (minField) {
				case Db.VARIETA_TASSONOMIA_FAMIGLIA:
				case Db.VARIETA_ALTRO_TOLLERA_MEZZOMBRA:
					query = query.whereIn(minField, new ArrayList<>(activeFilters.get(minField)));
					break;
				case Db.VARIETA_ALTRO_PACK:
					ArrayList<Integer> activeFiltersPack = new ArrayList<>();
					for (String str : activeFilters.get(minField)) {
						activeFiltersPack.add(Integer.parseInt(str));
					}
					query = query.whereIn(minField, new ArrayList<>(activeFiltersPack));
					break;
				case Db.VARIETA_RACCOLTA_AVG:
				case Db.VARIETA_DISTANZE_PIANTE:
				case Db.VARIETA_DISTANZE_FILE:
					List<Integer> multiRange = new ArrayList<>();
					for (String range : activeFilters.get(minField)) {
						int start = Integer.parseInt(range.split(" - ")[0]);
						int end = Integer.parseInt(range.split(" - ")[1]);
						multiRange.addAll(IntStream
								.rangeClosed(start, end)
								.filter(x -> x % 5 == 0)
								.boxed().collect(Collectors.toList()));
					}
					if (multiRange.size() <= 10) {
						query = query.whereIn(minField, multiRange);
					} else {
						minField = "";
					}
					break;
				case Db.VARIETA_TRAPIANTI_MESI:
					List<String> months = new ArrayList<>();  // FIXME
					for (String month : activeFilters.get(minField)) {
						months.add("" + MONTHS.indexOf(month));
					}
					query = query.whereArrayContainsAny(minField, months);
					break;
			}
		} else {
			minField = "";
		}
		
		String finalMinField = minField;
		query.get().addOnSuccessListener(queryDocumentSnapshots -> {
			filteredOrtaggi.clear();
			for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
				filteredOrtaggi.add(document.toObject(Ortaggio.class));
			}
			filterOrtaggi(finalMinField);
			setupGroups();
		});
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	private void filterOrtaggi(String minField) {
		
		for (String field : new ArrayList<>(Arrays.asList(
				Db.VARIETA_TASSONOMIA_FAMIGLIA,
				Db.VARIETA_ALTRO_TOLLERA_MEZZOMBRA
		))) {
			if (!activeFilters.get(field).isEmpty() && !Objects.equals(field, minField)) {
				filteredOrtaggi = filteredOrtaggi.stream()
						.filter(o -> activeFilters.get(field).contains((String) o.get(field)))
						.collect(Collectors.toList());
			}
		}
		
		String fieldTmp = Db.VARIETA_ALTRO_PACK;
		ArrayList<Integer> activeFiltersPack = new ArrayList<>();
		for (String str : activeFilters.get(fieldTmp))
			activeFiltersPack.add(Integer.parseInt(str));
		if (!activeFilters.get(fieldTmp).isEmpty() && !Objects.equals(fieldTmp, minField)) {
			String finalFieldTmp = fieldTmp;
			filteredOrtaggi = filteredOrtaggi.stream()
					.filter(o -> activeFiltersPack.contains((Integer) o.get(finalFieldTmp)))
					.collect(Collectors.toList());
		}
		
		for (String field : new ArrayList<>(Arrays.asList(
				Db.VARIETA_RACCOLTA_AVG,
				Db.VARIETA_DISTANZE_PIANTE,
				Db.VARIETA_DISTANZE_FILE
		))) {
			if (!activeFilters.get(field).isEmpty() && !Objects.equals(field, minField)) {
				Set<Integer> multiRange = new HashSet<>();
				for (String range : activeFilters.get(field)) {
					int start = Integer.parseInt(range.split(" - ")[0]);
					int end = Integer.parseInt(range.split(" - ")[1]);
					multiRange.addAll(IntStream.rangeClosed(start, end).filter(x -> x % 5 == 0)  // FIXME !!!
							.boxed().collect(Collectors.toSet()));
				}
				filteredOrtaggi = filteredOrtaggi.stream()
						.filter(o -> multiRange.contains((Integer) o.get(field)))  // FIXME !!!
						.collect(Collectors.toList());
			}
		}
		
		fieldTmp = Db.VARIETA_TRAPIANTI_MESI;
		if (!activeFilters.get(fieldTmp).isEmpty() && !Objects.equals(fieldTmp, minField)) {
			String finalFieldTmp = fieldTmp;
			List<String> months = new ArrayList<>();
			for (String month : activeFilters.get(minField)) {
				months.add("" + MONTHS.indexOf(month));
			}
			filteredOrtaggi = filteredOrtaggi.stream()
					.filter(o -> Collections.disjoint(months, (ArrayList<?>) o.get(finalFieldTmp)))
					.collect(Collectors.toList());
		}
	}
	
	
	//========[ GROUPS ]========//
	
	private void gruopByRange(String field) {
		String udm = UDM.get(field);
		for (Pair<Integer, Integer> dists : ranges.get(field)) {
			String tmp = udm;
			if (Objects.equals(udm, "piante") && Objects.equals(dists.first, 1)) {
				udm = "pianta";  // FIXME plurals
			}
			String title = dists.first + (!Objects.equals(dists.second, dists.first) ? " - " + dists.second : "") + " " + udm;
			udm = tmp;
			cards.add(new Pair<>(title, new ArrayList<>()));
		}
		for (Ortaggio ortaggio : filteredOrtaggi) {
			int d = (Integer) ortaggio.get(field);
			for (int i = 0; i < ranges.get(field).size(); i++) {
				Pair<Integer, Integer> dists = ranges.get(field).get(i);
				if (d >= dists.first && d <= dists.second) {
					cards.get(i).second.add(ortaggio.getClassificazione_ortaggio());
				}
			}
		}
	}
	
	private void gruopByNome() {
		for (char c = 'A'; c <= 'Z'; c++) {
			cards.add(new Pair<>("" + c, new ArrayList<>()));
		}
		for (Ortaggio ortaggio : filteredOrtaggi) {
			String ortaggioName = ortaggio.getClassificazione_ortaggio();
			cards.get(ortaggioName.charAt(0) - 'A').second.add(ortaggioName);
		}
	}
	
	private void gruopByField(String field) {
		HashMap<String, List<String>> strMap = new HashMap<>();
		for (Ortaggio ortaggio : filteredOrtaggi) {
			String str = (String) ortaggio.get(field);
			if (strMap.get(str) == null) strMap.put(str, new ArrayList<>());
			strMap.get(str).add(ortaggio.getClassificazione_ortaggio());
		}
		for (Pair<String, List<String>> chip : chips) {
			if (Objects.equals(chip.first, field)) {
				for (int i = 0; i < chip.second.size(); i++) {
					String value = chip.second.get(i);
					if (strMap.get(value) != null) {
						cards.add(new Pair<>(value, strMap.get(value)));
					}
				}
				return;
			}
		}
	}
	
	
	//========[ SETUP UI ]========//
	
	public void setupSubheader() {
		if (isBackdropShown) {
			String title = "Mostra " + filteredOrtaggi.size() + " risultati";
			((TextView) view.findViewById(R.id.all_plants_fl_subheader)).setText(title);
		} else {
			((TextView) view.findViewById(R.id.all_plants_fl_subheader))
					.setText(titles.get((new ArrayList<>(activeFilters.get(RAGGRUPPA))).get(0)));
		}
	}
	
	@SuppressLint("NotifyDataSetChanged")
	private void setupGroups() {
		setupSubheader();
		String activeGroup = (new ArrayList<>(activeFilters.get(RAGGRUPPA))).get(0);
		cards.clear();
		
		switch (activeGroup) {
			case DbPlants.VARIETA_TASSONOMIA_FAMIGLIA:
			case DbPlants.VARIETA_ALTRO_TOLLERA_MEZZOMBRA:
				gruopByField(activeGroup);
				break;
			case DbPlants.VARIETA_TASSONOMIA_SPECIE:
				gruopByNome();
				break;
			case DbPlants.VARIETA_RACCOLTA_AVG:
			case DbPlants.VARIETA_DISTANZE_PIANTE:
			case DbPlants.VARIETA_DISTANZE_FILE:
			case DbPlants.VARIETA_ALTRO_PACK:
				gruopByRange(activeGroup);
				break;
		}
		
		new Handler().postDelayed(() -> cardAdapter.notifyDataSetChanged(), 500);  // FIXME !!!!!!!?!?!?!
	}
	
	private void setupFilters() {
		RecyclerView filterRecycler = view.findViewById(R.id.all_plants_bl_filters_recycler);
		filterRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
		filterAdapter = new AllPlantsFiltersAdapter(getContext(), activeFilters, chips, RAGGRUPPA, this, titles);
		filterRecycler.setAdapter(filterAdapter);
		filterRecycler.setVisibility(View.GONE);
	}
	
	@SuppressLint("NotifyDataSetChanged")
	@RequiresApi(api = Build.VERSION_CODES.N)
	private void setupSearch() {
		RecyclerView searchRecycler = view.findViewById(R.id.all_plants_bl_search_recycler);
		searchRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
		searchAdapter = new AllPlantsSearchAdapter(
				getContext(),
				searchResultsOrtaggi,
				searchResultsVarieta,
				searchTextList,
				R.id.allPlantsFragment);
		searchRecycler.setAdapter(searchAdapter);
		searchTextInit();
		searchRecycler.setVisibility(View.GONE);
	}
	
	private void setupToolbar() {
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		if (activity != null) activity.setSupportActionBar(toolbar);
		toolbar.setNavigationOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_goto_home));
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
		
		this.menu = menu;
		getActivity().getMenuInflater().inflate(R.menu.all_plants_bl_toolbar_menu, menu);
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
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
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	private void setOnMenuItemsClickListeners(Menu menu) {
		Handler handler = new Handler();
		menu.findItem(R.id.search).setOnMenuItemClickListener(menuItem -> {
			if (!isSearchShown) {
				isSearchShown = true;
				view.findViewById(R.id.all_plants_bl_search_recycler).setVisibility(View.VISIBLE);
				toolbar.setTitle("Cerca");
				toolbar.setNavigationIcon(R.drawable.ic_round_close_24);
				toolbar.setNavigationOnClickListener(view -> backdropBehaviour());
				handler.post(this::backdropBehaviour);
			}
			return true;
		});
		
		menu.findItem(R.id.filter).setOnMenuItemClickListener(menuItem -> {
			view.findViewById(R.id.all_plants_bl_filters_recycler).setVisibility(View.VISIBLE);
			toolbar.setTitle("Filtra");
			toolbar.setNavigationIcon(R.drawable.ic_round_close_24);
			toolbar.setNavigationOnClickListener(view -> backdropBehaviour());
			handler.post(this::backdropBehaviour);
			return true;
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
				handler.post(() -> {
					filterAdapter.notifyItemRangeChanged(1, chips.size() - 1);
					queryDb();
				});
			}
			return true;
		});
		
		view.findViewById(R.id.all_plants_fl_header).setOnClickListener(view -> backdropBehaviour(true));
	}
	
	private void updateMenuIcons(Menu menu) {
		menu.findItem(R.id.search).setVisible(!isBackdropShown || isSearchShown);
		menu.findItem(R.id.filter).setVisible(!isBackdropShown);
		menu.findItem(R.id.reset).setVisible(isBackdropShown && !isSearchShown);
		menu.findItem(R.id.done).setVisible(isBackdropShown && !isSearchShown);
	}
	
	
	//========[ SEARCH ]========//
	
	@SuppressLint("NotifyDataSetChanged")
	@RequiresApi(api = Build.VERSION_CODES.N)
	private void searchTextInit() {
		searchTextChange("");
	}
	
	@SuppressLint("NotifyDataSetChanged")
	@RequiresApi(api = Build.VERSION_CODES.N)
	private void searchTextChange(String searchText) {
		List<String> results = DbPlants.getOrtaggiNames()
				.stream()
				.filter(o -> searchText.isEmpty()
						|| (o.toLowerCase()).contains(searchText.toLowerCase()))
				.collect(Collectors.toList());
		List<String> resultsVarieta = DbPlants.getVarietaNames().keySet()
				.stream()
				.filter(v -> !results.contains(DbPlants.getVarietaNames().get(v))
						&& (v.toLowerCase()).contains(searchText.toLowerCase()))
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
			String ortaggio = DbPlants.getVarietaNames().get(varietaName);
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
	
	
	//========[ BACKDROP ]========//
	
	private boolean backdropBehaviour() {
		return backdropBehaviour(false);
	}
	
	@SuppressLint("Recycle")
	private boolean backdropBehaviour(boolean closeOnly) {
		
		if (!isBackdropShown && closeOnly) return false;
		isBackdropShown = !isBackdropShown;
		
		final int DELAY = 200;
		Handler handler = new Handler();
		handler.post(this::setupSubheader);
		
		handler.post(() -> {
			AnimatorSet animatorSet = new AnimatorSet();
			Interpolator interpolator = new AccelerateDecelerateInterpolator();
			View frontLayer = view.findViewById(R.id.all_plants_backdrop_frontlayer);
			ObjectAnimator animator = ObjectAnimator.ofFloat(
					frontLayer,
					"translationY",
					isBackdropShown ? translateY : 0);
			animator.setDuration(DELAY);
			animator.start();
			animator.setInterpolator(interpolator);
			animatorSet.play(animator);
			animator.start();
			animatorSet.removeAllListeners();
			animatorSet.end();
			animatorSet.cancel();
		});
		
		if (isBackdropShown) {
			view.findViewById(R.id.all_plants_fl_header_arrow).setVisibility(View.VISIBLE);
		} else {
			isSearchShown = false;
			view.findViewById(R.id.all_plants_fl_header_arrow).setVisibility(View.GONE);
			toolbar.setTitle("Piante");
			toolbar.setNavigationIcon(R.drawable.ic_round_arrow_back_24);
			toolbar.setNavigationOnClickListener(
					view -> Navigation.findNavController(view).navigate(R.id.action_goto_home)
			);
			handler.postDelayed(() -> {
				view.findViewById(R.id.all_plants_bl_filters_recycler).setVisibility(View.GONE);
				view.findViewById(R.id.all_plants_bl_search_recycler).setVisibility(View.GONE);
			}, DELAY);
		}
		
		updateMenuIcons(menu);
		
		return false;
	}
	
}
