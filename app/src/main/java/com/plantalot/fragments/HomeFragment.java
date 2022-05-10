package com.plantalot.fragments;

import android.os.Bundle;
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

import com.plantalot.R;
import com.plantalot.adapters.DrawerAdapter;
import com.plantalot.utils.Consts;
import com.plantalot.utils.NavigationIconClickListener;
import com.plantalot.adapters.OrtiAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class HomeFragment extends Fragment {
	
	private final Map<String, Map<String, List<Integer>>> mGiardini = new HashMap<>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		Collections.shuffle(Consts.plants_icons, new Random(System.currentTimeMillis()));
		
		mGiardini.put("Trento", new HashMap<>());
		mGiardini.put("Bergamo", new HashMap<>());
		mGiardini.put("Belluno", new HashMap<>());
		
		for (int i = 1, a = 0; i <= Consts.CARD_PLANTS + 2; i++, a += i) {
			mGiardini.get("Trento").put("Orto " + i, Consts.plants_icons.subList(a, a + i));
		}
		mGiardini.get("Belluno").put("Back yard", Arrays.asList(R.mipmap.tomato_3944072, R.mipmap.cabbage_3944158, R.mipmap.garlic_3944096, R.mipmap.watermelon_3944164, R.mipmap.peanut_3944274, R.mipmap.asparagus_3944087, R.mipmap.beet_3944102, R.mipmap.chard_3944149, R.mipmap.broccoli_3944323, R.mipmap.artichoke_3944084, R.mipmap.melon_3944173, R.mipmap.carrot_3944093, R.mipmap.cauliflower_3944060, R.mipmap.cabbage_3944158, R.mipmap.kale_3944155, R.mipmap.cucumber_3944113, R.mipmap.onion_3944225, R.mipmap.beans_3944104, R.mipmap.soybean_3944107, R.mipmap.fennel_3944161, R.mipmap.strawberry_3944176, R.mipmap.lettuce_3944125, R.mipmap.corn_3944286, R.mipmap.eggplants_3944110, R.mipmap.melon_3944173, R.mipmap.chili_pepper_3944137, R.mipmap.pepper_3944075, R.mipmap.leek_3944259, R.mipmap.vegetable_3944143, R.mipmap.turnip_3944078, R.mipmap.shallot_3944099, R.mipmap.celery_3944146, R.mipmap.vegetable_3944152, R.mipmap.spinach_3944292, R.mipmap.melon_3944173, R.mipmap.pumpkin_3944344, R.mipmap.zucchini_3944064));
		mGiardini.get("Bergamo").put("Dietro casa", Arrays.asList(R.mipmap.cucumber_3944113, R.mipmap.onion_3944225, R.mipmap.beans_3944104, R.mipmap.fennel_3944161, R.mipmap.strawberry_3944176, R.mipmap.lettuce_3944125, R.mipmap.corn_3944286, R.mipmap.eggplants_3944110, R.mipmap.melon_3944173, R.mipmap.chili_pepper_3944137, R.mipmap.pepper_3944075, R.mipmap.leek_3944259, R.mipmap.vegetable_3944143, R.mipmap.turnip_3944078, R.mipmap.shallot_3944099, R.mipmap.celery_3944146, R.mipmap.vegetable_3944152, R.mipmap.spinach_3944292, R.mipmap.melon_3944173, R.mipmap.pumpkin_3944344, R.mipmap.zucchini_3944064));
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		setUpRecyclerView(view);
		setUpToolbar(view);
		return view;
	}
	
	private void setUpRecyclerView(@NonNull View view) {
		String key = "Trento";
		TextView title = view.findViewById(R.id.home_fl_title_giardino);
		title.setText(key);
		
		RecyclerView giardiniRecyclerView = view.findViewById(R.id.home_bl_drawer_recycler);
		giardiniRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		DrawerAdapter giardiniAdapter = new DrawerAdapter(getActivity(), new ArrayList<>(mGiardini.keySet()));
		giardiniRecyclerView.setAdapter(giardiniAdapter);
		
		RecyclerView ortiRecyclerView = view.findViewById(R.id.home_fl_recycler_orti);
		ortiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtiAdapter ortiAdapter = new OrtiAdapter(mGiardini.get(key));
		ortiRecyclerView.setAdapter(ortiAdapter);
	}
	
	private void setUpToolbar(@NonNull View view) {
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
