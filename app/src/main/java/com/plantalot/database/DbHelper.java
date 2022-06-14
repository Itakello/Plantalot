//package com.plantalot.database;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DbHelper extends SQLiteOpenHelper {
//
//	public DbHelper(Context context) {
//		super(context, DbStrings.DB_NAME, null, 1);
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		createTables(db);
//
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//	}
//
//	public void dropTables(SQLiteDatabase db) {
//		db.execSQL("DROP TABLE IF EXISTS " + DbStrings.PIANTE_TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + DbStrings.VARIETA_TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + DbStrings.ICONS_TABLE_NAME);
//	}
//
//	public void createTables(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE IF NOT EXISTS " + DbStrings.PIANTE_TABLE_NAME + " (" +
//				DbStrings.PIANTE_FIELD_TASSONOMIA_PIANTA + " TEXT PRIMARY KEY," +
//				DbStrings.PIANTE_FIELD_TASSONOMIA_FAMIGLIA + " TEXT," +
//				DbStrings.PIANTE_FIELD_CONSOCIAZIONI_POS + " TEXT," +
//				DbStrings.PIANTE_FIELD_ROTAZIONI_POS + " TEXT," +
//				DbStrings.PIANTE_FIELD_ROTAZIONI_NEG + " TEXT," +
//				DbStrings.PIANTE_FIELD_ROTAZIONI_ANNI + " INT," +
//				DbStrings.PIANTE_FIELD_MEZZOMBRA_TOLLERATA + " TEXT," +
//				DbStrings.PIANTE_FIELD_DISTANZE_PIANTE + " INT," +
//				DbStrings.PIANTE_FIELD_DISTANZE_FILE + " INT," +
//				DbStrings.PIANTE_FIELD_CONCIMAZIONE_ORGANICA + " TEXT," +
//				DbStrings.PIANTE_FIELD_CONCIMAZIONE_TRAPIANTO + " BOOLEAN," +
//				DbStrings.PIANTE_FIELD_CONCIMAZIONE_MENSILE + " BOOLEAN," +
//				DbStrings.PIANTE_FIELD_IRRIGAZIONE_RIDUZIONE + " BOOLEAN," +
//				DbStrings.PIANTE_FIELD_IRRIGAZIONE_SOSPENSIONE + " BOOLEAN," +
//				DbStrings.PIANTE_FIELD_IRRIGAZIONE_ATTECCHIMENTO + " TEXT)");
//
//		db.execSQL("CREATE TABLE IF NOT EXISTS " + DbStrings.VARIETA_TABLE_NAME + " (" +
//				DbStrings.VARIETA_FIELD_INFO_CODICE + " TEXT PRIMARY KEY," +
//				DbStrings.VARIETA_FIELD_INFO_IDVAR + " INT," +
//				DbStrings.VARIETA_FIELD_INFO_PACK + " INT," +
//				DbStrings.VARIETA_FIELD_TASSONOMIA_FAMIGLIA + " TEXT," +
//				DbStrings.VARIETA_FIELD_TASSONOMIA_GENERE + " TEXT," +
//				DbStrings.VARIETA_FIELD_TASSONOMIA_SPECIE + " TEXT," +
//				DbStrings.VARIETA_FIELD_TASSONOMIA_VARIETA + " TEXT," +
//				DbStrings.VARIETA_FIELD_CLASSIFICAZIONE_PIANTA + " TEXT," +
//				DbStrings.VARIETA_FIELD_CLASSIFICAZIONE_ORTAGGIO + " TEXT," +
//				DbStrings.VARIETA_FIELD_CLASSIFICAZIONE_VARIETA + " TEXT," +
//				DbStrings.VARIETA_FIELD_TRAPIANTO_MESI + " TEXT," +
//				DbStrings.VARIETA_FIELD_DISTANZE_PIANTE + " INT," +
//				DbStrings.VARIETA_FIELD_DISTANZE_PIANTE_RANGE + " INT," +
//				DbStrings.VARIETA_FIELD_DISTANZE_FILE + " INT," +
//				DbStrings.VARIETA_FIELD_RACCOLTA_MIN + " INT," +
//				DbStrings.VARIETA_FIELD_RACCOLTA_MAX + " INT," +
//				DbStrings.VARIETA_FIELD_RACCOLTA_UDM + " TEXT," +
//				DbStrings.VARIETA_FIELD_PRODUZIONE_PESO + " INT," +
//				DbStrings.VARIETA_FIELD_PRODUZIONE_RANGE + " INT," +
//				DbStrings.VARIETA_FIELD_PRODUZIONE_UDM + " TEXT," +
//				DbStrings.VARIETA_FIELD_BOOLEANS_PERENNE + " BOOLEAN," +
//				DbStrings.VARIETA_FIELD_BOOLEANS_MEZZOMBRA + " BOOLEAN," +
//				DbStrings.VARIETA_FIELD_BOOLEANS_F1 + " BOOLEAN," +
//				DbStrings.VARIETA_FIELD_INFO_DESCRIZIONE + " TEXT)");
//
//		db.execSQL("CREATE TABLE IF NOT EXISTS " + DbStrings.ICONS_TABLE_NAME + " (" +
//				DbStrings.ICONS_FIELD_ORTAGGIO + " TEXT PRIMARY KEY," +
//				DbStrings.ICONS_FIELD_ARTIST + " TEXT," +
//				DbStrings.ICONS_FIELD_ICON + " TEXT)");
//	}
//}