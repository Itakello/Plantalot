//package com.plantalot.database;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteException;
//
//public class DbManager {
//	private DbHelper dbhelper;
//	SQLiteDatabase db;
//
//	public DbManager(Context ctx) {
//		dbhelper = new DbHelper(ctx);
//		db = dbhelper.getWritableDatabase();
//	}
//
//	public void reset() {
//		dbhelper.dropTables(db);
//		dbhelper.createTables(db);
//	}
//
//
//	public void save(String sub, String txt, String date) {
//		SQLiteDatabase db = dbhelper.getWritableDatabase();
//		ContentValues cv = new ContentValues();
//		cv.put(DbStrings.ICONS_FIELD_ORTAGGIO, sub);
//		cv.put(DbStrings.ICONS_FIELD_ARTIST, txt);
//		cv.put(DbStrings.ICONS_FIELD_ICON, date);
//		try {
//			db.insert(DbStrings.ICONS_TABLE_NAME, null, cv);
//		} catch (SQLiteException sqle) {
//		}
//	}
//
//	public void update() {
//		SQLiteDatabase db = dbhelper.getWritableDatabase();
//		db.execSQL("DROP TABLE " + DbStrings.ICONS_TABLE_NAME);
//
//		db.execSQL("CREATE TABLE " + DbStrings.ICONS_TABLE_NAME + " (" +
//				"_id TEXT PRIMARY KEY," +
////				DatabaseStrings.ICONS_FIELD_ORTAGGIO + " TEXT PRIMARY KEY," +
//				DbStrings.ICONS_FIELD_ARTIST + " TEXT," +
//				DbStrings.ICONS_FIELD_ICON + " TEXT)");
//	}
//
//	//
////	public boolean delete(long id) {
////		SQLiteDatabase db = dbhelper.getWritableDatabase();
////		try {
////			if (db.delete(DatabaseStrings.TBL_NAME, DatabaseStrings.FIELD_ID + "=?", new String[]{Long.toString(id)}) > 0)
////				return true;
////			return false;
////		} catch (SQLiteException sqle) {
////			return false;
////		}
////	}
////
//	public Cursor query() {
//		Cursor crs = null;
//		try {
//			SQLiteDatabase db = dbhelper.getReadableDatabase();
//			crs = db.query(DbStrings.ICONS_TABLE_NAME, null, null, null, null, null, null, null);
//		} catch (SQLiteException sqle) {
//			return null;
//		}
//		return crs;
//	}
//}
