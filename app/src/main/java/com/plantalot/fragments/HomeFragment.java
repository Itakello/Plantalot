package com.plantalot.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.R;
import com.plantalot.adapters.DrawerAdapter;
import com.plantalot.utils.NavigationIconClickListener;
import com.plantalot.adapters.OrtiAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {
	
	private final Map<String, Map<String, List<Integer>>> mGiardini = new HashMap<>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		mGiardini.put("Trento", new HashMap<>());
		mGiardini.put("Bergamo", new HashMap<>());
		mGiardini.put("Belluno", new HashMap<>());
		
//		mGiardini.get("Trento").put("Back yard", Arrays.asList(R.drawable.tomato_3944072, R.drawable.cabbage_3944158, R.drawable.garlic_3944096, R.drawable.watermelon_3944164, R.drawable.peanut_3944274, R.drawable.asparagus_3944087, R.drawable.beet_3944102, R.drawable.chard_3944149, R.drawable.broccoli_3944323, R.drawable.artichoke_3944084, R.drawable.melon_3944173, R.drawable.carrot_3944093, R.drawable.cauliflower_3944060, R.drawable.cabbage_3944158, R.drawable.brussels_sprouts_4977368, R.drawable.kale_3944155, R.drawable.cucumber_3944113, R.drawable.onion_3944225, R.drawable.green_onion_4788140, R.drawable.chives_4788105, R.drawable.green_beans_4977341, R.drawable.beans_3944104, R.drawable.soybean_3944107, R.drawable.fennel_3944161, R.drawable.strawberry_3944176, R.drawable.lettuce_3944125, R.drawable.corn_3944286, R.drawable.eggplants_3944110, R.drawable.melon_3944173, R.drawable.okra_4977475, R.drawable.chili_pepper_3944137, R.drawable.pepper_3944075, R.drawable.green_pea_4977534, R.drawable.leek_3944259, R.drawable.parsley_4788183, R.drawable.vegetable_3944143, R.drawable.turnip_3944078, R.drawable.arugula_4788082, R.drawable.shallot_3944099, R.drawable.celery_3944146, R.drawable.vegetable_3944152, R.drawable.spinach_3944292, R.drawable.melon_3944173, R.drawable.pumpkin_3944344, R.drawable.zucchini_3944064));
		mGiardini.get("Trento").put("Front yard", Arrays.asList(R.drawable.cucumber_3944113, R.drawable.onion_3944225, R.drawable.beans_3944104, R.drawable.fennel_3944161, R.drawable.strawberry_3944176, R.drawable.lettuce_3944125, R.drawable.corn_3944286, R.drawable.eggplants_3944110, R.drawable.melon_3944173, R.drawable.okra_4977475, R.drawable.chili_pepper_3944137, R.drawable.pepper_3944075, R.drawable.green_pea_4977534, R.drawable.leek_3944259, R.drawable.parsley_4788183, R.drawable.vegetable_3944143, R.drawable.turnip_3944078, R.drawable.arugula_4788082, R.drawable.shallot_3944099, R.drawable.celery_3944146, R.drawable.vegetable_3944152, R.drawable.spinach_3944292, R.drawable.melon_3944173, R.drawable.pumpkin_3944344, R.drawable.zucchini_3944064));
		mGiardini.get("Trento").put("Orto 1", Arrays.asList(R.drawable.tomato_3944072));
		mGiardini.get("Trento").put("Orto 2", Arrays.asList(R.drawable.onion_3944225, R.drawable.beans_3944104));
		mGiardini.get("Trento").put("Orto 3", Arrays.asList(R.drawable.fennel_3944161, R.drawable.strawberry_3944176, R.drawable.lettuce_3944125));
		mGiardini.get("Trento").put("Orto 4", Arrays.asList(R.drawable.corn_3944286, R.drawable.eggplants_3944110, R.drawable.melon_3944173, R.drawable.chili_pepper_3944137));
		mGiardini.get("Trento").put("Orto 5", Arrays.asList(R.drawable.pepper_3944075, R.drawable.leek_3944259, R.drawable.vegetable_3944143, R.drawable.pumpkin_3944344, R.drawable.cabbage_3944158));
		mGiardini.get("Trento").put("Orto 6", Arrays.asList(R.drawable.turnip_3944078, R.drawable.shallot_3944099, R.drawable.celery_3944146, R.drawable.vegetable_3944152, R.drawable.melon_3944173, R.drawable.zucchini_3944064));
		mGiardini.get("Trento").put("Orto 7", Arrays.asList(R.drawable.shallot_3944099, R.drawable.celery_3944146, R.drawable.vegetable_3944152, R.drawable.spinach_3944292, R.drawable.melon_3944173, R.drawable.pumpkin_3944344, R.drawable.zucchini_3944064));
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		setUpRecyclerView(view);
		setUpToolbar(view);
		return view;
	}
	
	private void setUpRecyclerView(@NonNull View view) {
		RecyclerView giardiniRecyclerView = view.findViewById(R.id.recycler_view_giardini);
		giardiniRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		DrawerAdapter giardiniAdapter = new DrawerAdapter(getActivity(), new ArrayList<>(mGiardini.keySet()));
		giardiniRecyclerView.setAdapter(giardiniAdapter);
		
		RecyclerView ortiRecyclerView = view.findViewById(R.id.recycler_home_orti);
		ortiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtiAdapter ortiAdapter = new OrtiAdapter(mGiardini.get("Trento"));
		ortiRecyclerView.setAdapter(ortiAdapter);
	}
	
	private void setUpToolbar(@NonNull View view) {
		Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		
		if (activity != null) {
			activity.setSupportActionBar(toolbar);
		}
		
		final LinearLayout drawer = (LinearLayout) view.findViewById(R.id.navigation_drawer);
		drawer.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

		// animation
		toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
				getContext(),
				view.findViewById(R.id.backdrop_front_layer),
				new AccelerateDecelerateInterpolator(),
				getContext().getResources().getDrawable(R.drawable.ic_round_menu_24),
				getContext().getResources().getDrawable(R.drawable.ic_round_close_24),
				drawer.getMeasuredHeight()));
		
	}
	
	// Mostra il menu della app_bar
	@Override
	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
		getActivity().getMenuInflater().inflate(R.menu.top_app_bar, menu);
	}
	
}
