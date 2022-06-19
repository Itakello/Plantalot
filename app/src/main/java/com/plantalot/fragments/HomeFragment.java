package com.plantalot.fragments;

import android.os.Bundle;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.plantalot.R;
import com.plantalot.adapters.HomeDrawerAdapter;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.User;
import com.plantalot.animations.NavigationIconClickListener;
import com.plantalot.adapters.HomeOrtiAdapter;
import com.plantalot.components.CircleButton;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

	private static final String TAG = "HomeFragment";
	private static Giardino giardino;
	private static User user;
	private FirebaseAuth mAuth;

	private List<CircleButton> mButtons;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mAuth = FirebaseAuth.getInstance();
//		mAuth.useEmulator("0.0.0.0", 9099);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		initializeUI(view);

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
		mButtons.add(new CircleButton("Tutte le piante", R.drawable.ic_iconify_carrot_24, R.id.action_goto_all_plants));
		mButtons.add(new CircleButton("Le mie piante", R.drawable.ic_iconify_sprout_24));
		mButtons.add(new CircleButton("Visualizza carriola", R.drawable.ic_round_wheelbarrow_24));
		mButtons.add(new CircleButton("Disponi giardino", R.drawable.ic_round_auto_24));
		mButtons.add(new CircleButton("Aggiungi orto", R.drawable.ic_round_add_big_24));
		Bundle bundle = new Bundle();
		bundle.putString("ortaggio", "Aglio");
		mButtons.add(new CircleButton("Casuale", R.drawable.ic_round_casino_24, R.id.action_goto_ortaggio, bundle));
	}

	public void updateUI(@NonNull View view, FirebaseUser fUser, User user, String nomeGiardino){ //FIXME delete 1 user
		giardino = user.getGiardinoByName(nomeGiardino);

		TextView idView = view.findViewById(R.id.anonymousStatusId);
		idView.setText("User ID: " + fUser.getUid());

		RecyclerView giardiniRecyclerView = view.findViewById(R.id.home_bl_drawer_recycler);
		giardiniRecyclerView.setAdapter(new HomeDrawerAdapter(getActivity(), user.getGiardiniNames(), view));

		TextView instructions = view.findViewById(R.id.instructions);
		if(giardino == null){
			instructions.setText("NO giardino");
		}else{
			instructions.setText("SI giardino");
			CircleButton.setRecycler(mButtons, view.findViewById(R.id.home_fl_recycler_navbuttons), getContext());
			TextView title = view.findViewById(R.id.home_fl_title_giardino);
			title.setText(giardino.getNome());

			RecyclerView ortiRecyclerView = view.findViewById(R.id.home_fl_recycler_orti);
			HomeOrtiAdapter homeOrtiAdapter = new HomeOrtiAdapter(giardino);
			ortiRecyclerView.setAdapter(homeOrtiAdapter);
		}
	}
}
