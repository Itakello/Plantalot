package com.plantalot.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.plantalot.R;
import com.plantalot.adapters.HomeDrawerAdapter;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.User;
import com.plantalot.animations.NavigationIconClickListener;
import com.plantalot.adapters.HomeOrtiAdapter;
import com.plantalot.components.CircleButton;
import com.plantalot.database.DbUsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {
	
	private static final String TAG = "HomeFragment";
	private SharedPreferences sharedPref;
	private Giardino giardino;
	
	private static final List<Pair<CircleButton, Boolean>> mButtons = new ArrayList<>(Arrays.asList(
			new Pair<>(new CircleButton("Tutte le piante", R.drawable.ic_iconify_carrot_24, R.id.action_goto_all_plants), true),
			new Pair<>(new CircleButton("Visualizza carriola", R.drawable.ic_round_wheelbarrow_24, R.id.action_goto_carriola), true),
			new Pair<>(new CircleButton("Le mie piante", R.drawable.ic_iconify_sprout_24), false),
			new Pair<>(new CircleButton("Guarda carriola", R.drawable.ic_round_wheelbarrow_24), false),
			new Pair<>(new CircleButton("Disponi giardino", R.drawable.ic_round_auto_24), false),
			new Pair<>(new CircleButton("Aggiungi orto", R.drawable.ic_round_add_big_24, R.id.action_goto_nuovo_orto), true)
	));
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if(savedInstanceState != null){ // FIXME
//			String message = savedInstanceState.getString("message");
//			Toast.makeText(getActivity(),message, Toast.LENGTH_LONG).show();
//		}
		setHasOptionsMenu(true);
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
	}

//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		outState.putString("message", "This is my message to be reloaded");
//		super.onSaveInstanceState(outState);
//	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		// TODO add loading bar
		initializeUI(view);
		
		String giardinoName = null;
//		if(savedInstanceState != null){
//			giardinoName = savedInstanceState.getString("giardino");
//		}
		DbUsers.init(view, giardinoName);
		return view;
	}
	
	@Override
	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
		getActivity().getMenuInflater().inflate(R.menu.home_bl_toolbar_menu, menu);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (giardino != null) sharedPref.edit().putString("giardino", giardino.getName());
	}
	
	private void initializeUI(@NonNull View view) {
		setUpToolbar(view);
		
		RecyclerView giardiniRecyclerView = view.findViewById(R.id.home_bl_drawer_recycler);
		giardiniRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		
		RecyclerView ortiRecyclerView = view.findViewById(R.id.home_fl_recycler_orti);
		ortiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		
		Button new_garden = view.findViewById(R.id.nuovo_giardino);
		new_garden.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_goto_nuovo_giardino));
	}
	
	private void setUpToolbar(@NonNull View view) {
		Toolbar toolbar = view.findViewById(R.id.home_bl_toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		
		if (activity != null) activity.setSupportActionBar(toolbar);
		
		toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
				getContext(),
				view.findViewById(R.id.home_backdrop_frontlayer),
				new AccelerateDecelerateInterpolator(),
				R.drawable.ic_round_menu_24,
				R.drawable.ic_round_close_24,
				view.findViewById(R.id.home_bl_drawer)
		));
	}
	
	public static void updateUI(@NonNull View view, User user, String nomeGiardino) {
		if (user == null) return;
		Log.d(TAG, "Updating user " + user.getUsername());
		
		FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
		TextView idView = view.findViewById(R.id.anonymousStatusId);
		idView.setText("User ID: " + firebaseUser.getUid());
		RecyclerView giardiniRecyclerView = view.findViewById(R.id.home_bl_drawer_recycler);
		giardiniRecyclerView.setAdapter(new HomeDrawerAdapter(view.getContext(), user.getGiardiniNames(), view));
		
		TextView instructions = view.findViewById(R.id.instructions);
		TextView title = view.findViewById(R.id.home_fl_title_giardino);
		
		Giardino giardino = user.getGiardino(nomeGiardino);
		
		title.setVisibility(View.VISIBLE); // FIXME
		instructions.setVisibility(View.VISIBLE); // FIXME
		
		if (giardino == null) {
			instructions.setText(R.string.instruction_no_giardini);
			title.setVisibility(View.GONE); // FIXME
		} else {
			if (giardino.orti.isEmpty()) {
				instructions.setText(R.string.instruction_no_orti);
			} else {
				instructions.setVisibility(View.GONE); // FIXME
			}
			List<CircleButton> buttonList = new ArrayList<>();
			for (Pair<CircleButton, Boolean> button : mButtons) {
				if (button.second) buttonList.add(button.first);
			}
			CircleButton.setupRecycler(buttonList, view.findViewById(R.id.home_fl_recycler_navbuttons), view.getContext());
			Log.d(TAG, "Giardino name: " + giardino.getName());
			title.setText(giardino.getName());
			
			RecyclerView ortiRecyclerView = view.findViewById(R.id.home_fl_recycler_orti);
			HomeOrtiAdapter homeOrtiAdapter = new HomeOrtiAdapter(giardino);
			ortiRecyclerView.setAdapter(homeOrtiAdapter);
		}
	}
}
