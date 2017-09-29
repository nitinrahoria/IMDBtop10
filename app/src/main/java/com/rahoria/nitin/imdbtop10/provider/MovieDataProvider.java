package com.rahoria.nitin.imdbtop10.provider;

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
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rahoria.nitin.imdbtop10.Movie;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by nitin on 9/29/2017.
 */

public class MovieDataProvider extends ContentProvider {
    public static final String PROVIDER_NAME = "com.rahoria.nitin.imdbtop10";
    public static final String URL = "content://" + PROVIDER_NAME + "/top_movie_list";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String TOP_MOVIE_LIST_COLUMN_ID = "id";
    public static final String TOP_MOVIE_LIST_COLUMN_IMAGE = "image";
    public static final String TOP_MOVIE_LIST_COLUMN_TITLE = "title";
    public static final String TOP_MOVIE_LIST_COLUMN_YEAR ="year";
    public static final String TOP_MOVIE_LIST_COLUMN_RATING = "rating";
    public static final String TOP_MOVIE_LIST_COLUMN_URL = "url";

    public static final int TOP_MOVIE_LIST_ID_INDEX = 0;
    public static final int TOP_MOVIE_LIST_TITLE_INDEX = 1;
    public static final int TOP_MOVIE_LIST_IMAGE_INDEX = 2;
    public static final int TOP_MOVIE_LIST_YEAR_INDEX = 3;
    public static final int TOP_MOVIE_LIST_RATING_INDEX = 4;
    public static final int TOP_MOVIE_LIST_URL_INDEX = 5;

    static final int MOVIE_LIST = 1;
    static final int MOVIE_ID = 2;

    private static HashMap<String, String> MOVIES_PROJECTION_MAP;
    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    public static final String DATABASE_NAME = "IMDBTop10.db";
    public static final String MOVIE_LIST_TABLE_NAME = "movie_list";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            "create table "+ MOVIE_LIST_TABLE_NAME +
                    "(" + TOP_MOVIE_LIST_COLUMN_ID + " integer primary key autoincrement, " +
                    TOP_MOVIE_LIST_COLUMN_TITLE + " text, " + TOP_MOVIE_LIST_COLUMN_IMAGE + " text, " +
                    TOP_MOVIE_LIST_COLUMN_YEAR + " text, " + TOP_MOVIE_LIST_COLUMN_RATING + " text, " +
                    TOP_MOVIE_LIST_COLUMN_URL + " text)";

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "movie_list", MOVIE_LIST);
        uriMatcher.addURI(PROVIDER_NAME, "movie_list/#", MOVIE_ID);
    }


    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  MOVIE_LIST_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(MOVIE_LIST_TABLE_NAME);

        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on rating
             */
            sortOrder = TOP_MOVIE_LIST_COLUMN_RATING+" DESC";
        }
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);

        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all scheduled msgs records
             */
            case MOVIE_LIST:
                return "vnd.android.cursor.dir/vnd.example.movie_list";

            /**
             * Get a particular scheduled msg
             */
            case MOVIE_ID:
                return "vnd.android.cursor.item/vnd.example.movie_list";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a movie list
         */
        long rowID = db.insert(	MOVIE_LIST_TABLE_NAME, "", values);

        /**
         * If record is added successfully
         */

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        return count;
    }
}
