// Clone di HomeFragment.java

package com.plantalot;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;


public class HomeActivity extends AppCompatActivity {
	
	private final List<String> mDataset = Arrays.asList("Trento", "Bergamo", "Belluno");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_fragment);
		setUpRecyclerView();
		setUpToolbar();
	}
	
	private void setUpRecyclerView() {
		RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		DrawerAdapter mAdapter = new DrawerAdapter(this, mDataset);
//        mAdapter.setClickListener(getActivity());
		mRecyclerView.setAdapter(mAdapter);
	}
	
	private void setUpToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
		setSupportActionBar(toolbar);
		
		final LinearLayout drawer = (LinearLayout) findViewById(R.id.navigation_drawer);
		drawer.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		
		toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
				this,
				findViewById(R.id.backdrop_front_layer),
				new AccelerateDecelerateInterpolator(),
				getResources().getDrawable(R.drawable.ic_round_menu_24),
				getResources().getDrawable(R.drawable.ic_round_close_24),
				drawer.getMeasuredHeight()));
		
	}
	
	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.top_app_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
