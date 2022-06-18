package com.plantalot.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
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

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
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
import com.plantalot.components.CircleButton;
import com.plantalot.components.OrtaggioSpecs;
import com.plantalot.database.Db;
import com.plantalot.database.DbStrings;
import com.plantalot.utils.ColorUtils;
import com.plantalot.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class OrtaggioFragment extends Fragment {
	
	private long dropdownDismissTime = 0;
	
	private final static String[] months = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII"};
	//	private final static String[] months = {"G","F","M","A","M","G", "L", "A", "S", "O", "N", "D"};
//	List<String> ortaggi_list = Arrays.asList("Peperoncino");
	private LinkedList<String> dropdownItems;
	
	private final List<CircleButton> mButtons = Arrays.asList(
			new CircleButton("Carriola", R.drawable.ic_round_wheelbarrow_border_24),
			new CircleButton("Preferiti", R.drawable.ic_round_favorite_border_24),
			new CircleButton("Modifica", R.drawable.ic_round_edit_24));
	
	private View view;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.ortaggio_fragment, container, false);
		setupTexField();
		setupContent();
		return view;
	}
	
	private void setupToolbar(@NonNull DataSnapshot snapshot, HashMap<String, Object> pianta) {
		MaterialToolbar toolbar = view.findViewById(R.id.ortaggio_fl_toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		assert activity != null;
		activity.setSupportActionBar(toolbar);
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
				Object result = snapshot.child(dropdownItems.get(position)).getValue();
				if (result != null && result.getClass().getName().equals("java.util.HashMap")) {
					setupStats((HashMap<String, Object>) result, pianta);  // fixme
				}
			}
		});
		
		CircleButton.setRecycler(mButtons, view.findViewById(R.id.ortaggio_bl_buttons), getContext());
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
	
	private void setupStats(HashMap varieta, HashMap pianta) {
		System.out.println("====================================================== " + varieta);
		
		TextView title = view.findViewById(R.id.ortaggio_fl_appbar_title);
		TextView subtitle = view.findViewById(R.id.ortaggio_fl_appbar_subtitle);
		TextView sub1 = view.findViewById(R.id.ortaggio_fl_toolbar_subtitle1);
		TextView sub2 = view.findViewById(R.id.ortaggio_fl_toolbar_subtitle2);
		title.setText((String) varieta.get(DbStrings.VARIETA_CLASSIFICAZIONE_ORTAGGIO));
		subtitle.setText((String) varieta.get(DbStrings.VARIETA_CLASSIFICAZIONE_VARIETA));
		sub1.setText((String) varieta.get(DbStrings.VARIETA_TASSONOMIA_GENERE) + " " + (String) varieta.get(DbStrings.VARIETA_TASSONOMIA_SPECIE));
		sub2.setText((String) varieta.get(DbStrings.VARIETA_TASSONOMIA_FAMIGLIA));
		
		// Expandable Text View
		MaterialCardView descriptionCard = view.findViewById(R.id.ortaggio_bl_specs_description);
		String description = (String) varieta.get(DbStrings.VARIETA_INFO_DESCRIZIONE);
		if (description != null && !description.isEmpty()) {
			descriptionCard.setVisibility(View.VISIBLE);
			ExpandableTextView descriptionExpand = view.findViewById(R.id.expand_text_view);
			descriptionExpand.setText(description);
		} else {
			descriptionCard.setVisibility(View.GONE);
		}
		
		// Calendar
		MaterialButtonToggleGroup calendar = view.findViewById(R.id.ortaggio_bl_calendar);
		calendar.removeAllViews();
		for (int i = 0; i < 12; i++) {
			MaterialButton month = (MaterialButton) getLayoutInflater().inflate(R.layout.ortaggio_bl_calendar_item, calendar, false);
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			
			month.setText(months[i]);
			month.setRippleColor(null);
			Object isMonthOk = ((ArrayList) varieta.get(DbStrings.VARIETA_TRAPIANTI_MESI)).get(i);
			if (isMonthOk.getClass().getName().equals("java.lang.Long")) {
				isMonthOk = Double.valueOf((Long) isMonthOk);
			}
			month.setChecked(false);
			month.setBackgroundColor(ColorUtils.attrColor(com.google.android.material.R.attr.colorPrimary, getContext(), (int) ((Double) isMonthOk * 50)));
			month.jumpDrawablesToCurrentState();
			
			if (i == cal.get(Calendar.MONTH)) {
				month.setTypeface(null, Typeface.BOLD);
				month.setStrokeWidth(Utils.dp2px(3, getContext()));
			}
			
			calendar.addView(month);
		}
		
		List<OrtaggioSpecs> specs = Arrays.asList(
				new OrtaggioSpecs(
						"Distanze",
						varieta.get(DbStrings.VARIETA_DISTANZE_PIANTE) + " × " + varieta.get(DbStrings.VARIETA_DISTANZE_FILE) + " cm",
						R.mipmap.specs_distanze_1462005,
						false),
				new OrtaggioSpecs(
						"Mezz'ombra",
						"" + varieta.get(DbStrings.VARIETA_ALTRO_TOLLERA_MEZZOMBRA),
						R.mipmap.specs_mezzombra_4496245,
						false),
				new OrtaggioSpecs(
						"Raccolta",
						varieta.get(DbStrings.VARIETA_RACCOLTA_MIN) + (varieta.get(DbStrings.VARIETA_RACCOLTA_MAX) != varieta.get(DbStrings.VARIETA_RACCOLTA_MIN) ? "-" + varieta.get(DbStrings.VARIETA_RACCOLTA_MAX) : "") + " giorni",
						R.mipmap.specs_raccolta_3078971,
						false),
				new OrtaggioSpecs(
						"Produzione",
						varieta.get(DbStrings.VARIETA_PRODUZIONE_PESO) + " " + varieta.get(DbStrings.VARIETA_PRODUZIONE_UDM),
						R.mipmap.specs_produzione_741366,
						false),
				new OrtaggioSpecs(
						"Rotazione",
						pianta.get(DbStrings.PIANTE_ROTAZIONI_ANNI) + " anni",
						R.mipmap.specs_rotazione_4496256,
						false),
				new OrtaggioSpecs(
						"Vaschetta",
						varieta.get(DbStrings.VARIETA_ALTRO_PACK) + " piante",
						R.mipmap.specs_vaschetta_1655603,
						false),
				new OrtaggioSpecs(
						"Concimazione",
						pianta.get(DbStrings.PIANTE_CONCIMAZIONE_ORGANICA) + " (organica)"
								+ (pianta.get(DbStrings.PIANTE_CONCIMAZIONE_TRAPIANTO) == "1" ? "\n" + "In buca al trapianto" : "")
								+ (pianta.get(DbStrings.PIANTE_CONCIMAZIONE_MENSILE) == "1" ? "\n" + "Mensile dopo il trapianto" : ""),
						R.mipmap.specs_concimazione_1670075,
						true),
				new OrtaggioSpecs(
						"Irrigazione",
						
						pianta.get(DbStrings.PIANTE_IRRIGAZIONE_ATTECCHIMENTO)
								+ (pianta.get(DbStrings.PIANTE_IRRIGAZIONE_RIDUZIONE) == "1" ? "\n" + "Ridurre prima della raccolta" : "")
								+ (pianta.get(DbStrings.PIANTE_IRRIGAZIONE_SOSPENSIONE) == "1" ? "\n" + "Sospendere prima della raccolta" : ""),
						R.mipmap.specs_irrigazione_3319229,
						true));
		RecyclerView specsRecyclerView = view.findViewById(R.id.ortaggio_bl_specs_recycler);
		specsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtaggioSpecsAdapter ortaggioSpecsAdapter = new OrtaggioSpecsAdapter(specs);
		specsRecyclerView.setAdapter(ortaggioSpecsAdapter);
	}
	
	private void setupContent() {
		
		// Specs
		assert getArguments() != null;
		String ortaggio = getArguments().getString("ortaggio");
		DatabaseReference dbRefVarieta = FirebaseDatabase.getInstance().getReference("ortomio/varieta/" + ortaggio);
		System.out.println("firebase OK ====================================================== " + ortaggio);
		System.out.println("firebase OK ====================================================== " + dbRefVarieta);
		
		dbRefVarieta.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshotVarieta) {
				if(snapshotVarieta.getValue() == null) {
					return;  // FIXME !!!!
				}
				String defaultVar = ((HashMap) snapshotVarieta.getValue()).size() == 1
						? snapshotVarieta.getChildren().iterator().next().getKey()
						: "Generico";
				
				dropdownItems = new LinkedList<String>(((HashMap) snapshotVarieta.getValue()).keySet());
				Collections.sort(dropdownItems);
				if (dropdownItems.contains("Generico")) {
					dropdownItems.remove("Generico");
					dropdownItems.add(0, "Generico");
				}
				setupToolbar(snapshotVarieta, null);
				
				HashMap varieta = (HashMap) snapshotVarieta.child(defaultVar).getValue();
				System.out.println("====================================================== " + varieta);
				
				String imageFile = Db.icons.get((String) varieta.get(DbStrings.VARIETA_CLASSIFICAZIONE_ORTAGGIO));
				ImageView img = view.findViewById(R.id.ortaggio_fl_appbar_image);
				boolean hasImage = false;
				if (imageFile != null) {
					Context context = getContext();
					Resources res = context.getResources();
					int imageId = res.getIdentifier(imageFile.split("\\.")[0], "mipmap", context.getPackageName());
					if (imageId > 0) {
						img.setImageResource(imageId);
						hasImage = true;
					}
				}
				if (!hasImage) {
					img.setImageResource(R.mipmap.plant_basil_3944343);
				}
				
				TextView title = view.findViewById(R.id.ortaggio_fl_appbar_title);
				TextView subtitle = view.findViewById(R.id.ortaggio_fl_appbar_subtitle);
				TextView sub1 = view.findViewById(R.id.ortaggio_fl_toolbar_subtitle1);
				TextView sub2 = view.findViewById(R.id.ortaggio_fl_toolbar_subtitle2);
				title.setText((String) varieta.get(DbStrings.VARIETA_CLASSIFICAZIONE_ORTAGGIO));
				subtitle.setText((String) varieta.get(DbStrings.VARIETA_CLASSIFICAZIONE_VARIETA));
				sub1.setText((String) varieta.get(DbStrings.VARIETA_TASSONOMIA_GENERE) + " " + (String) varieta.get(DbStrings.VARIETA_TASSONOMIA_SPECIE));
				sub2.setText((String) varieta.get(DbStrings.VARIETA_TASSONOMIA_FAMIGLIA));
				
				// Pianta
				DatabaseReference dbRefPianta = FirebaseDatabase.getInstance().getReference("ortomio/piante/" + varieta.get(DbStrings.VARIETA_CLASSIFICAZIONE_PIANTA));
				System.out.println("firebase OK ====================================================== " + dbRefPianta);
				
				dbRefPianta.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot snapshotPianta) {
						HashMap pianta = (HashMap) snapshotPianta.getValue();
						
						setupStats(varieta, pianta);
						setupToolbar(snapshotVarieta, pianta);
						
						System.out.println("===================== " + pianta);
						
						// Cards
						List cards1 = Collections.singletonList(
								new Pair<>("Consociazioni utili", (ArrayList) pianta.get(DbStrings.PIANTE_CONSOCIAZIONI_POS))
						);
						
						List cards2 = Arrays.asList(
								new Pair<>("Rotazioni utili", (ArrayList) pianta.get(DbStrings.PIANTE_ROTAZIONI_POS)),
								new Pair<>("Rotazioni sconsigliate", (ArrayList) pianta.get(DbStrings.PIANTE_ROTAZIONI_NEG))
						);
						
						RecyclerView cardsRecyclerView1 = view.findViewById(R.id.ortaggio_bl_card_list_recycler1);
						cardsRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
						OrtaggioCardListAdapter ortaggioCardListAdapter1 = new OrtaggioCardListAdapter(cards1);
						cardsRecyclerView1.setAdapter(ortaggioCardListAdapter1);
						
						RecyclerView cardsRecyclerView2 = view.findViewById(R.id.ortaggio_bl_card_list_recycler2);
						cardsRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
						OrtaggioCardListAdapter ortaggioCardListAdapter2 = new OrtaggioCardListAdapter(cards2);
						cardsRecyclerView2.setAdapter(ortaggioCardListAdapter2);
						
					}
					
					@Override
					public void onCancelled(@NonNull DatabaseError error) {
						Log.e("firebase", "onCancelled " + error.getMessage());
					}
				});
				
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.e("firebase", "onCancelled " + error.getMessage());
			}
		});
		
	}
	

//	// Show appbar right menu
//	@Override
//	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
//		getActivity().getMenuInflater().inflate(R.menu.ortaggio_fl_toolbar_menu, menu);
//	}
	
}
