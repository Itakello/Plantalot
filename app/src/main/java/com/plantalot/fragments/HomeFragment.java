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
import java.util.List;


public class HomeFragment extends Fragment {
	
	private static final String TAG = "HomeFragment";
<<<<<<< HEAD
	private SharedPreferences sharedPref;
	private Giardino giardino;
	private static List<Pair<CircleButton, Boolean>> mButtons;

=======
	private static Giardino giardino;
	private static User user;
	private FirebaseAuth mAuth;
	
	private final List<CircleButton> mButtons = Arrays.asList(
			new CircleButton("Tutte le piante", R.drawable.ic_iconify_carrot_24),
			new CircleButton("Le mie piante", R.drawable.ic_iconify_sprout_24),
			new CircleButton("Visualizza carriola", R.drawable.ic_round_wheelbarrow_24),
			new CircleButton("Disponi giardino", R.drawable.ic_round_auto_24),
//			new CircleButton("Aggiungi orto", R.drawable.ic_round_add_big_24),
			new CircleButton("Casuale\n", R.drawable.ic_round_casino_24));  // FIXME
	
>>>>>>> 1d6e065b4633e9abbdc6d4fa44fb7a1bdf38a59f
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if(savedInstanceState != null){ //FIXME
//			String message = savedInstanceState.getString("message");
//			Toast.makeText(getActivity(),message, Toast.LENGTH_LONG).show();
//		}
		setHasOptionsMenu(true);
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
	}
<<<<<<< HEAD

//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		outState.putString("message", "This is my message to be reloaded");
//		super.onSaveInstanceState(outState);
//	}

=======
	
>>>>>>> 1d6e065b4633e9abbdc6d4fa44fb7a1bdf38a59f
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		// TODO add loading bar
		initializeUI(view);
<<<<<<< HEAD
		String giardinoName = null;
//		if(savedInstanceState != null){
//			giardinoName = savedInstanceState.getString("giardino");
//		}
		DbUsers.init(view, giardinoName);

=======
		
		// Check if user is signed in (non-null) and update UI accordingly.
		mAuth.signInAnonymously()
				.addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						FirebaseUser currentUser = mAuth.getCurrentUser();
						User user = new User("Giacomo");
						updateUI(view, currentUser, user, "Belluno");
					}
				});
		
>>>>>>> 1d6e065b4633e9abbdc6d4fa44fb7a1bdf38a59f
		return view;
	}
	
	// Show appbar right menu
	@Override
	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
		getActivity().getMenuInflater().inflate(R.menu.home_bl_toolbar_menu, menu);
	}
<<<<<<< HEAD

	@Override
	public void onPause() {
		super.onPause();
		if(giardino != null)
			sharedPref.edit().putString("giardino", giardino.getName());
	}

	private void initializeUI(@NonNull View view){
=======
	
	private void initializeUI(@NonNull View view) {
>>>>>>> 1d6e065b4633e9abbdc6d4fa44fb7a1bdf38a59f
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
<<<<<<< HEAD

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

	public static void updateUI(@NonNull View view, User user, String nomeGiardino){
		if(user == null)
			return;
		Log.d(TAG, "Updating user " + user.getUsername());

		FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
		TextView idView = view.findViewById(R.id.anonymousStatusId);
		idView.setText("User ID: " + firebaseUser.getUid());

=======
	
	public void updateUI(@NonNull View view, FirebaseUser fUser, User user, String nomeGiardino) { //FIXME delete 1 user
		giardino = user.getGiardinoByName(nomeGiardino);

//		TextView idView = view.findViewById(R.id.anonymousStatusId);
//		idView.setText("User ID: " + fUser.getUid());
		
>>>>>>> 1d6e065b4633e9abbdc6d4fa44fb7a1bdf38a59f
		RecyclerView giardiniRecyclerView = view.findViewById(R.id.home_bl_drawer_recycler);
		giardiniRecyclerView.setAdapter(new HomeDrawerAdapter(view.getContext(), user.getGiardiniNames(), view));

<<<<<<< HEAD
		TextView instructions = view.findViewById(R.id.instructions);
		TextView title = view.findViewById(R.id.home_fl_title_giardino);

		Giardino giardino = user.getGiardino(nomeGiardino);

		title.setVisibility(View.VISIBLE); // FIXME
		instructions.setVisibility(View.VISIBLE); // FIXME

		if(giardino == null){
			instructions.setText(R.string.instruction_no_giardini);
			title.setVisibility(View.INVISIBLE); // FIXME
		}else{
			boolean no_orti = giardino.orti.size() == 0;

			if(no_orti)
				instructions.setText(R.string.instruction_no_orti);
			else
				instructions.setVisibility(View.INVISIBLE); // FIXME

			List<CircleButton> buttonList = getCircleButtons(mButtons, !no_orti);
			CircleButton.setRecycler(buttonList, view.findViewById(R.id.home_fl_recycler_navbuttons), view.getContext());
			Log.d(TAG, "Giardino name: " + giardino.getName());
			title.setText(giardino.getName());

			RecyclerView ortiRecyclerView = view.findViewById(R.id.home_fl_recycler_orti);
			HomeOrtiAdapter homeOrtiAdapter = new HomeOrtiAdapter(giardino);
			ortiRecyclerView.setAdapter(homeOrtiAdapter);
		}
=======
//		TextView instructions = view.findViewById(R.id.instructions);
//		if(giardino == null){
//			instructions.setText("NO giardino");
//		}else{
//			instructions.setText("SI giardino");
		CircleButton.setRecycler(mButtons, view.findViewById(R.id.home_fl_recycler_navbuttons), getContext());
		TextView title = view.findViewById(R.id.home_fl_title_giardino);
		title.setText(giardino.getNome());
		
		RecyclerView ortiRecyclerView = view.findViewById(R.id.home_fl_recycler_orti);
		HomeOrtiAdapter homeOrtiAdapter = new HomeOrtiAdapter(giardino);
		ortiRecyclerView.setAdapter(homeOrtiAdapter);
//		}
>>>>>>> 1d6e065b4633e9abbdc6d4fa44fb7a1bdf38a59f
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
