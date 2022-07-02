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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import java.util.List;


public class HomeFragment<x> extends Fragment {
	
	private static final String TAG = "HomeFragment";
	private static String nome_giardino;
	public static User user;
	private static List<Pair<CircleButton, Boolean>> mButtons;

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(Consts.KEY_GIARDINO, nome_giardino);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if(savedInstanceState != null) {
			nome_giardino = savedInstanceState.getString(Consts.KEY_GIARDINO);
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
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		initializeUI(view);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		View v = this.requireView();
		FirebaseAuth mAuth = FirebaseAuth.getInstance();
		FirebaseUser currentUser = mAuth.getCurrentUser();
		if (currentUser == null) {
			// User not signed in
			mAuth.signInAnonymously()
					.addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							Log.d(TAG, "Signed in anonymously");
							getUserFromFirebase(mAuth.getCurrentUser(), v);
						}
					});
		} else {
			// User signed in
			getUserFromFirebase(currentUser, v);
		}
	}

	private User getUserFromFirebase(FirebaseUser firebaseUser, View v) {
		String uid = firebaseUser.getUid();
		DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
		DatabaseReference userRef = rootRef.child("users").child(uid);
		userRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if(!snapshot.exists()){
					user = DbUsers.writeNewUser("default_username", "default_email");
				}else{
					user = snapshot.getValue(User.class);
				}
				updateUI(v);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.e(TAG, "Error on checking existence user", error.toException());
			}
		});
		return null;
	}

	// Show appbar right menu
	@Override
	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
		getActivity().getMenuInflater().inflate(R.menu.home_bl_toolbar_menu, menu);
	}

	private void initializeUI(@NonNull View view){
		// Setup giardini recycler view
		RecyclerView giardiniRecyclerView = view.findViewById(R.id.home_bl_drawer_recycler);
		giardiniRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		
		setUpToolbar(view);
		
		// Setup orti recycler view
		RecyclerView ortiRecyclerView = view.findViewById(R.id.home_fl_recycler_orti);
		ortiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		// Add link to new_garden fragment
		Button new_garden = view.findViewById(R.id.nuovo_giardino);
		new_garden.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Navigation.findNavController(view).navigate(R.id.action_goto_newGarden);
			}
		});

		setUpCircleButtons();
	}
	
	private void setUpToolbar(@NonNull View view) {
		Toolbar toolbar = view.findViewById(R.id.home_bl_toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		
		if (activity != null) {
			activity.setSupportActionBar(toolbar);
		}
		
		// Setup listener + animation
		toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
				getContext(),
				view.findViewById(R.id.home_backdrop_frontlayer),
				new AccelerateDecelerateInterpolator(),
				R.drawable.ic_round_menu_24,
				R.drawable.ic_round_close_24,
				view.findViewById(R.id.home_bl_drawer)));
	}

	private void setUpCircleButtons(){
		mButtons = new ArrayList<>();
		mButtons.add(new Pair(new CircleButton("Tutte le piante", R.drawable.ic_iconify_carrot_24, R.id.action_goto_all_plants), true));
		mButtons.add(new Pair(new CircleButton("Le mie piante", R.drawable.ic_iconify_sprout_24), false));
		mButtons.add(new Pair(new CircleButton("Guarda carriola", R.drawable.ic_round_wheelbarrow_24), false));
		mButtons.add(new Pair(new CircleButton("Disponi giardino", R.drawable.ic_round_auto_24), false));
		mButtons.add(new Pair(new CircleButton("Aggiungi orto", R.drawable.ic_round_add_big_24), true));
		Bundle bundle = new Bundle();
		bundle.putString("ortaggio", "Aglio");
		bundle.putInt("prev_fragment", R.id.homeFragment);
		mButtons.add(new Pair(new CircleButton("Casuale", R.drawable.ic_round_casino_24, R.id.action_goto_ortaggio, bundle), true));
	}

	public static void updateUI(@NonNull View view){
		if(user == null){
			Log.w(TAG, "UpdateUI with null user");
			return;
		}
		Log.d(TAG, "Updating user " + user.getUsername());

		FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
		TextView idView = view.findViewById(R.id.anonymousStatusId);
		idView.setText("User ID: " + firebaseUser.getUid());

		RecyclerView giardiniRecyclerView = view.findViewById(R.id.home_bl_drawer_recycler);
		giardiniRecyclerView.setAdapter(new HomeDrawerAdapter(view.getContext(), user.getGiardiniNames(), view));

		TextView instructions = view.findViewById(R.id.instructions);
		TextView title = view.findViewById(R.id.home_fl_title_giardino);

		title.setVisibility(View.VISIBLE); // FIXME
		instructions.setVisibility(View.VISIBLE); // FIXME
		nome_giardino = user.getNome_giardino_selected();
		if(nome_giardino == null){
			instructions.setText(R.string.instruction_no_giardini);
			title.setVisibility(View.INVISIBLE); // FIXME
		}else{
			Giardino giardino = user.getGiardino(nome_giardino);
			boolean no_orti = giardino.orti.size() == 0;

			if(no_orti)
				instructions.setText(R.string.instruction_no_orti);
			else
				instructions.setVisibility(View.INVISIBLE); // FIXME

			List<CircleButton> buttonList = getCircleButtons(mButtons, !no_orti);
			CircleButton.setRecycler(buttonList, view.findViewById(R.id.home_fl_recycler_navbuttons), view.getContext());
			title.setText(giardino.getName());

			RecyclerView ortiRecyclerView = view.findViewById(R.id.home_fl_recycler_orti);
			HomeOrtiAdapter homeOrtiAdapter = new HomeOrtiAdapter(giardino);
			ortiRecyclerView.setAdapter(homeOrtiAdapter);
		}
	}

	public static List<CircleButton> getCircleButtons(List<Pair<CircleButton, Boolean>> mButtons, boolean has_orti){
		List<CircleButton> tmpList = new ArrayList<>();
		for(Pair<CircleButton, Boolean> button : mButtons){
			if(has_orti || button.second)
				tmpList.add(button.first);
		}
		return tmpList;
	}
}
