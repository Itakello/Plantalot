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
import com.plantalot.classes.Giardino;
import com.plantalot.classes.User;
import com.plantalot.animations.NavigationIconClickListener;
import com.plantalot.adapters.HomeOrtiAdapter;
import com.plantalot.adapters.CircleButtonsAdapter;

import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {
	
	private Giardino giardino;
	private static User user;
	private final List<Pair<String, Integer>> mDataButtons = Arrays.asList(
			new Pair<>("Tutte le piante", R.drawable.ic_iconify_carrot_24),
			new Pair<>("Le mie piante", R.drawable.ic_iconify_sprout_24),
			new Pair<>("Visualizza carriola", R.drawable.ic_round_wheelbarrow_24),
			new Pair<>("Disponi giardino", R.drawable.ic_round_auto_24),
			new Pair<>("Aggiungi orto", R.drawable.ic_round_add_big_24));
	
	public static HomeFragment newInstance() {
		HomeFragment myFrag = new HomeFragment();
		Bundle args = new Bundle();
		args.putParcelable("Userr", user);
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
			user = bundle.getParcelable("Userr");
		} else {
			user = new User("Giacomo");
		}
		if (user.giardini.size() > 0)
			giardino = user.giardini.get(0);
		else
			giardino = null;
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		setUpRecyclerView(view);
		setUpToolbar(view);
		return view;
	}
	
	private void setUpRecyclerView(@NonNull View view) {
		
		String key = giardino.getNome();
		TextView title = view.findViewById(R.id.home_fl_title_giardino);
		title.setText(key);
		
		RecyclerView giardiniRecyclerView = view.findViewById(R.id.home_bl_drawer_recycler);
		giardiniRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		
		// FIXME
		if (user.giardini.size() > 0) {
			HomeDrawerAdapter giardiniAdapter = new HomeDrawerAdapter(getActivity(), user.getGiardiniNames());
			giardiniRecyclerView.setAdapter(giardiniAdapter);
		}
		
		RecyclerView ortiRecyclerView = view.findViewById(R.id.home_fl_recycler_orti);
		ortiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		System.out.println(giardino);
		HomeOrtiAdapter homeOrtiAdapter = new HomeOrtiAdapter(giardino);
		ortiRecyclerView.setAdapter(homeOrtiAdapter);
		
		RecyclerView navbuttonsRecyclerView = view.findViewById(R.id.home_fl_recycler_navbuttons);
		CircleButtonsAdapter circleButtonsAdapter = new CircleButtonsAdapter(mDataButtons);
		FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
		flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
		navbuttonsRecyclerView.setLayoutManager(flexboxLayoutManager);
		navbuttonsRecyclerView.setAdapter(circleButtonsAdapter);
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
