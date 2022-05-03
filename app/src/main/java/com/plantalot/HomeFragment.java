package com.plantalot;

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

import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {
	
	private final List<String> mDataset = Arrays.asList("Trento", "Bergamo", "Belluno");
	private final List<String> mDataset2 = Arrays.asList("Back yard", "Front yard", "Nice yard");
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
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
		RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		DrawerAdapter mAdapter = new DrawerAdapter(getActivity(), mDataset);
//        mAdapter.setClickListener(getActivity());
		mRecyclerView.setAdapter(mAdapter);
		
		RecyclerView mRecyclerView2 = view.findViewById(R.id.recycler_view2);
		mRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtiAdapter mAdapter2 = new OrtiAdapter(getActivity(), mDataset2);
//        mAdapter.setClickListener(getActivity());
		mRecyclerView2.setAdapter(mAdapter2);
	}
	
	private void setUpToolbar(@NonNull View view) {
		Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		
		if (activity != null) {
			activity.setSupportActionBar(toolbar);
		}
		
		final LinearLayout drawer = (LinearLayout) view.findViewById(R.id.navigation_drawer);
		drawer.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		
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
