package com.example.hw567;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBProvider extends ContentProvider {

    //Uri is a pathway that is created to the data. Content provider handles the location where its stored
    //
    static final String PROVIDER_NAME = "com.example.hw567DBtest.DBProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/entries";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final String ID = "_id";
    static final String NAME = "name";
    static final String CATEGORY = "category";
    static final String DATE = "date";
    static final String AMOUNT = "amount";
    static final String NOTES = "notes";
    private static HashMap<String, String> ENTRY_PROJECTION_MAP;
    static final int NAMES = 1;
    static final int ENTRY_ID = 2;

    //
    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "entries", NAMES);
        uriMatcher.addURI(PROVIDER_NAME, "entries/#", ENTRY_ID);

    }

    //creates database
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "expenses";
    static final String ENTRY_TABLE_NAME = "students";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + ENTRY_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + " name TEXT NOT NULL, " +
                    "category TEXT NOT NULL, date TEXT NOT NULL, amount TEXT NOT NULL, notes TEXT);";
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE); }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ENTRY_TABLE_NAME);
            onCreate(db);
        }
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
/**
 * Create a write able database which will trigger its
 * creation if it doesn't already exist. */

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        //tool to help build a query
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(ENTRY_TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case NAMES:
                qb.setProjectionMap(ENTRY_PROJECTION_MAP); break;
            case ENTRY_ID:
                qb.appendWhere( ID + "=" + uri.getPathSegments().get(1));   //appends statement to search by id
                break;
            default:
        }
        if (s1 == null || s1 == "")
        { /**
         * By default sort on student names
         */
            s1 = NAME;
        }
        Cursor c = qb.query(db,strings, s, strings1,null, null, s1);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            /**
             * Get all student records
             */
            case NAMES:
                return "vnd.android.cursor.dir/vnd.example.entries";
            /**
             * Get a particular student
             */
            case ENTRY_ID:
                return "vnd.android.cursor.item/vnd.example.entries";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID = db.insert(ENTRY_TABLE_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null); return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case NAMES:
                count = db.delete(ENTRY_TABLE_NAME, s,
                        strings);
                break;
            case ENTRY_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( ENTRY_TABLE_NAME, ID + " = " + id +
                        (!TextUtils.isEmpty(s) ? " AND (" + s + ')' : ""), strings); break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String s, @Nullable String[] strings) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case NAMES:
                count = db.update(ENTRY_TABLE_NAME, values, s,
                        strings);
                break;
            case ENTRY_ID:
                count = db.update(ENTRY_TABLE_NAME, values,
                        ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(s) ? " AND (" +s + ')' : ""), strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri ); }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}

