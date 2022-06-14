package com.plantalot.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.plantalot.R;
import com.plantalot.adapters.CircleButtonsAdapter;
import com.plantalot.adapters.OrtaggioCardListAdapter;
import com.plantalot.adapters.OrtaggioSpecsAdapter;
import com.plantalot.classes.OrtaggioSpecs;
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
import java.util.List;


public class OrtaggioFragment extends Fragment {
	
	private static int iter = 0;
	
	private long dropdownDismissTime = 0;
	
	private final static String[] months = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII"};
	//	private final static String[] months = {"G", "F", "M", "A", "M", "G", "L", "A", "S", "O", "N", "D"};
	private final static Boolean[] months_bs = {false, false, true, true, true, true, true, true, true, false, false, false};
	List<String> codes = Arrays.asList("H547", "H020", "H451", "H450", "H452", "H050", "H495", "H496", "H497", "H490", "H494", "H492", "H493", "H508", "H509", "H014", "H013", "H010", "H012", "H011", "H562", "H042", "H045", "H030", "H031", "H046", "H041", "H040", "H043", "H044", "H105", "H070", "H073", "H075", "H079", "H06C", "H06E", "H10Z", "H10F", "H052", "H049", "H345", "H098", "H061", "H060", "H063", "H062", "H108", "H066", "H103", "H069", "H059", "H058", "H064", "H101", "H102", "H068", "H087", "H065", "H104", "H067", "H109", "H086", "H081", "H089", "H06A", "H084", "H085", "H080", "H082", "H083", "H548", "H088", "H072", "H100", "H071", "H107", "H555", "H110", "H111", "H113", "H10Y", "H112", "H346", "H341", "H362", "H369", "H371", "H357", "H549", "H342", "H340", "H366", "H348", "H355", "H364", "H349", "H351", "H361", "H518", "H517", "H516", "H512", "H502", "H513", "H503", "H524", "H519", "H525", "H521", "H523", "H514", "H522", "H526", "H505", "H500", "H501", "H515", "H506", "H034", "H559", "H551", "H560", "H552", "H561", "H558", "H554", "H121", "H122", "H120", "H124", "H130", "H131", "H144", "H142", "H141", "H143", "H140", "H204", "H153", "H152", "H184", "H158", "H182", "H181", "H200", "H151", "H159", "H154", "H150", "H208", "H203", "H160", "H170", "H183", "H180", "H202", "H193", "H161", "H192", "H191", "H207", "H155", "H211", "H206", "H210", "H201", "H157", "H556", "H254", "H241", "H253", "H252", "H251", "H260", "H259", "H270", "H114", "H280", "HPF43", "HPF49", "HPF44", "HPF39", "H286", "HPF35", "H29A", "H291", "HPF32", "HPF27", "HPF45", "HPF48", "HPF33", "HPF9", "H722", "HPF23", "H682", "H679", "H678", "HPF41", "H695", "H696", "HPF36", "HPF25", "HPF24", "HPF38", "H683", "HPF47", "HPF46", "HPF22", "HPF42", "HPF5", "HPF50", "HPF37", "H724", "H723", "HPF40", "HPF31", "H290", "HPF4", "H299", "H298", "H294", "H283", "H281", "H285", "H292", "H284", "H297", "H301", "H289", "H293", "H282", "H300", "H553", "H329", "H336", "H312", "H335", "H307", "H303", "H337", "H322", "H328", "H504", "H527", "H115", "H117", "H118", "H116", "H347", "H365", "H354", "H363", "H350", "H367", "H360", "H370", "H368", "H373", "H358", "H352", "H343", "H353", "H359", "H074", "H077", "H557", "H480", "H507", "H391", "H394", "H390", "H395", "H392", "H510", "H511", "H094", "H096", "H092", "H090", "H106", "H095", "H091", "H424", "H414", "H415", "H423", "H419", "H411", "H425", "H427", "H410", "H426", "H417", "H418", "H412", "H422", "H420", "H421");
	
	private static List<OrtaggioSpecs> specs;
	private DatabaseReference mDatabase;
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
	
	private final List<String> dropdownItems = Arrays.asList("Generico", "Fatalii", "Habanero", "Naga Morich", "Diavolicchio"
//				, "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4"
	);
	
	private final List<Pair<String, Integer>> mButtons = Arrays.asList(
			new Pair<>("Carriola", R.drawable.ic_round_wheelbarrow_border_24),
			new Pair<>("Preferiti", R.drawable.ic_round_favorite_border_24),
			new Pair<>("Modifica", R.drawable.ic_round_edit_24));
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Collections.shuffle(codes);
		mDatabase = FirebaseDatabase.getInstance().getReference();
//		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ortaggio_fragment, container, false);
		setupToolbar(view);
		setupTexField(view);
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
		
		AutoCompleteTextView dropdown = view.findViewById(R.id.ortaggio_bl_autocomplete);
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
	
	private void setupTexField(@NonNull View view) {
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
	
	private void setupContent(@NonNull View view, ViewGroup container) {
		// Specs
		mDatabase.child("ortomio").child("varieta").child(codes.get(iter)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DataSnapshot> task) {
				if (!task.isSuccessful()) {
					Log.e("firebase", "Error getting data", task.getException());
				} else {

					HashMap plant = (HashMap) task.getResult().getValue();
					System.out.println("firebase OK ====================================================== " + plant);
					
					ImageView img = view.findViewById(R.id.ortaggio_fl_appbar_image);
//					img.setImageResource(R.mipmap.)
					
					TextView title = view.findViewById(R.id.ortaggio_fl_appbar_title);
					TextView subtitle = view.findViewById(R.id.ortaggio_fl_appbar_subtitle);
					TextView sub1 = view.findViewById(R.id.ortaggio_fl_toolbar_subtitle1);
					TextView sub2 = view.findViewById(R.id.ortaggio_fl_toolbar_subtitle2);
					title.setText(plant.get(DbStrings.VARIETA_CLASSIFICAZIONE_ORTAGGIO) + "");
					subtitle.setText(plant.get(DbStrings.VARIETA_CLASSIFICAZIONE_VARIETA) + "");
					sub1.setText(plant.get(DbStrings.VARIETA_TASSONOMIA_GENERE) + " " + plant.get(DbStrings.VARIETA_TASSONOMIA_SPECIE));
					sub2.setText(plant.get(DbStrings.VARIETA_TASSONOMIA_FAMIGLIA) + "");
					
					
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
					iter++;
					RecyclerView specsRecyclerView = view.findViewById(R.id.ortaggio_bl_specs_recycler);
					specsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
					OrtaggioSpecsAdapter ortaggioSpecsAdapter = new OrtaggioSpecsAdapter(specs);
					specsRecyclerView.setAdapter(ortaggioSpecsAdapter);
				}
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
