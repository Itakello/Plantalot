package com.plantalot.fragments;

import android.app.Activity;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

	private static final String TAG = "HomeFragment";

	private static List<Pair<CircleButton, Boolean>> mButtons;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if(savedInstanceState != null){ //FIXME
//			String message = savedInstanceState.getString("message");
//			Toast.makeText(getActivity(),message, Toast.LENGTH_LONG).show();
//		}
		setHasOptionsMenu(true);
//		Log.d(TAG, "User retrieved from DB with username " + user.getUsername());
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
		DbUsers.init(view);

		return view;
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

	public static void updateUI(@NonNull View view, User user){
		if(user == null)
			return;
//		View view = this.getView();
		Log.d(TAG, "Updating user " + user.getUsername());

		Log.d(TAG, view.toString());

		FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
		TextView idView = view.findViewById(R.id.anonymousStatusId);
		idView.setText("User ID: " + firebaseUser.getUid());

		RecyclerView giardiniRecyclerView = view.findViewById(R.id.home_bl_drawer_recycler);
		giardiniRecyclerView.setAdapter(new HomeDrawerAdapter(view.getContext(), user.getGiardiniNames(), view));

		TextView instructions = view.findViewById(R.id.instructions);
		TextView title = view.findViewById(R.id.home_fl_title_giardino);

		Giardino giardino = user.getCurrentGiardino();
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

			title.setText(giardino.getNome());

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
