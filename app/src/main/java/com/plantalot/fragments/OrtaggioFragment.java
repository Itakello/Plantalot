package com.plantalot.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.plantalot.MyApplication;
import com.plantalot.R;
import com.plantalot.adapters.OrtaggioCardListAdapter;
import com.plantalot.adapters.OrtaggioSpecsAdapter;
import com.plantalot.classes.Carriola;
import com.plantalot.classes.Giardino;
import com.plantalot.classes.Pianta;
import com.plantalot.classes.Varieta;
import com.plantalot.components.OrtaggioSpecs;
import com.plantalot.database.DbPlants;
import com.plantalot.database.DbUsers;
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
import java.util.Map;


public class OrtaggioFragment extends Fragment {
	
	private final static String[] months = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII"};
	private LinkedList<String> dropdownItems;
	private Giardino giardino;
	private Carriola carriola;

//	private final List<CircleButton> mButtons = Arrays.asList(  // FIXME !!!!!
//			new CircleButton("Carriola", R.drawable.ic_round_wheelbarrow_border_24, R.drawable.ic_round_wheelbarrow_24, CircleButton.CARRIOLA),
//			new CircleButton("Preferiti", R.drawable.ic_round_favorite_border_24, R.drawable.ic_round_favorite_24, CircleButton.PREFERITI)
//	);
//			new CircleButton("Modifica", R.drawable.ic_round_edit_24));
	
	private View view;
	private final Map<String, Varieta> ortaggioDocuments = new HashMap<>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		giardino = ((MyApplication) this.getActivity().getApplication()).user.getGiardinoCorrente();
		carriola = giardino.getCarriola();
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.ortaggio_fragment, container, false);
		Handler handler = new Handler();
		handler.post(this::setupContentOrtaggio);
		return view;
	}
	
	private void setupHeader(Pianta pianta) {
		MaterialToolbar toolbar = view.findViewById(R.id.ortaggio_fl_toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		assert activity != null;
		activity.setSupportActionBar(toolbar);
		final ActionBar actionBar = activity.getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		toolbar.setNavigationOnClickListener(view -> {
			int prev_frag_id = getArguments().getInt("prev_fragment");
			NavController navController = Navigation.findNavController(view);
			navController.popBackStack(prev_frag_id, false);
		});
		
		AutoCompleteTextView dropdown = view.findViewById(R.id.ortaggio_bl_autocomplete);
		dropdown.setText(dropdownItems.get(0));
		ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.ortaggio_fl_dropdown_item, dropdownItems);
		dropdown.setAdapter(adapter);
		
		dropdown.setOnItemClickListener((parent, view, position, id) ->
				setupContentVarieta(pianta, ortaggioDocuments.get(dropdownItems.get(position)))
		);

//		CircleButton.setupRecycler(mButtons, view.findViewById(R.id.ortaggio_bl_buttons), getContext());  // FIXME
	}
	
	private void setupButton(Varieta varieta) {
		String ortaggioName = varieta.getClassificazione_ortaggio();
		String varietaName = varieta.getClassificazione_varieta();
		View buttonCarriola = view.findViewById(R.id.ortaggio_fl_button_carriola);
		boolean isIn = carriola.contains(ortaggioName, varietaName);
		int icon = isIn ? R.drawable.ic_round_wheelbarrow_24 : R.drawable.ic_round_wheelbarrow_border_24;
		String label = isIn ? "Togli dalla carriola" : "Metti in carriola";
		((MaterialButton) buttonCarriola.findViewById(R.id.component_circle_button_icon)).setIconResource(icon);
		((TextView) buttonCarriola.findViewById(R.id.component_circle_button_label)).setText(label);
		buttonCarriola.setOnClickListener(v -> {
			if (isIn) {
				carriola.remove(ortaggioName, varietaName);
			} else {
				carriola.put(ortaggioName, varietaName, varieta.getAltro_pack());
			}
			setupButton(varieta);
			giardino.setCarriola(carriola);
			DbUsers.updateGiardino(giardino);
		});
		buttonCarriola.setVisibility(View.VISIBLE);
	}
	
	private void setupContentVarieta(Pianta pianta, Varieta varieta) {
		
		// Header
		TextView title = view.findViewById(R.id.ortaggio_fl_appbar_title);
		TextView subtitle = view.findViewById(R.id.ortaggio_fl_appbar_subtitle);
		TextView sub1 = view.findViewById(R.id.ortaggio_fl_toolbar_subtitle1);
		TextView sub2 = view.findViewById(R.id.ortaggio_fl_toolbar_subtitle2);
		title.setText(varieta.getClassificazione_ortaggio());
		subtitle.setText(varieta.getClassificazione_varieta());
		sub1.setText(varieta.getTassonomia_genere() + " " + varieta.getTassonomia_specie());
		sub2.setText(varieta.getTassonomia_famiglia());
		setupButton(varieta);
		
		// Description
		MaterialCardView descriptionCard = view.findViewById(R.id.ortaggio_bl_specs_description);
		String description = varieta.getInfo_descrizione();
		if (description != null && !description.isEmpty()) {
			descriptionCard.setVisibility(View.VISIBLE);
			((TextView) view.findViewById(R.id.ortaggio_bl_specs_description_text)).setText(description);
		} else {
			descriptionCard.setVisibility(View.GONE);
		}
		
		// Calendar
		MaterialButtonToggleGroup calendar = view.findViewById(R.id.ortaggio_bl_calendar);
		calendar.removeAllViews();
		for (int i = 0; i < 12; i++) {
			MaterialButton month = (MaterialButton) getLayoutInflater().inflate(R.layout.ortaggio_bl_calendar_item, calendar, false);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			
			month.setText(months[i]);
			month.setRippleColor(null);
			Object isMonthOk = varieta.getTrapianti_mesi().get(i);
			if (isMonthOk.getClass().getName().equals("java.lang.Long")) {
				isMonthOk = Double.valueOf((Long) isMonthOk);
			}
			month.setChecked(false);
			month.setBackgroundColor(ColorUtils.attrColor(com.google.android.material.R.attr.colorPrimary, getContext(), (int) ((Double) isMonthOk * 50)));
			month.jumpDrawablesToCurrentState();
			
			if (cal.get(Calendar.MONTH) == i) {
				month.setTypeface(null, Typeface.BOLD);
				month.setStrokeWidth(Utils.dp2px(3, getContext()));
			}
			calendar.addView(month);
		}
		
		// Specs
		List<OrtaggioSpecs> specs = Arrays.asList(
				new OrtaggioSpecs(
						"Distanze",
						varieta.getDistanze_piante() + " × " + varieta.getDistanze_file() + " cm",
						R.mipmap.specs_distanze_1462005,
						false),
				new OrtaggioSpecs(
						"Mezz'ombra",
						varieta.getAltro_tollera_mezzombra(),
						R.mipmap.specs_mezzombra_4496245,
						false),
				new OrtaggioSpecs(
						"Raccolta",
						varieta.getRaccolta_min() + (varieta.getRaccolta_max() != varieta.getRaccolta_min() ? "-" + varieta.getRaccolta_max() : "") + " giorni",
						R.mipmap.specs_raccolta_3078971,
						false),
				new OrtaggioSpecs(
						"Produzione",
						varieta.getProduzione_peso() + " " + varieta.getProduzione_udm(),
						R.mipmap.specs_produzione_741366,
						false),
				new OrtaggioSpecs(
						"Rotazione",
						pianta.getRotazioni_anni() + " anni",
						R.mipmap.specs_rotazione_4496256,
						false),
				new OrtaggioSpecs(
						"Vaschetta",
						varieta.getAltro_pack() + " piante",
						R.mipmap.specs_vaschetta_1655603,
						false),
				new OrtaggioSpecs(
						"Concimazione",
						pianta.getConcimazione_organica() + " (organica)"
								+ (pianta.getConcimazione_trapianto() == 1 ? "\nIn buca al trapianto" : "")
								+ (pianta.getConcimazione_mensile() == 1 ? "\nMensile dopo il trapianto" : ""),
						R.mipmap.specs_concimazione_1670075,
						true),
				new OrtaggioSpecs(
						"Irrigazione",
						pianta.getIrrigazione_attecchimento()
								+ (pianta.getIrrigazione_riduzione() == 1 ? "\nRidurre prima della raccolta" : "")
								+ (pianta.getIrrigazione_sospensione() == 1 ? "\nSospendere prima della raccolta" : ""),
						R.mipmap.specs_irrigazione_3319229,
						true));
		
		RecyclerView specsRecyclerView = view.findViewById(R.id.ortaggio_bl_specs_recycler);
		specsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtaggioSpecsAdapter ortaggioSpecsAdapter = new OrtaggioSpecsAdapter(specs);
		specsRecyclerView.setAdapter(ortaggioSpecsAdapter);
		specsRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT
		));
	}
	
	private void setupContentOrtaggio() {
		final String GENERICO = "Generico";
		assert getArguments() != null;
		String ortaggio = getArguments().getString("ortaggio");
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		
		db.collection("varieta")
				.whereEqualTo(DbPlants.VARIETA_CLASSIFICAZIONE_ORTAGGIO, ortaggio)
				.get().addOnSuccessListener(queryDocumentSnapshots -> {
					
					for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
						ortaggioDocuments.put(doc.get(DbPlants.VARIETA_CLASSIFICAZIONE_VARIETA).toString(), doc.toObject(Varieta.class));
					}
					
					String defaultVarieta = ortaggioDocuments.size() == 1
							? new ArrayList<>(ortaggioDocuments.keySet()).get(0)
							: "Generico";
					Varieta varieta = ortaggioDocuments.get(defaultVarieta);
					
					dropdownItems = new LinkedList<>(ortaggioDocuments.keySet());
					Collections.sort(dropdownItems);
					if (dropdownItems.contains(GENERICO)) {
						dropdownItems.remove(GENERICO);
						dropdownItems.add(0, GENERICO);
					}
					
					db.collection("icons")
							.document(varieta.getClassificazione_ortaggio())
							.get().addOnSuccessListener(document -> {
								if (!document.exists()) return;
								ImageView img = view.findViewById(R.id.ortaggio_fl_appbar_image);
								img.setImageResource(DbPlants.getImageId(ortaggio));
							});
					
					db.collection("piante")
							.document(varieta.getClassificazione_pianta())
							.get().addOnSuccessListener(document -> {
								if (!document.exists()) return;
								Pianta pianta = document.toObject(Pianta.class);
								setupContentVarieta(pianta, varieta);
								setupHeader(pianta);
								setupCompanions(pianta);
							});
				});
	}
	
	private void setupCompanions(Pianta pianta) {
		
		List<Pair<String, List<String>>> cards1 = new ArrayList<>(Collections.singletonList(
				new Pair<>("Consociazioni utili", pianta.getConsociazioni_pos())
		));
		
		List<Pair<String, List<String>>> cards2 = new ArrayList<>(Arrays.asList(
				new Pair<>("Rotazioni utili", pianta.getRotazioni_pos()),
				new Pair<>("Rotazioni sconsigliate", pianta.getRotazioni_neg())
		));
		
		int prev_fragment = getArguments().getInt("prev_fragment");
		RecyclerView cardsRecyclerView1 = view.findViewById(R.id.ortaggio_bl_card_list_recycler1);
		cardsRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtaggioCardListAdapter ortaggioCardListAdapter1 = new OrtaggioCardListAdapter(cards1, prev_fragment);
		cardsRecyclerView1.setAdapter(ortaggioCardListAdapter1);
		
		RecyclerView cardsRecyclerView2 = view.findViewById(R.id.ortaggio_bl_card_list_recycler2);
		cardsRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
		OrtaggioCardListAdapter ortaggioCardListAdapter2 = new OrtaggioCardListAdapter(cards2, prev_fragment);
		cardsRecyclerView2.setAdapter(ortaggioCardListAdapter2);
	}
	
}
