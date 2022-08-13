package com.plantalot.fragments;

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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.plantalot.R;
import com.plantalot.adapters.HomeDrawerAdapter;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.User;
import com.plantalot.animations.NavigationIconClickListener;
import com.plantalot.adapters.HomeOrtiAdapter;
import com.plantalot.components.CircleButton;
import com.plantalot.database.DbUsers;
import com.plantalot.utils.Consts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {
	
	private static final String TAG = "HomeFragment";
	private static String nomeGiardinoCorrente;
	public static User user;  // FIXME !!??
	private View view;
	
	private static final List<Pair<CircleButton, Boolean>> mButtons = new ArrayList<>(Arrays.asList(
			new Pair<>(new CircleButton("Tutte le piante", R.drawable.ic_iconify_carrot_24, R.id.action_goto_all_plants), true),
			new Pair<>(new CircleButton("Visualizza carriola", R.drawable.ic_round_wheelbarrow_24, R.id.action_goto_carriola), true),
			new Pair<>(new CircleButton("Le mie piante", R.drawable.ic_iconify_sprout_24), false),
			new Pair<>(new CircleButton("Guarda carriola", R.drawable.ic_round_wheelbarrow_24), false),
			new Pair<>(new CircleButton("Disponi giardino", R.drawable.ic_round_auto_24), false),
			new Pair<>(new CircleButton("Aggiungi orto", R.drawable.ic_round_add_big_24, R.id.action_goto_nuovo_orto), true)
	));
	
	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(Consts.KEY_GIARDINO, nomeGiardinoCorrente);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (savedInstanceState != null) {
			nomeGiardinoCorrente = savedInstanceState.getString(Consts.KEY_GIARDINO);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "On create");
//
//		Bundle bundle = this.getArguments();
//		if (bundle != null) {
//			Log.d(TAG, "Receiving from bundle");
//			nome_giardino = bundle.getString(Consts.KEY_GIARDINO);
//		}
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "On createView");
		view = inflater.inflate(R.layout.home_fragment, container, false);
		initializeUI();
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		FirebaseAuth mAuth = FirebaseAuth.getInstance();
		FirebaseUser currentUser = mAuth.getCurrentUser();
		if (currentUser == null) {  // User not signed in
			mAuth.signInAnonymously().addOnCompleteListener(this.getActivity(), task -> {
				Log.d(TAG, "Signed in anonymously");
				getUserFromFirebase(mAuth.getCurrentUser());
			});
		} else {  // User signed in
			getUserFromFirebase(currentUser);
		}
	}
	
	private void getUserFromFirebase(FirebaseUser firebaseUser) {
		String uid = firebaseUser.getUid();
		DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
		DatabaseReference userRef = rootRef.child("users").child(uid);
		userRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (!snapshot.exists()) {
					user = DbUsers.writeNewUser("default_username", "default_email");
				} else {
					user = snapshot.getValue(User.class);
				}
				updateUI();
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.e(TAG, "Error on checking existence user", error.toException());
			}
		});
	}
	
	// Show appbar right menu
	@Override
	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
		getActivity().getMenuInflater().inflate(R.menu.home_bl_toolbar_menu, menu);
	}
	
	private void initializeUI() {
		// Setup giardini recycler view
		RecyclerView giardiniRecyclerView = view.findViewById(R.id.home_bl_drawer_recycler);
		giardiniRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		
		setUpToolbar(view);
		
		// Setup orti recycler view
		RecyclerView ortiRecyclerView = view.findViewById(R.id.home_fl_recycler_orti);
		ortiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		
		// Add link to new_garden fragment
		Button new_garden = view.findViewById(R.id.nuovo_giardino);
		new_garden.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_goto_nuovo_giardino));
	}
	
	private void setUpToolbar(@NonNull View view) {
		Toolbar toolbar = view.findViewById(R.id.home_bl_toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		
		if (activity != null) activity.setSupportActionBar(toolbar);
		
		// Setup listener + animation
		toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
				getContext(),
				view.findViewById(R.id.home_backdrop_frontlayer),
				new AccelerateDecelerateInterpolator(),
				R.drawable.ic_round_menu_24,
				R.drawable.ic_round_close_24,
				view.findViewById(R.id.home_bl_drawer)
		));
	}
	
	private void updateUI() {
		if (user == null) return;
		Log.d(TAG, "Updating user " + user.getUsername());
		
		FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
		TextView idView = view.findViewById(R.id.anonymousStatusId);
		idView.setText("User ID: " + firebaseUser.getUid());
		idView.setVisibility(View.GONE);
		
		RecyclerView giardiniRecyclerView = view.findViewById(R.id.home_bl_drawer_recycler);
		giardiniRecyclerView.setAdapter(new HomeDrawerAdapter(view.getContext(), user.getGiardiniNames(), view));
		
		TextView instructions = view.findViewById(R.id.instructions);
		TextView title = view.findViewById(R.id.home_fl_title_giardino);
		
		title.setVisibility(View.VISIBLE); // FIXME
		instructions.setVisibility(View.VISIBLE); // FIXME
		nomeGiardinoCorrente = user.getNomeGiardinoCorrente();
		if (nomeGiardinoCorrente == null) {
			instructions.setText(R.string.instruction_no_giardini);
			title.setVisibility(View.GONE); // FIXME
		} else {
			Giardino giardino = user.getGiardino(nomeGiardinoCorrente);
			if (giardino.getOrti().isEmpty()) {
				instructions.setText(R.string.instruction_no_orti);
			} else {
				instructions.setVisibility(View.GONE); // FIXME
			}
			List<CircleButton> buttonList = new ArrayList<>();
			for (Pair<CircleButton, Boolean> button : mButtons) {
				if (button.second) buttonList.add(button.first);
			}
			CircleButton.setupRecycler(buttonList, view.findViewById(R.id.home_fl_recycler_navbuttons), view.getContext());
			title.setText(giardino.getName());
			
			RecyclerView ortiRecyclerView = view.findViewById(R.id.home_fl_recycler_orti);
			HomeOrtiAdapter homeOrtiAdapter = new HomeOrtiAdapter(giardino.getOrti());
			ortiRecyclerView.setAdapter(homeOrtiAdapter);
		}
	}
}
