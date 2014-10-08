package com.hbb.ege.obezite;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Manager {
	private static volatile Manager instance = null;
	private ArrayList<Entry> list;
	private ArrayList<Entry> searchResult;
	private DataBaseHelper helper;
    private ArrayList<Food> foodList;

	private Manager(Context context) {
        /*Create a helper for maintain database*/
		helper = new DataBaseHelper(context);
        //Initialize list for usage
		list = new ArrayList<Entry>();
        //get Database
		SQLiteDatabase db = helper.getWritableDatabase();

        //get args ready for query
		String selection = " title LIKE ? ";
		String[] selectionArgs = { "%" };
		String[] columns = { "id", "title", "content" };

        //Query Entry Table
		Cursor c = db.query("EntryList", columns, selection, selectionArgs, null,
				null, null);
		if (c.moveToFirst()) {
			while (!c.isAfterLast()) {
				list.add(new Entry(c.getInt(0),c.getString(1),c.getString(2)));
				c.moveToNext();
			}
		}
        //Query Food List table
        c.close();
        c = db.query("FoodList", columns, selection, selectionArgs, null,
                null, null);
        foodList = new ArrayList<Food>();

        if(c.moveToFirst()){
            while (!c.isAfterLast()){
                foodList.add(new Food(c.getInt(0),c.getString(1),c.getString(2)));
                c.moveToNext();
            }
        }
		c.close();
		db.close();
	}

	public static Manager getManager(Context context) {
		if (instance == null)
			instance = new Manager(context);
		return instance;
	}

	public ArrayList<Entry> getIndexList() {
		return list;
	}

	public ArrayList<Entry> search(String selection, String[] selectionArgs,
			String[] columns) {
		searchResult = new ArrayList<Entry>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.query("EntryList", columns, selection, selectionArgs, null,
				null, null);
		if (c.moveToFirst()) {
			while (!c.isAfterLast()) {
				searchResult.add(new Entry(c.getInt(0),c.getString(1),c.getString(2)));
				c.moveToNext();
			}
		}
		c.close();
		db.close();
		return searchResult;
	}

	public ArrayList<Entry> getSearchResult() {
		if (searchResult == null) {
			throw new NullPointerException("Search result is empty.");
		}
		return searchResult;
	}

    public ArrayList<Food> getFoodList(){
        return foodList;
    }

	@Override
	protected void finalize() throws Throwable {
		helper.close();
		super.finalize();
	}
}
