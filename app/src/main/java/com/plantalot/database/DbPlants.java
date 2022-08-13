package com.plantalot.database;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.material.color.MaterialColors;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public class DbPlants {
	
	private static final List<String> famiglieNames = new ArrayList<>();
	private static final List<String> ortaggiNames = new ArrayList<>();
	private static final Map<String, Integer> icons = new HashMap<>();
	private static final Map<String, Integer> iconColors = new HashMap<>();
	private static final Map<String, String> varietaNames = new HashMap<>();
	private static int defaultImageId;
	
	public static List<String> getFamiglieNames() {
		return famiglieNames;
	}
	
	public static List<String> getOrtaggiNames() {
		return ortaggiNames;
	}
	
	public static Map<String, Integer> getIcons() {
		return icons;
	}
	
	public static Map<String, Integer> getIconColors() {
		return iconColors;
	}
	
	public static Map<String, String> getVarietaNames() {
		return varietaNames;
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	public static void init(Activity activity) {
		
		Resources res = activity.getResources();
		defaultImageId = res.getIdentifier(
				"plant_mushroom_3944308".split("\\.")[0],
				"mipmap",
				activity.getPackageName());
		
		// FIXME local/offline db !!!!!!!!!!
		FirebaseFirestore db = FirebaseFirestore.getInstance();
//		db.setPersistenceEnabled(true);
//		db.keepSynced(true);
		
		Set<String> famiglieSet = new HashSet<>();
		db.collection("ortaggi").get().addOnSuccessListener(queryDocumentSnapshots -> {
			for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
				famiglieSet.add(document.get(VARIETA_TASSONOMIA_FAMIGLIA).toString());
				ortaggiNames.add(document.get(VARIETA_CLASSIFICAZIONE_ORTAGGIO).toString());
			}
			famiglieNames.addAll(famiglieSet);
			Collections.sort(famiglieNames);
			Collections.sort(ortaggiNames);
			
			db.collection("icons").get().addOnSuccessListener(queryDocumentSnapshotsIcons -> {
				HashMap<String, String> iconsMap = new HashMap<>();
				for (QueryDocumentSnapshot document : queryDocumentSnapshotsIcons) {
					iconsMap.put(document.getId(), document.get(ICON).toString());
				}
				setIcons(activity, iconsMap);
				setColors(activity);  // FIXME persistent !!!!!!
			});
		});
		
		db.collection("varieta").get().addOnSuccessListener(queryDocumentSnapshots -> {
			for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
				varietaNames.put(
						document.get(VARIETA_CLASSIFICAZIONE_VARIETA).toString(),
						document.get(VARIETA_CLASSIFICAZIONE_ORTAGGIO).toString()
				);
			}
		});
	}
	
	
	public static int getImageId(String ortaggio) {
		if (icons.get(ortaggio) == null || icons.get(ortaggio) == 0) return defaultImageId;
		return icons.get(ortaggio);
	}
	
	
	// TODO persistent
	public static int getIconColor(String ortaggio) {
		if (iconColors.get(ortaggio) == null || famiglieNames.contains(ortaggio)) ortaggio = "Rapa";
		if (Objects.equals(ortaggio, "Peperoncino")) ortaggio = "Pomodoro";
		return iconColors.get(ortaggio);
	}
	
	private static void setIcons(Activity mActivity, HashMap<String, String> iconsMap) {
		Resources res = mActivity.getResources();
		for (String ortaggio : iconsMap.keySet()) {
			String imageFile = iconsMap.get(ortaggio);
			if (imageFile != null) {
				int imageId = res.getIdentifier(
						imageFile.split("\\.")[0],
						"mipmap",
						mActivity.getPackageName());
				icons.put(ortaggio, imageId);
			}
		}
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	private static void setColors(Activity mActivity) {  // FIXME persistent !!!!!!
		for (String ortaggio : ortaggiNames) {
			Bitmap image = BitmapFactory.decodeResource(
					mActivity.getResources(),
					getImageId(ortaggio));
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
			int color = Collections.max(
					color2counter.entrySet(),
					Comparator.comparingInt(Map.Entry::getValue)).getKey();
			color = MaterialColors.harmonizeWithPrimary(mActivity, color);
			iconColors.put(ortaggio, color);
		}
	}
	
	// Fields (utility names)
	public static final String PIANTA_PIANTA = "pianta";
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
	
	public static final String ICON = "icon";
	
}
