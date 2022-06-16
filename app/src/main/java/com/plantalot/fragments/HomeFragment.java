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
import com.plantalot.adapters.HomeDrawerAdapter;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.User;
import com.plantalot.animations.NavigationIconClickListener;
import com.plantalot.adapters.HomeOrtiAdapter;
import com.plantalot.components.CircleButton;
import com.plantalot.database.Db;

import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {
	
	private static Giardino giardino;
	private static User user;
	private final List<CircleButton> mButtons = Arrays.asList(
			new CircleButton("Tutte le piante", R.drawable.ic_iconify_carrot_24),
			new CircleButton("Le mie piante", R.drawable.ic_iconify_sprout_24),
			new CircleButton("Visualizza carriola", R.drawable.ic_round_wheelbarrow_24),
			new CircleButton("Disponi giardino", R.drawable.ic_round_auto_24),
//			new CircleButton("Aggiungi orto", R.drawable.ic_round_add_big_24),
			new CircleButton("Casuale (↑↑↑)", R.drawable.ic_round_casino_24));  // FIXME
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		/*Bundle bundle = this.getArguments();
		if (bundle != null) {
			System.out.println("User preso dal bundle");
			user = bundle.getParcelable("User");
		} else {
			user = new User("Giacomo");
		}*/
		user = new User("Giacomo");
		giardino = (user.giardini.size() > 0) ? user.giardini.get(0) : null;
		
		Db db = new Db();  // FIXME !!!!!
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		setUpBackLayer(view);
		setUpGiardino(view);
		setUpRecyclerView(view);
		setUpToolbar(view);

		return view;
	}
	
	private void setUpRecyclerView(@NonNull View view){
		RecyclerView ortiRecyclerView = view.findViewById(R.id.home_fl_recycler_orti);
		ortiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		CircleButton.setRecycler(mButtons, view.findViewById(R.id.home_fl_recycler_navbuttons), getContext());
	}

	private static void setUpGiardino(@NonNull View view){
		TextView title = view.findViewById(R.id.home_fl_title_giardino);
		title.setText(giardino.getNome());

		RecyclerView ortiRecyclerView = view.findViewById(R.id.home_fl_recycler_orti);
		HomeOrtiAdapter homeOrtiAdapter = new HomeOrtiAdapter(giardino);
		ortiRecyclerView.setAdapter(homeOrtiAdapter);
	}

	// used in HomeDrawerAdapter
	public static void setUpGiardino(@NonNull View view, String newGiardino){
		giardino = user.getGiardinoByName(newGiardino);
		assert giardino != null: "Invalid name from home drawer adapter";
		setUpGiardino(view);
	}

	private void setUpToolbar(@NonNull View view) {
		Toolbar toolbar = view.findViewById(R.id.home_bl_toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();

		if (activity != null) {
			activity.setSupportActionBar(toolbar);
		}
		
		final LinearLayout drawer = view.findViewById(R.id.home_bl_drawer);
		drawer.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		
		// Setup animation
		toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
				getContext(),
				view.findViewById(R.id.home_backdrop_frontlayer),
				new AccelerateDecelerateInterpolator(),
				R.drawable.ic_round_menu_24,
				R.drawable.ic_round_close_24,
				drawer.getMeasuredHeight()));
	}

	private void setUpBackLayer(@NonNull View view) {
		RecyclerView giardiniRecyclerView = view.findViewById(R.id.home_bl_drawer_recycler);
		giardiniRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		// FIXME
		if (user.giardini.size() > 0) {
			HomeDrawerAdapter giardiniAdapter = new HomeDrawerAdapter(getActivity(), user.getGiardiniNames(), view);
			giardiniRecyclerView.setAdapter(giardiniAdapter);
		}
	}
	
	// Show appbar right menu
	@Override
	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
		getActivity().getMenuInflater().inflate(R.menu.home_bl_toolbar_menu, menu);
	}
}
