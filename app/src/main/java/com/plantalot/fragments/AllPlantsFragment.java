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
import android.widget.FrameLayout;

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
import com.plantalot.animations.NavigationIconClickListener2;
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


public class AllPlantsFragment extends Fragment {
	
	private Giardino giardino;
	private static User user;
	private View view;
	private HashMap<String, List<String>> filters = new HashMap<>();
	private boolean backdropShown = false;
	
	private final List<Pair<String, List<String>>> chips = new ArrayList<>(Arrays.asList(
			new Pair<>("Raggruppa", Arrays.asList("Famiglia", "Nome", "Distanza fra piante", "Distanza fra file", "Tempo di raccolta", "Produzione", "Anni di rotazione", "Piante per pack", "Mesi per il trapianto")),
//			new Pair<>("Giardini e preferiti", Arrays.asList("Tutto", "", "Preferiti")),  // TODO
			new Pair<>("Famiglia", Db.famiglie),
			new Pair<>("Mesi per il trapianto", Arrays.asList("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "Questo mese")),
			new Pair<>("Giorni per la raccolta", Arrays.asList("10-40", "41-60", "61-80", "81-90", "91-150")),
			new Pair<>("Dimensioni", Arrays.asList("Piccole", "Medie", "Grandi")),
			new Pair<>("Esposizione", Arrays.asList("Sole", "Mezz'ombra"))
	));
	
	public static AllPlantsFragment newInstance() {
		AllPlantsFragment myFrag = new AllPlantsFragment();
		Bundle args = new Bundle();
		args.putParcelable("User", user);
		myFrag.setArguments(args);
		return myFrag;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			System.out.println("User preso dal bundle");
			user = bundle.getParcelable("User");
		} else {
			user = new User("Giacomo");
		}
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.all_plants_fragment, container, false);
		filters.put("Raggruppa", new ArrayList<>(Arrays.asList("Famiglia")));
		setUpRecyclerView();
		setUpToolbar();
		setUpFilters();
		return view;
	}
	
	private void setUpRecyclerView() {
		List<Pair<String, List<String>>> cards = new ArrayList();  // FIXME HasMap
		switch (filters.get("Raggruppa").get(0)) {
			case "Famiglia":
				cards = Db.cards;
				break;
			case "Nome":
				cards = new ArrayList<>();
				for (int c = 'A'; c <= 'Z'; c++) {
					cards.add(new Pair<>("" + (char)c, new ArrayList<>()));
				}
				List<String> ortaggiNames = new ArrayList<>(Db.ortaggi.keySet());
				Collections.sort(ortaggiNames);
				for (String ortaggio : ortaggiNames) {
					cards.get(ortaggio.charAt(0) - 'A').second.add(ortaggio);
				}
				break;
			case "Distanza fra piante":
				break;
			case "Distanza fra file":
				break;
			case "Tempo di raccolta":
				break;
			case "Produzione":
				break;
			case "Anni di rotazione":
				break;
			case "Piante per pack":
				break;
			case "Mesi per il trapianto":
				break;
		}
		
		RecyclerView cardsRecyclerView = view.findViewById(R.id.all_plants_fl_card_list_recycler);
		cardsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtaggioCardListAdapter ortaggioCardListAdapter = new OrtaggioCardListAdapter(cards);
		cardsRecyclerView.setAdapter(ortaggioCardListAdapter);
		
	}
	
	private void setUpFilters() {
		RecyclerView drawerRecyclerView = view.findViewById(R.id.all_plants_bl_drawer_recycler);
		drawerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		AllPlantsDrawerAdapter drawerAdapter = new AllPlantsDrawerAdapter(getContext(), chips, filters);
		drawerRecyclerView.setAdapter(drawerAdapter);
		
	}
	
	private void setUpToolbar() {
		Toolbar toolbar = view.findViewById(R.id.all_plants_bl_toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		
		if (activity != null) {
			activity.setSupportActionBar(toolbar);
		}
		
	}
	
	// Show appbar right menu
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
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		menu.findItem(R.id.filter).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				backdropShown = !backdropShown;
				AnimatorSet animatorSet = new AnimatorSet();
				View frontLayer = view.findViewById(R.id.all_plants_backdrop_frontlayer);
				Interpolator interpolator = new AccelerateDecelerateInterpolator();
				int translateY = displayMetrics.heightPixels - Utils.dp2px(56 + 48 + 8, getContext());
				
				// Cancel the existing com.plantalot.animations
				animatorSet.removeAllListeners();
				animatorSet.end();
				animatorSet.cancel();

//				updateIcon(frontLayer);
				
				// Translation animation
				int interval = 300;
				int interval2 = interval - 20;
				ObjectAnimator animator = ObjectAnimator.ofFloat(frontLayer, "translationY", backdropShown ? translateY : 0);
				animator.setDuration(interval);
				if (interpolator != null) {
					animator.setInterpolator(interpolator);
				}
				animatorSet.play(animator);
				animator.start();
				
				// Add bottom margin to RecyclerView to not crop the content
				RecyclerView fl = view.findViewById(R.id.all_plants_fl_card_list_recycler);
				FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fl.getLayoutParams();
				params.bottomMargin = backdropShown ? translateY : 0;
				fl.setLayoutParams(params);
				
				// FIXME double click
				if (backdropShown) {
					view.findViewById(R.id.all_plants_bl_drawer_top_divider).setVisibility(View.VISIBLE);
				} else {
					Handler handler = new Handler();
					Runnable runnable = new Runnable() {
						public void run() {
							view.findViewById(R.id.all_plants_bl_drawer_top_divider).setVisibility(View.INVISIBLE);
						}
					};
					handler.postAtTime(runnable, System.currentTimeMillis() + interval2);
					handler.postDelayed(runnable, interval2);
				}
				System.out.println(filters);
				if (!backdropShown) {
					setUpRecyclerView();
				}
				return false;
			}
		});
	}
}
