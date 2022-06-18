package com.plantalot.database;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.plantalot.R;
import com.plantalot.adapters.OrtaggioCardListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Db {
	
	public static final List<String> famiglie = new ArrayList<>();
	public static final HashMap<String, String> icons = new HashMap<>();
	public static final HashMap<String, HashMap<String, Object>> ortaggi = new HashMap<>();
	public static final List<String> ortaggiNames = new ArrayList<>();
	
	static {
		
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);
		DatabaseReference db = FirebaseDatabase.getInstance().getReference("ortomio");
		db.keepSynced(true);
		DatabaseReference dbRefIcons = FirebaseDatabase.getInstance().getReference("ortomio/icons");
		dbRefIcons.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot taskSnapshot) {
				HashMap<String, String> snapMap = (HashMap<String, String>) taskSnapshot.getValue();
				for (String ortaggio : snapMap.keySet()){
					icons.put(ortaggio, snapMap.get(ortaggio));
				}
			}
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.e("firebase", "onCancelled " + error.getMessage());
			}
		});
		
		FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
		FirebaseFirestore.getInstance().setFirestoreSettings(settings);
		FirebaseFirestore.getInstance().collection("ortomio").document("ortaggi").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					HashMap<String, Object> ortaggiRaw = (HashMap<String, Object>) task.getResult().getData();
					for (String ortaggio : ortaggiRaw.keySet()) {
						ortaggi.put(ortaggio, (HashMap<String, Object>) ortaggiRaw.get(ortaggio));
					}
					Set<String> famiglieSet = new HashSet<>();
					for (Object ortaggio : ortaggi.values()) {
						famiglieSet.add((String) ((HashMap) ortaggio).get(Db.VARIETA_TASSONOMIA_FAMIGLIA));
					}
					famiglie.addAll(famiglieSet);
					Collections.sort(famiglie);
					ortaggiNames.addAll(ortaggi.keySet());
					Collections.sort(ortaggiNames);
				} else {
					System.out.println("firebase ERROR ======================================================");
				}
			}
		});
	}
	
	public static void init() {
		// Rimane vuoto, alla chiamata inizializza le variabili nel blocco static
	}
	
	public static final String DB_NAME = "PLANTALOT";
	
	public static final String PIANTA = "pianta";
	public static final String PIANTE_FAMIGLIA = "famiglia";
	public static final String PIANTE_CONSOCIAZIONI_POS = "consociazioni_pos";
	public static final String PIANTE_ROTAZIONI_POS = "rotazioni_pos";
	public static final String PIANTE_ROTAZIONI_NEG = "rotazioni_neg";
	public static final String PIANTE_ROTAZIONI_ANNI = "rotazioni_anni";
	public static final String PIANTE_MEZZOMBRA = "mezzombra";
	public static final String PIANTE_DISTANZE_PIANTE = "distanze_piante";
	public static final String PIANTE_DISTANZE_FILE = "distanze_file";
	public static final String PIANTE_CONCIMAZIONE_ORGANICA = "concimazione_organica";
	public static final String PIANTE_CONCIMAZIONE_TRAPIANTO = "concimazione_trapianto";
	public static final String PIANTE_CONCIMAZIONE_MENSILE = "concimazione_mensile";
	public static final String PIANTE_IRRIGAZIONE_RIDUZIONE = "irrigazione_riduzione";
	public static final String PIANTE_IRRIGAZIONE_SOSPENSIONE = "irrigazione_sospensione";
	public static final String PIANTE_IRRIGAZIONE_ATTECCHIMENTO = "irrigazione_attecchimento";
	
	public static final String VARIETA_INFO_CODICE = "info_codice";
	public static final String VARIETA_INFO_IDVAR = "info_idvar";
	public static final String VARIETA_INFO_URL = "info_url";
	public static final String VARIETA_INFO_DESCRIZIONE = "info_descrizione";
	public static final String VARIETA_TASSONOMIA_FAMIGLIA = "tassonomia_famiglia";
	public static final String VARIETA_TASSONOMIA_GENERE = "tassonomia_genere";
	public static final String VARIETA_TASSONOMIA_SPECIE = "tassonomia_specie";
	public static final String VARIETA_TASSONOMIA_VARIETA = "tassonomia_varieta";
	public static final String VARIETA_CLASSIFICAZIONE_PIANTA = "classificazione_pianta";
	public static final String VARIETA_CLASSIFICAZIONE_ORTAGGIO = "classificazione_ortaggio";
	public static final String VARIETA_CLASSIFICAZIONE_VARIETA = "classificazione_varieta";
	public static final String VARIETA_CLASSIFICAZIONE_SOPRANNOME = "classificazione_soprannome";
	public static final String VARIETA_TRAPIANTI_MESI = "trapianti_mesi";
	public static final String VARIETA_DISTANZE_PIANTE = "distanze_piante";
	public static final String VARIETA_DISTANZE_PIANTE_RANGE = "distanze_piante_range";
	public static final String VARIETA_DISTANZE_FILE = "distanze_file";
	public static final String VARIETA_RACCOLTA_MIN = "raccolta_min";
	public static final String VARIETA_RACCOLTA_MAX = "raccolta_max";
	public static final String VARIETA_RACCOLTA_AVG = "raccolta_avg";
	public static final String VARIETA_RACCOLTA_UDM = "raccolta_udm";
	public static final String VARIETA_PRODUZIONE_PESO = "produzione_peso";
	public static final String VARIETA_PRODUZIONE_RANGE = "produzione_range";
	public static final String VARIETA_PRODUZIONE_UDM = "produzione_udm";
	public static final String VARIETA_ALTRO_PACK = "altro_pack";
	public static final String VARIETA_ALTRO_TOLLERA_MEZZOMBRA = "altro_tollera_mezzombra";
	public static final String VARIETA_ALTRO_MEZZOMBRA = "altro_mezzombra";
	public static final String VARIETA_ALTRO_PERENNE = "altro_perenne";
	public static final String VARIETA_ALTRO_F1 = "altro_f1";
}
