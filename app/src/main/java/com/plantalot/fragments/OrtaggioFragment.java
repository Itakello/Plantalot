package com.plantalot.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.plantalot.R;
import com.plantalot.adapters.CircleButtonsAdapter;
import com.plantalot.adapters.OrtaggioCardListAdapter;
import com.plantalot.adapters.OrtaggioSpecsAdapter;
import com.plantalot.classes.OrtaggioSpecs;
import com.plantalot.database.Db;
import com.plantalot.database.DbStrings;
import com.plantalot.utils.ColorUtils;
import com.plantalot.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class OrtaggioFragment extends Fragment {
	
	private static int iter = 0;
	
	private long dropdownDismissTime = 0;
	
	private final static String[] months = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII"};
	//	private final static String[] months = {"G", "F", "M", "A", "M", "G", "L", "A", "S", "O", "N", "D"};
	private final static Boolean[] months_bs = {false, false, true, true, true, true, true, true, true, false, false, false};
	List<String> ortaggi_list = Arrays.asList("Aglio", "Anguria", "Arachide", "Barbabietola", "Basilico", "Bietola", "Broccolo", "Carosello", "Carota", "Catalogna", "Cavolfiore", "Cavolo cappuccio", "Cavolo cinese", "Cavolo di Bruxelles", "Cavolo nero", "Cavolo riccio", "Cece", "Cetriolo", "Cicoria", "Cima di rapa", "Cipolla", "Cipollotto", "Erba cipollina", "Fagiolino", "Fagiolo", "Fava", "Finocchio", "Indivia", "Lattuga", "Mais", "Melanzana", "Melone", "Okra gombo", "Peperoncino", "Peperone", "Pisello", "Pomodoro", "Porro", "Prezzemolo", "Puntarelle", "Radicchio", "Rapa", "Ravanello", "Rucola", "Scalogno", "Sedano", "Sedano rapa", "Spinacio", "Valeriana", "Verza", "Zucca", "Zucchino");
	
	private static List<OrtaggioSpecs> specs;

//	= Arrays.asList(
//			new OrtaggioSpecs("Distanze", "40 × 100 cm", R.mipmap.specs_distanze_1462005, false),
//			new OrtaggioSpecs("Mezz'ombra", "In estate", R.mipmap.specs_mezzombra_4496245, false),
//			new OrtaggioSpecs("Raccolta", "80-100 giorni", R.mipmap.specs_raccolta_3078971, false),
//			new OrtaggioSpecs("Produzione", "200 g/cad", R.mipmap.specs_produzione_741366, false),
//			new OrtaggioSpecs("Rotazione", "4 anni", R.mipmap.specs_rotazione_4496256, false),
//			new OrtaggioSpecs("Vaschetta", "6 piante", R.mipmap.specs_vaschetta_1655603, false),
//			new OrtaggioSpecs("Concimazione", "Abbondante (organica)\nIn buca al trapianto\nMensile dopo il trapianto", R.mipmap.specs_concimazione_1670075, true),
//			new OrtaggioSpecs("Irrigazione", "Abbondare a inizio allegagione e ingrossamento dei frutti\nSospendere prima della raccolta", R.mipmap.specs_irrigazione_3319229, true));
	
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
	
	private LinkedList<String> dropdownItems;
	
	private final List<Pair<String, Integer>> mButtons = Arrays.asList(
			new Pair<>("Carriola", R.drawable.ic_round_wheelbarrow_border_24),
			new Pair<>("Preferiti", R.drawable.ic_round_favorite_border_24),
			new Pair<>("Modifica", R.drawable.ic_round_edit_24));
	
	View view;
			
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (iter == 0) {
			Collections.shuffle(ortaggi_list, new Random());
		} else {
			iter++;
		}
//		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.ortaggio_fragment, container, false);
		setupTexField();
		setupContent();
		return view;
	}
	
	private void setupToolbar(@NonNull DataSnapshot snapshot) {
		MaterialToolbar toolbar = view.findViewById(R.id.ortaggio_fl_toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		
		if (activity != null) {
			activity.setSupportActionBar(toolbar);
		}
		
		final ActionBar actionBar = activity.getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		AutoCompleteTextView dropdown = view.findViewById(R.id.ortaggio_bl_autocomplete);
		dropdown.setText(dropdownItems.get(0));
		ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.ortaggio_fl_dropdown_item, dropdownItems);
		dropdown.setAdapter(adapter);
		
		dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				setupStats(snapshot, dropdownItems.get(position));
			}
		});


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
	
	private void setupTexField() {
		AutoCompleteTextView autocomplete = view.findViewById(R.id.ortaggio_bl_autocomplete);
		TextInputLayout textfield = view.findViewById(R.id.ortaggio_bl_textfield);
		autocomplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
//					textfield.setEndIconMode(TextInputLayout.END_ICON_DROPDOWN_MENU);
					Utils.hideSoftKeyboard(v, getActivity());
					// todo reset last input
				} else {
//					textfield.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
				}
			}
		});
	}
	
	private void setupStats(DataSnapshot snapshot, String varieta) {
		HashMap plant = (HashMap) snapshot.child(varieta).getValue();
		System.out.println("====================================================== " + plant);
		
		TextView title = view.findViewById(R.id.ortaggio_fl_appbar_title);
		TextView subtitle = view.findViewById(R.id.ortaggio_fl_appbar_subtitle);
		TextView sub1 = view.findViewById(R.id.ortaggio_fl_toolbar_subtitle1);
		TextView sub2 = view.findViewById(R.id.ortaggio_fl_toolbar_subtitle2);
		title.setText((String) plant.get(DbStrings.VARIETA_CLASSIFICAZIONE_ORTAGGIO));
		subtitle.setText((String) plant.get(DbStrings.VARIETA_CLASSIFICAZIONE_VARIETA));
		sub1.setText((String) plant.get(DbStrings.VARIETA_TASSONOMIA_GENERE) + " " + (String) plant.get(DbStrings.VARIETA_TASSONOMIA_SPECIE));
		sub2.setText((String) plant.get(DbStrings.VARIETA_TASSONOMIA_FAMIGLIA));
		
		// Expandable Text View
		ExpandableTextView expTv1 = view.findViewById(R.id.expand_text_view);
		expTv1.setText("" + plant.get(DbStrings.VARIETA_INFO_DESCRIZIONE));
		
		// Calendar
		MaterialButtonToggleGroup calendar = view.findViewById(R.id.ortaggio_bl_calendar);
		for (int i = 0; i < 12; i++) {
			MaterialButton month = (MaterialButton) getLayoutInflater().inflate(R.layout.ortaggio_bl_calendar_item, calendar, false);
			java.util.Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			
			month.setText(months[i]);
			if (months_bs[i]) {  // FIXME it doesn't show changes with API 22 (?)
				month.setChecked(true);
				month.setBackgroundColor(ColorUtils.attrColor(com.google.android.material.R.attr.colorPrimary, getContext(), 40));
				month.jumpDrawablesToCurrentState();
			}
			
			if (i == cal.get(Calendar.MONTH)) {
				month.setTypeface(null, Typeface.BOLD);
				month.setStrokeWidth(Utils.dp2px(3, getContext()));
			}
			
			calendar.addView(month);
		}
		
		specs = Arrays.asList(
				new OrtaggioSpecs(
						"Distanze",
						plant.get(DbStrings.VARIETA_DISTANZE_PIANTE) + " × " + plant.get(DbStrings.VARIETA_DISTANZE_FILE) + " cm",
						R.mipmap.specs_distanze_1462005,
						false),
				new OrtaggioSpecs(
						"Mezz'ombra",
						"" + plant.get(DbStrings.VARIETA_ALTRO_TOLLERA_MEZZOMBRA),
						R.mipmap.specs_mezzombra_4496245,
						false),
				new OrtaggioSpecs(
						"Raccolta",
						plant.get(DbStrings.VARIETA_RACCOLTA_MIN) + (plant.get(DbStrings.VARIETA_RACCOLTA_MAX) != plant.get(DbStrings.VARIETA_RACCOLTA_MIN) ? "-" + plant.get(DbStrings.VARIETA_RACCOLTA_MAX) : "") + " giorni",
						R.mipmap.specs_raccolta_3078971,
						false),
				new OrtaggioSpecs(
						"Produzione",
						plant.get(DbStrings.VARIETA_PRODUZIONE_PESO) + " " + plant.get(DbStrings.VARIETA_PRODUZIONE_UDM),
						R.mipmap.specs_produzione_741366,
						false),
				new OrtaggioSpecs(
						"Rotazione",
						"??? anni",
						R.mipmap.specs_rotazione_4496256,
						false),
				new OrtaggioSpecs(
						"Vaschetta",
						plant.get(DbStrings.VARIETA_ALTRO_PACK) + " piante",
						R.mipmap.specs_vaschetta_1655603,
						false),
				new OrtaggioSpecs(
						"Concimazione",
						"??? Abbondante (organica)\nIn buca al trapianto\nMensile dopo il trapianto",
						R.mipmap.specs_concimazione_1670075,
						true),
				new OrtaggioSpecs(
						"Irrigazione",
						"??? Abbondare a inizio allegagione e ingrossamento dei frutti\nSospendere prima della raccolta",
						R.mipmap.specs_irrigazione_3319229,
						true));
		RecyclerView specsRecyclerView = view.findViewById(R.id.ortaggio_bl_specs_recycler);
		specsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtaggioSpecsAdapter ortaggioSpecsAdapter = new OrtaggioSpecsAdapter(specs);
		specsRecyclerView.setAdapter(ortaggioSpecsAdapter);
	}
	
	private void setupContent() {
		
		// Specs
		DatabaseReference dbRefPlant = Db.mDatabase.child("ortomio").child("varieta").child(ortaggi_list.get(iter));
		System.out.println("firebase OK ====================================================== " + ortaggi_list.get(iter));
		
		dbRefPlant.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot taskSnapshot) {
				String defaultVar = ((HashMap) taskSnapshot.getValue()).size() == 1
						? taskSnapshot.getChildren().iterator().next().getKey()
						: "Generico";
				
				dropdownItems = new LinkedList<String>(((HashMap) taskSnapshot.getValue()).keySet());
				Collections.sort(dropdownItems);
				if (dropdownItems.contains("Generico")) {
					dropdownItems.remove("Generico");
					dropdownItems.add(0, "Generico");
				}
				setupToolbar(taskSnapshot);
				
				HashMap plant = (HashMap) taskSnapshot.child(defaultVar).getValue();
				System.out.println("====================================================== " + plant);
				
				String imageFile = Db.icons.get((String) plant.get(DbStrings.VARIETA_CLASSIFICAZIONE_PIANTA));
				if (imageFile != null) {
					Context context = getContext();
					Resources res = context.getResources();
					int imageId = res.getIdentifier(imageFile.split("\\.")[0], "mipmap", context.getPackageName());
					if (imageId > 0) {
						ImageView img = view.findViewById(R.id.ortaggio_fl_appbar_image);
						img.setImageResource(imageId);
					}
				}
				
				setupStats(taskSnapshot, defaultVar);
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.e("firebase", "onCancelled " + error.getMessage());
			}
		});
		
		
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
