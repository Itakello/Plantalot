package com.plantalot.database;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.color.MaterialColors;
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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Db {
	
	public static final List<String> famiglie = new ArrayList<>();
	public static final HashMap<String, Integer> icons = new HashMap<>();
	public static final HashMap<String, Integer> iconColors = new HashMap<>();
	public static final HashMap<String, HashMap<String, Object>> ortaggi = new HashMap<>();
	public static final HashMap<String, HashMap<String, Object>> piante = new HashMap<>();
	public static final HashMap<String, HashMap<String, HashMap<String, Object>>> varieta = new HashMap<>();
	public static final HashMap<String, String> varietaNames = new HashMap<>();
	public static final List<String> ortaggiNames = new ArrayList<>();
	private static final String defaultFile = "plant_weed_3944340";
	private static int defaultImageId;
//	private static final String defaultFile = "plant_basil_3944343.png";
	
	public static void init(Activity activity) {
		
		Resources res = activity.getResources();
		defaultImageId = res.getIdentifier("plant_mushroom_3944308".split("\\.")[0], "mipmap", activity.getPackageName());
		
		// FIXME local db !!!!!!!!!!?
		FirebaseDatabase db = FirebaseDatabase.getInstance();
//		db.setPersistenceEnabled(true);
//		db.keepSynced(true);

		DatabaseReference dbRefOrtaggi = db.getReference("ortomio/ortaggi");
		dbRefOrtaggi.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot taskSnapshot) {
				ortaggi.putAll((HashMap) taskSnapshot.getValue());
				Set<String> famiglieSet = new HashSet<>();
				for (Object ortaggio : ortaggi.values()) {
					famiglieSet.add((String) ((HashMap) ortaggio).get(Db.VARIETA_TASSONOMIA_FAMIGLIA));
				}
				famiglie.addAll(famiglieSet);
				Collections.sort(famiglie);
				ortaggiNames.addAll(ortaggi.keySet());
				Collections.sort(ortaggiNames);
				
				
				DatabaseReference dbRefIcons = db.getReference("ortomio/icons");
				dbRefIcons.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot taskSnapshot) {
						HashMap<String, String> iconsMap = (HashMap) taskSnapshot.getValue();
						setIcons(activity, iconsMap);
						setColors(activity);
					}
					
					@Override
					public void onCancelled(@NonNull DatabaseError error) {
						Log.e("firebase", "onCancelled " + error.getMessage());
					}
				});
				
				DatabaseReference dbRefVarieta = db.getReference("ortomio/varieta");
				dbRefVarieta.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot taskSnapshot) {
						varieta.putAll((HashMap) taskSnapshot.getValue());
						for (String ortaggio : varieta.keySet()) {
							for (String var : varieta.get(ortaggio).keySet()) {
								varietaNames.put(var, ortaggio);
							}
						}
					}
					
					@Override
					public void onCancelled(@NonNull DatabaseError error) {
						Log.e("firebase", "onCancelled " + error.getMessage());
					}
				});
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				System.out.println("firebase ERROR ======================================================");
			}
			
		});
		
		DatabaseReference dbRefPiante = db.getReference("ortomio/piante");
		dbRefPiante.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot taskSnapshot) {
				piante.putAll((HashMap) taskSnapshot.getValue());
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.e("firebase", "onCancelled " + error.getMessage());
			}
		});
	}
	
	public static int getImageId(String ortaggio) {
		if (icons.get(ortaggio) == null || icons.get(ortaggio) == 0) return defaultImageId;
		return icons.get(ortaggio);
	}
	
	
	// TODO persistent
	public static int getIconColor(String ortaggio) {
		if (iconColors.get(ortaggio) == null) ortaggio = "Rapa";
		return iconColors.get(ortaggio);
	}
	
	private static void setIcons(Activity mActivity, HashMap<String, String> iconsMap) {
		Resources res = mActivity.getResources();
		for (String ortaggio : iconsMap.keySet()) {
			String imageFile = iconsMap.get(ortaggio);
			if (imageFile != null) {
				int imageId = res.getIdentifier(imageFile.split("\\.")[0], "mipmap", mActivity.getPackageName());
				icons.put(ortaggio, imageId);
			}
		}
	}
	
	private static void setColors(Activity mActivity) {
		for (String ortaggio : ortaggiNames) {
			Bitmap image = BitmapFactory.decodeResource(mActivity.getResources(), getImageId(ortaggio));
			Map<Integer, Integer> color2counter = new HashMap<>();
			int height = image.getHeight();
			int width = image.getWidth();
			for (int y = (int) (0.5 * height); y < (int) (1.0 * height); y++) {
				for (int x = (int) (0.25 * width); x < (int) (0.75 * width); x++) {
					int color = image.getPixel(x, y);
					if (color2counter.get(color) == null) {
						color2counter.put(color, 0);
					}
					int occurrences = color2counter.get(color);
					color2counter.put(color, occurrences + 1);
				}
			}
			color2counter.remove(0);
			color2counter.remove(-13948117);
			int color = Collections.max(color2counter.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
			color = MaterialColors.harmonizeWithPrimary(mActivity, color);
			iconColors.put(ortaggio, color);
		}
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
