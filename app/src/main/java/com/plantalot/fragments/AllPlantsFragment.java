package com.plantalot.fragments;

import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.plantalot.R;
import com.plantalot.adapters.HomeDrawerAdapter;
import com.plantalot.adapters.HomeOrtiAdapter;
import com.plantalot.adapters.OrtaggioCardListAdapter;
import com.plantalot.animations.NavigationIconClickListener;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.User;
import com.plantalot.components.CircleButton;
import com.plantalot.database.Db;
import com.plantalot.database.DbStrings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class AllPlantsFragment extends Fragment {
	
	private Giardino giardino;
	private static User user;
	
	public static AllPlantsFragment newInstance() {
		AllPlantsFragment myFrag = new AllPlantsFragment();
		Bundle args = new Bundle();
		args.putParcelable("User", user);
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
			user = bundle.getParcelable("User");
		} else {
			user = new User("Giacomo");
		}
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.all_plants_fragment, container, false);
		setUpRecyclerView(view);
		setUpToolbar(view);
		return view;
	}
	
	private void setUpRecyclerView(@NonNull View view) {
		RecyclerView giardiniRecyclerView = view.findViewById(R.id.all_plants_bl_drawer_recycler);
		giardiniRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		
		// FIXME
		if (user.giardini.size() > 0) {
			HomeDrawerAdapter giardiniAdapter = new HomeDrawerAdapter(getActivity(), user.getGiardiniNames());
			giardiniRecyclerView.setAdapter(giardiniAdapter);
		}
		
//		List<Pair<String, List<String>>> cards = new ArrayList();
//		for (int i = 0; i < 5; i++) {
//			cards.add(new Pair<>("Caricamento...", new ArrayList<>(Arrays.asList(new String[5]))));
//		}
		
		RecyclerView cardsRecyclerView = view.findViewById(R.id.all_plants_fl_card_list_recycler);
		cardsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtaggioCardListAdapter ortaggioCardListAdapter = new OrtaggioCardListAdapter(Db.cards);
		cardsRecyclerView.setAdapter(ortaggioCardListAdapter);
		
	}
	
	private void setUpToolbar(@NonNull View view) {
		Toolbar toolbar = view.findViewById(R.id.all_plants_bl_toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		
		if (activity != null) {
			activity.setSupportActionBar(toolbar);
		}
		
		final LinearLayout drawer = view.findViewById(R.id.all_plants_bl_drawer);
		drawer.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		
		// animation
		toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
				getContext(),
				view.findViewById(R.id.all_plants_backdrop_frontlayer),
				new AccelerateDecelerateInterpolator(),
				R.drawable.ic_round_menu_24,
				R.drawable.ic_round_close_24,
				drawer.getMeasuredHeight()));
	}
	
	// Show appbar right menu
	@Override
	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
		getActivity().getMenuInflater().inflate(R.menu.all_plants_bl_toolbar_menu, menu);
	}
}
