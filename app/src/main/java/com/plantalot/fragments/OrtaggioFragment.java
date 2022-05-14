package com.plantalot.fragments;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.plantalot.R;
import com.plantalot.adapters.OrtaggioCardListAdapter;
import com.plantalot.adapters.OrtaggioCardRowAdapter;
import com.plantalot.adapters.OrtaggioSpecsAdapter;
import com.plantalot.classes.OrtaggioSpecs;
import com.plantalot.utils.Utils;

import java.util.Arrays;
import java.util.List;


public class OrtaggioFragment extends Fragment {
	
	private long dropdownDismissTime = 0;
	
	private final static List<OrtaggioSpecs> specs = Arrays.asList(
			new OrtaggioSpecs("Distanze", "40 × 100 cm", R.mipmap.specs_distanze_1462005, false),
			new OrtaggioSpecs("Mezz'ombra", "In estate", R.mipmap.specs_mezzombra_4496245, false),
			new OrtaggioSpecs("Raccolta", "80-100 giorni", R.mipmap.specs_raccolta_3078971, false),
			new OrtaggioSpecs("Produzione", "200 g/cad", R.mipmap.specs_produzione_741366, false),
			new OrtaggioSpecs("Rotazione", "4 anni", R.mipmap.specs_rotazione_4496256, false),
			new OrtaggioSpecs("Vaschetta", "6 piante", R.mipmap.specs_vaschetta_1655603, false),
			new OrtaggioSpecs("Concimazione", "Abbondante (organica)\nIn buca al trapianto\nMensile dopo il trapianto", R.mipmap.specs_concimazione_1670075, true),
			new OrtaggioSpecs("Irrigazione", "Abbondare a inizio allegagione e ingrossamento dei frutti\nSospendere prima della raccolta", R.mipmap.specs_irrigazione_3319229, true));
	
	private final static List<Pair<String, List<Pair<String, Integer>>>> cards = Arrays.asList(
			new Pair<>("Consociazioni utili", Arrays.asList(
					new Pair<>("Carota", R.mipmap.plant_carrot_3944093),
					new Pair<>("Zucca", R.mipmap.plant_pumpkin_3944344),
					new Pair<>("Cavolo", R.mipmap.plant_cabbage_3944158),
					new Pair<>("Porro", R.mipmap.plant_leek_3944259),
					new Pair<>("Bietola", R.mipmap.plant_chard_3944149))),
			new Pair<>("Consociazioni sfavorevoli", Arrays.asList(
					new Pair<>("Zucca", R.mipmap.plant_pumpkin_3944344),
					new Pair<>("Cavolo", R.mipmap.plant_cabbage_3944158),
					new Pair<>("Carota", R.mipmap.plant_carrot_3944093),
					new Pair<>("Bietola", R.mipmap.plant_chard_3944149))),
			new Pair<>("Rotazioni utili", Arrays.asList(
					new Pair<>("Carota", R.mipmap.plant_carrot_3944093),
					new Pair<>("Zucca", R.mipmap.plant_pumpkin_3944344),
					new Pair<>("Zucca", R.mipmap.plant_pumpkin_3944344),
					new Pair<>("Zucca", R.mipmap.plant_pumpkin_3944344),
					new Pair<>("Bietola", R.mipmap.plant_chard_3944149))),
			new Pair<>("Rotazioni sfavorevoli", Arrays.asList(
					new Pair<>("Porro", R.mipmap.plant_leek_3944259),
					new Pair<>("Carota", R.mipmap.plant_carrot_3944093),
					new Pair<>("Carota", R.mipmap.plant_carrot_3944093),
					new Pair<>("Carota", R.mipmap.plant_carrot_3944093),
					new Pair<>("Zucca", R.mipmap.plant_pumpkin_3944344),
					new Pair<>("Bietola", R.mipmap.plant_chard_3944149)))
	);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ortaggio_fragment, container, false);
		setupToolbar(view);
		setupDropdown(view);
		setupContent(view, container);
		return view;
	}
	
	private void setupToolbar(@NonNull View view) {
		MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		
		if (activity != null) {
			activity.setSupportActionBar(toolbar);
		}
		
		final ActionBar actionBar = activity.getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

//		toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
//				getContext(),
//				view.findViewById(R.id.home_backdrop_frontlayer),
//				new AccelerateDecelerateInterpolator(),
//				R.drawable.ic_round_menu_24,
//				R.drawable.ic_round_close_24,
//				drawer.getMeasuredHeight()));
	}
	
	private void setupContent(@NonNull View view, ViewGroup container) {
		
		// Expandable Text View
		ExpandableTextView expTv1 = view.findViewById(R.id.expand_text_view);
		expTv1.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris convallis nisi sed mi cursus maximus eget ac enim. Aenean a sodales lectus. Aenean erat ex, luctus a feugiat et, bibendum nec nibh. Nullam eget risus leo. Nulla facilisi. Proin iaculis consectetur elit et tempor. Pellentesque lacus metus, pulvinar et viverra eget, lobortis nec dui. In euismod eu magna facilisis suscipit. Sed ut imperdiet diam. Integer ut neque turpis. Aenean tortor mauris, convallis sed pellentesque at, interdum vel odio. Vivamus nec mollis nisl. Nulla eleifend congue venenatis. Etiam eget ex pulvinar, iaculis diam a, hendrerit velit. Morbi aliquet id mauris dapibus fermentum. Pellentesque pretium finibus blandit. Vivamus ut orci in lacus elementum suscipit eget sed purus. Integer augue lectus, consectetur sit amet velit quis, auctor dignissim lacus. Integer suscipit pulvinar justo a condimentum. Nam tincidunt pretium risus in ullamcorper. Aliquam efficitur, enim vitae consectetur sagittis, nunc leo porttitor dolor, et tristique metus ipsum sed erat. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut tellus nisl, dictum in justo nec, volutpat laoreet libero.");
		
		// Specs
		RecyclerView specsRecyclerView = view.findViewById(R.id.ortaggio_bl_specs_recycler);
		specsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtaggioSpecsAdapter ortaggioSpecsAdapter = new OrtaggioSpecsAdapter(specs);
		specsRecyclerView.setAdapter(ortaggioSpecsAdapter);
		
		// Cards
		RecyclerView cardsRecyclerView = view.findViewById(R.id.ortaggio_bl_card_list_recycler);
		cardsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtaggioCardListAdapter ortaggioCardListAdapter = new OrtaggioCardListAdapter(cards);
		cardsRecyclerView.setAdapter(ortaggioCardListAdapter);
		
	}
	
	private void setupDropdown(@NonNull View view) {
		View listPopupWindowButton = view.findViewById(R.id.ortaggio_fl_dropdown);
		View scrim = view.findViewById(R.id.ortaggio_fl_scrim);
		ImageView listPopupWindowButtonIcon = view.findViewById(R.id.ortaggio_fl_dropdown_icon);
		ListPopupWindow listPopupWindow = new ListPopupWindow(getContext(), null, com.google.android.material.R.attr.listPopupWindowStyle);
		listPopupWindow.setAnchorView(listPopupWindowButton);
		List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4"
//				, "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4"
		);
		ArrayAdapter adapter = new ArrayAdapter(requireContext(), R.layout.ortaggio_fl_dropdown_item, items);
		listPopupWindow.setAdapter(adapter);
		
		listPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bkg_dropdown_window));
		listPopupWindow.setVerticalOffset(0);
		listPopupWindow.setHeight(Math.min(items.size() * Utils.dp2px(40, getContext()), Utils.dp2px(600, getContext())));  // FIXME!!!


//		// Set list popup's item click listener
//		listPopupWindow.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
//		  // Respond to list popup window item click.
//
//		  // Dismiss popup
//		  listPopupWindow.dismiss()
//		}
		
		listPopupWindowButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (System.currentTimeMillis() - dropdownDismissTime > 200) {  // FIXME!!!
					listPopupWindow.show();
					listPopupWindowButtonIcon.setImageResource(R.drawable.ic_round_keyboard_arrow_up_24);
					scrim.animate().alpha(1.0f).setDuration(100);
				}
			}
		});
		
		listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				dropdownDismissTime = System.currentTimeMillis();
				listPopupWindowButtonIcon.setImageResource(R.drawable.ic_round_keyboard_arrow_down_24);
				scrim.animate().alpha(0.0f).setDuration(100);
			}
		});
	}
	
	// Show appbar right menu
	@Override
	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
		getActivity().getMenuInflater().inflate(R.menu.ortaggio_fl_toolbar_menu, menu);
	}
	
}
