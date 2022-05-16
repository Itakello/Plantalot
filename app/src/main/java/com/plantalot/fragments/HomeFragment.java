package com.plantalot.fragments;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.plantalot.R;
import com.plantalot.adapters.HomeDrawerAdapter;
import com.plantalot.utils.Consts;
import com.plantalot.animations.NavigationIconClickListener;
import com.plantalot.adapters.HomeOrtiAdapter;
import com.plantalot.adapters.CircleButtonsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class HomeFragment extends Fragment {
	
	private final Map<String, Map<String, List<Integer>>> mDataGiardini = new HashMap<>();
	private final List<Pair<String, Integer>> mDataButtons = Arrays.asList(
			new Pair<>("Tutte le piante", R.drawable.ic_iconify_carrot_24),
			new Pair<>("Le mie piante", R.drawable.ic_iconify_sprout_24),
			new Pair<>("Visualizza carriola", R.drawable.ic_round_wheelbarrow_24),
			new Pair<>("Disponi giardino", R.drawable.ic_round_auto_24),
			new Pair<>("Aggiungi orto", R.drawable.ic_round_add_big_24));
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		Collections.shuffle(Consts.plants_icons, new Random(System.currentTimeMillis()));
		
		mDataGiardini.put("Trento", new HashMap<>());
		mDataGiardini.put("Bergamo", new HashMap<>());
		mDataGiardini.put("Belluno", new HashMap<>());
		
		for (int i = 1, a = 0; i <= Consts.CARD_PLANTS + 2; i++, a += i) {
			mDataGiardini.get("Trento").put("Orto " + i, Consts.plants_icons.subList(a, a + i));
		}
		mDataGiardini.get("Belluno").put("Back yard", Arrays.asList(R.mipmap.plant_tomato_3944072, R.mipmap.plant_cabbage_3944158, R.mipmap.plant_garlic_3944096, R.mipmap.plant_watermelon_3944164, R.mipmap.plant_peanut_3944274, R.mipmap.plant_asparagus_3944087, R.mipmap.plant_beet_3944102, R.mipmap.plant_chard_3944149, R.mipmap.plant_broccoli_3944323, R.mipmap.plant_artichoke_3944084, R.mipmap.plant_melon_3944173, R.mipmap.plant_carrot_3944093, R.mipmap.plant_cauliflower_3944060, R.mipmap.plant_cabbage_3944158, R.mipmap.plant_kale_3944155, R.mipmap.plant_cucumber_3944113, R.mipmap.plant_onion_3944225, R.mipmap.plant_beans_3944104, R.mipmap.plant_soybean_3944107, R.mipmap.plant_fennel_3944161, R.mipmap.plant_strawberry_3944176, R.mipmap.plant_lettuce_3944125, R.mipmap.plant_corn_3944286, R.mipmap.plant_eggplants_3944110, R.mipmap.plant_melon_3944173, R.mipmap.plant_chili_pepper_3944137, R.mipmap.plant_pepper_3944075, R.mipmap.plant_leek_3944259, R.mipmap.plant_vegetable_3944143, R.mipmap.plant_turnip_3944078, R.mipmap.plant_shallot_3944099, R.mipmap.plant_celery_3944146, R.mipmap.plant_vegetable_3944152, R.mipmap.plant_spinach_3944292, R.mipmap.plant_melon_3944173, R.mipmap.plant_pumpkin_3944344, R.mipmap.plant_zucchini_3944064));
		mDataGiardini.get("Bergamo").put("Dietro casa", Arrays.asList(R.mipmap.plant_cucumber_3944113, R.mipmap.plant_onion_3944225, R.mipmap.plant_beans_3944104, R.mipmap.plant_fennel_3944161, R.mipmap.plant_strawberry_3944176, R.mipmap.plant_lettuce_3944125, R.mipmap.plant_corn_3944286, R.mipmap.plant_eggplants_3944110, R.mipmap.plant_melon_3944173, R.mipmap.plant_chili_pepper_3944137, R.mipmap.plant_pepper_3944075, R.mipmap.plant_leek_3944259, R.mipmap.plant_vegetable_3944143, R.mipmap.plant_turnip_3944078, R.mipmap.plant_shallot_3944099, R.mipmap.plant_celery_3944146, R.mipmap.plant_vegetable_3944152, R.mipmap.plant_spinach_3944292, R.mipmap.plant_melon_3944173, R.mipmap.plant_pumpkin_3944344, R.mipmap.plant_zucchini_3944064));
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		setupRecyclerView(view);
		setupToolbar(view);
		return view;
	}
	
	private void setupRecyclerView(@NonNull View view) {
		String key = "Trento";
		TextView title = view.findViewById(R.id.home_fl_title_giardino);
		title.setText(key);
		
		RecyclerView giardiniRecyclerView = view.findViewById(R.id.home_bl_drawer_recycler);
		giardiniRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		HomeDrawerAdapter giardiniAdapter = new HomeDrawerAdapter(getActivity(), new ArrayList<>(mDataGiardini.keySet()));
		giardiniRecyclerView.setAdapter(giardiniAdapter);
		
		RecyclerView ortiRecyclerView = view.findViewById(R.id.home_fl_recycler_orti);
		ortiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		HomeOrtiAdapter homeOrtiAdapter = new HomeOrtiAdapter(mDataGiardini.get(key));
		ortiRecyclerView.setAdapter(homeOrtiAdapter);
		
		RecyclerView navbuttonsRecyclerView = view.findViewById(R.id.home_fl_recycler_navbuttons);
		CircleButtonsAdapter circleButtonsAdapter = new CircleButtonsAdapter(mDataButtons);
		FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
		flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
		navbuttonsRecyclerView.setLayoutManager(flexboxLayoutManager);
		navbuttonsRecyclerView.setAdapter(circleButtonsAdapter);
	}
	
	private void setupToolbar(@NonNull View view) {
		Toolbar toolbar = view.findViewById(R.id.home_bl_toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		
		if (activity != null) {
			activity.setSupportActionBar(toolbar);
		}
		
		final LinearLayout drawer = view.findViewById(R.id.home_bl_drawer);
		drawer.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		
		// animation
		toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
				getContext(),
				view.findViewById(R.id.home_backdrop_frontlayer),
				new AccelerateDecelerateInterpolator(),
				R.drawable.ic_round_menu_24,
				R.drawable.ic_round_close_24,
				drawer.getMeasuredHeight()));
	}
	
	// Show appbar right menu
	@Override
	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
		getActivity().getMenuInflater().inflate(R.menu.home_bl_toolbar_menu, menu);
	}
	
}
