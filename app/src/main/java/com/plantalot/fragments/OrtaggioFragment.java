package com.plantalot.fragments;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.plantalot.R;
import com.plantalot.adapters.CircleButtonsAdapter;
import com.plantalot.adapters.OrtaggioCardListAdapter;
import com.plantalot.adapters.OrtaggioSpecsAdapter;
import com.plantalot.classes.OrtaggioSpecs;
import com.plantalot.utils.ColorUtils;

import java.util.Arrays;
import java.util.List;


public class OrtaggioFragment extends Fragment {
	
	private long dropdownDismissTime = 0;
	
	private final static String[] months = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII"};
	private final static Boolean[] months_bs = {false, false, true, true, true, true, true, true, true, false, false, false};
	
	private final static List<OrtaggioSpecs> specs = Arrays.asList(
			new OrtaggioSpecs("Distanze", "40 × 100 cm", R.mipmap.specs_distanze_1462005, false),
			new OrtaggioSpecs("Mezz'ombra", "In estate", R.mipmap.specs_mezzombra_4496245, false),
			new OrtaggioSpecs("Raccolta", "80-100 giorni", R.mipmap.specs_raccolta_3078971, false),
			new OrtaggioSpecs("Produzione", "200 g/cad", R.mipmap.specs_produzione_741366, false),
			new OrtaggioSpecs("Rotazione", "4 anni", R.mipmap.specs_rotazione_4496256, false),
			new OrtaggioSpecs("Vaschetta", "6 piante", R.mipmap.specs_vaschetta_1655603, false),
			new OrtaggioSpecs("Concimazione", "Abbondante (organica)\nIn buca al trapianto\nMensile dopo il trapianto", R.mipmap.specs_concimazione_1670075, true),
			new OrtaggioSpecs("Irrigazione", "Abbondare a inizio allegagione e ingrossamento dei frutti\nSospendere prima della raccolta", R.mipmap.specs_irrigazione_3319229, true));
	
	private final static List<Pair<String, List<Pair<String, Integer>>>> cards1 = Arrays.asList(
			new Pair<>("Consociazioni utili", Arrays.asList(
					new Pair<>("Carota", R.mipmap.plant_carrot_3944093),
					new Pair<>("Zucca", R.mipmap.plant_pumpkin_3944344),
					new Pair<>("Cavolo", R.mipmap.plant_cabbage_3944158),
					new Pair<>("Porro", R.mipmap.plant_leek_3944259),
					new Pair<>("Bietola", R.mipmap.plant_chard_3944149),
					new Pair<>("Canapa", R.mipmap.plant_weed_3944340))),
			new Pair<>("Consociazioni sconsigliate", Arrays.asList(
					new Pair<>("Zucchina", R.mipmap.plant_zucchini_3944064),
					new Pair<>("Cipolla", R.mipmap.plant_onion_3944225),
					new Pair<>("Melanzana", R.mipmap.plant_eggplants_3944110),
					new Pair<>("Cavolo nero", R.mipmap.plant_kale_3944155)))
	);
	
	private final static List<Pair<String, List<Pair<String, Integer>>>> cards2 = Arrays.asList(
			new Pair<>("Rotazioni utili", Arrays.asList(
					new Pair<>("Pomodoro", R.mipmap.plant_tomato_3944072),
					new Pair<>("Aglio", R.mipmap.plant_garlic_3944096),
					new Pair<>("Finocchio", R.mipmap.plant_fennel_3944161))),
			new Pair<>("Rotazioni sconsigliate", Arrays.asList(
					new Pair<>("Asparago", R.mipmap.plant_asparagus_3944087),
					new Pair<>("Carciofo", R.mipmap.plant_artichoke_3944084),
					new Pair<>("Barbabietola", R.mipmap.plant_beet_3944102),
					new Pair<>("Sedano", R.mipmap.plant_celery_3944146),
					new Pair<>("Cavolfiore", R.mipmap.plant_cauliflower_3944060)))
	);
	
	private final List<String> dropdownItems = Arrays.asList("Peperoncino generico", "Peperoncino Fatalii", "Peperoncino Habanero", "Peperoncino Naga Morich", "Peperoncino Diavolicchio"
//				, "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4"
	);
	
	private final List<Pair<String, Integer>> mButtons = Arrays.asList(
			new Pair<>("Carriola", R.drawable.ic_round_wheelbarrow_border_24),
			new Pair<>("Preferiti", R.drawable.ic_round_favorite_border_24),
			new Pair<>("Modifica", R.drawable.ic_round_edit_24));
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ortaggio_fragment, container, false);
		setupToolbar(view);
//		setupDropdown(view);
		setupContent(view, container);
		return view;
	}
	
	private void setupToolbar(@NonNull View view) {
		MaterialToolbar toolbar = view.findViewById(R.id.ortaggio_fl_toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		
		if (activity != null) {
			activity.setSupportActionBar(toolbar);
		}
		
		final ActionBar actionBar = activity.getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		AutoCompleteTextView dropdown = view.findViewById(R.id.ortaggio_bl_dropdown);
		dropdown.setText(dropdownItems.get(0));
		ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.ortaggio_fl_dropdown_item, dropdownItems);
		dropdown.setAdapter(adapter);

//		toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
//				getContext(),
//				view.findViewById(R.id.home_backdrop_frontlayer),
//				new AccelerateDecelerateInterpolator(),
//				R.drawable.ic_round_menu_24,
//				R.drawable.ic_round_close_24,
//				drawer.getMeasuredHeight()));
		
		RecyclerView navbuttonsRecyclerView = view.findViewById(R.id.ortaggio_bl_buttons);
		CircleButtonsAdapter circleButtonsAdapter = new CircleButtonsAdapter(mButtons);
		FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
		flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
		navbuttonsRecyclerView.setLayoutManager(flexboxLayoutManager);
		navbuttonsRecyclerView.setAdapter(circleButtonsAdapter);
	}
	
	private void setupContent(@NonNull View view, ViewGroup container) {
		
		// Expandable Text View
		ExpandableTextView expTv1 = view.findViewById(R.id.expand_text_view);
		expTv1.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris convallis nisi sed mi cursus maximus eget ac enim. Aenean a sodales lectus. Aenean erat ex, luctus a feugiat et, bibendum nec nibh. Nullam eget risus leo. Nulla facilisi. Proin iaculis consectetur elit et tempor. Pellentesque lacus metus, pulvinar et viverra eget, lobortis nec dui. In euismod eu magna facilisis suscipit. Sed ut imperdiet diam. Integer ut neque turpis. Aenean tortor mauris, convallis sed pellentesque at, interdum vel odio. Vivamus nec mollis nisl. Nulla eleifend congue venenatis. Etiam eget ex pulvinar, iaculis diam a, hendrerit velit. Morbi aliquet id mauris dapibus fermentum. Pellentesque pretium finibus blandit. Vivamus ut orci in lacus elementum suscipit eget sed purus. Integer augue lectus, consectetur sit amet velit quis, auctor dignissim lacus. Integer suscipit pulvinar justo a condimentum. Nam tincidunt pretium risus in ullamcorper. Aliquam efficitur, enim vitae consectetur sagittis, nunc leo porttitor dolor, et tristique metus ipsum sed erat. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut tellus nisl, dictum in justo nec, volutpat laoreet libero.");
		
		// Calendar
		MaterialButtonToggleGroup calendar = view.findViewById(R.id.ortaggio_bl_calendar);
		for (int i = 0; i < 12; i++) {
			MaterialButton month = (MaterialButton) getLayoutInflater().inflate(R.layout.ortaggio_bl_calendar_item, calendar, false);
			month.setText(months[i]);
			if (months_bs[i]) {  // FIXME it doesn't show changes with API 22 (?)
				month.setChecked(true);
				month.setBackgroundColor(ColorUtils.attrColor(com.google.android.material.R.attr.colorPrimary, getContext(), 40));
				month.jumpDrawablesToCurrentState();
			}
			calendar.addView(month);
		}
		
		// Specs
		RecyclerView specsRecyclerView = view.findViewById(R.id.ortaggio_bl_specs_recycler);
		specsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtaggioSpecsAdapter ortaggioSpecsAdapter = new OrtaggioSpecsAdapter(specs);
		specsRecyclerView.setAdapter(ortaggioSpecsAdapter);
		
		// Cards
		RecyclerView cardsRecyclerView1 = view.findViewById(R.id.ortaggio_bl_card_list_recycler1);
		cardsRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtaggioCardListAdapter ortaggioCardListAdapter1 = new OrtaggioCardListAdapter(cards1);
		cardsRecyclerView1.setAdapter(ortaggioCardListAdapter1);
		
		// Cards
		RecyclerView cardsRecyclerView2 = view.findViewById(R.id.ortaggio_bl_card_list_recycler2);
		cardsRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtaggioCardListAdapter ortaggioCardListAdapter2 = new OrtaggioCardListAdapter(cards2);
		cardsRecyclerView2.setAdapter(ortaggioCardListAdapter2);
		
	}

//	// Show appbar right menu
//	@Override
//	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
//		getActivity().getMenuInflater().inflate(R.menu.ortaggio_fl_toolbar_menu, menu);
//	}
	
}
