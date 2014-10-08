package com.hbb.ege.obezite;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import android.content.Context;

public class DataBaseHelper extends SQLiteAssetHelper {
	public static final String DATABASE_NAME = "dbHiperTansiyon.db";
	public static final int DATABASE_VERSION = 1;

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
}
