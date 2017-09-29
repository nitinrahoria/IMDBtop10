package com.rahoria.nitin.imdbtop10;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.LoaderManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.rahoria.nitin.imdbtop10.adapter.MovieAdapter;
import com.rahoria.nitin.imdbtop10.provider.MovieDataProvider;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nitin on 9/29/2017.
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener {
    private static final String IMDB_PREFERENCE = "imdb_settings_preference";
    private static final String FISRTLAUNCH = "firstlaunch";
    private static final String TAG = "MainActivity";
    SharedPreferences mSharedPreferences;
    private RecyclerView recyclerView;
    private String mCurFilter;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        mSharedPreferences = getSharedPreferences(IMDB_PREFERENCE, Context.MODE_PRIVATE);
        if(isAppFirstLaunch()) {
            Log.d(TAG,"inserting to db");
            storeDataToDataBase();
            mSharedPreferences.edit().putInt(FISRTLAUNCH,0).commit();
        }

        movieAdapter = new MovieAdapter(this, null);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean isAppFirstLaunch(){
        if(mSharedPreferences.getInt(FISRTLAUNCH,1) == 1)
            return true;
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.home_screen_search_view_hint));

        searchView.setOnQueryTextListener(this);

        return true;
    }

    private void storeDataToDataBase() {
        LinkedList <Movie> movieList = getMovieList();
        insertMovieInBulk(movieList);
    }

    public int insertMovieInBulk(List<Movie> movieList){
        int result = 0;
        int id = 0;
        int len = movieList.size();
        for (int i = 0; i<len;i++){
            ContentValues values = new ContentValues();
            values.put(MovieDataProvider.TOP_MOVIE_LIST_COLUMN_TITLE, movieList.get(i).getTitle());
            values.put(MovieDataProvider.TOP_MOVIE_LIST_COLUMN_IMAGE, movieList.get(i).getImage());
            values.put(MovieDataProvider.TOP_MOVIE_LIST_COLUMN_YEAR, movieList.get(i).getYear());
            values.put(MovieDataProvider.TOP_MOVIE_LIST_COLUMN_RATING, movieList.get(i).getRating());
            values.put(MovieDataProvider.TOP_MOVIE_LIST_COLUMN_URL, movieList.get(i).getUrl());
            Uri uri = getContentResolver().insert(
                    MovieDataProvider.CONTENT_URI, values);
            id = Integer.parseInt(uri.getPathSegments().get(1));
            if(id >= 0)
                result++;
            else
                Log.e(TAG,"Error while inserting data : "+ movieList.get(i).toString());
        }
        return result;
    }

    private LinkedList<Movie> getMovieList() {
        LinkedList <Movie> movieList = new LinkedList<>();
        movieList.add(new Movie("The Shawshank Redemption", "movie_1", "1994", "9.2", "http://m.imdb.com/title/tt0111161/?pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=2398042122&pf_rd_r=0TPBKXRWSD4NSP2KYPED&pf_rd_s=top-1&pf_rd_t=15506&pf_rd_i=top&ref_=m_chttp_tt_1"));
        movieList.add(new Movie("The Godfather", "movie_2", "1972", "9.2", "http://m.imdb.com/title/tt0068646/?pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=2398042122&pf_rd_r=0TPBKXRWSD4NSP2KYPED&pf_rd_s=top-1&pf_rd_t=15506&pf_rd_i=top&ref_=m_chttp_tt_2"));
        movieList.add(new Movie("The Godfather: Part II", "movie_3", "1974", "9.0", "http://m.imdb.com/title/tt0071562/?pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=2398042122&pf_rd_r=0TPBKXRWSD4NSP2KYPED&pf_rd_s=top-1&pf_rd_t=15506&pf_rd_i=top&ref_=m_chttp_tt_3"));
        movieList.add(new Movie("The Dark Knight", "movie_4", "2008", "9.0", "http://m.imdb.com/title/tt0468569/?pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=2398042122&pf_rd_r=0TPBKXRWSD4NSP2KYPED&pf_rd_s=top-1&pf_rd_t=15506&pf_rd_i=top&ref_=m_chttp_tt_4"));
        movieList.add(new Movie("12 Angry Men", "movie_5", "1957", "8.9", "http://m.imdb.com/title/tt0050083/?pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=2398042122&pf_rd_r=0TPBKXRWSD4NSP2KYPED&pf_rd_s=top-1&pf_rd_t=15506&pf_rd_i=top&ref_=m_chttp_tt_5"));
        movieList.add(new Movie("Schindler's List", "movie_6", "1993", "8.9", "http://m.imdb.com/title/tt0108052/?pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=2398042122&pf_rd_r=0TPBKXRWSD4NSP2KYPED&pf_rd_s=top-1&pf_rd_t=15506&pf_rd_i=top&ref_=m_chttp_tt_6"));
        movieList.add(new Movie("Pulp Fiction", "movie_7", "1994", "8.9", "http://m.imdb.com/title/tt0110912/?pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=2398042122&pf_rd_r=0TPBKXRWSD4NSP2KYPED&pf_rd_s=top-1&pf_rd_t=15506&pf_rd_i=top&ref_=m_chttp_tt_7"));
        movieList.add(new Movie("The Lord of the Rings: The Return of the King", "movie_8", "2003", "8.9", "http://m.imdb.com/title/tt0167260/?pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=2398042122&pf_rd_r=0TPBKXRWSD4NSP2KYPED&pf_rd_s=top-1&pf_rd_t=15506&pf_rd_i=top&ref_=m_chttp_tt_8"));
        movieList.add(new Movie("The Good, the Bad and the Ugly", "movie_9", "1996", "8.8", "http://m.imdb.com/title/tt0060196/?pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=2398042122&pf_rd_r=0TPBKXRWSD4NSP2KYPED&pf_rd_s=top-1&pf_rd_t=15506&pf_rd_i=top&ref_=m_chttp_tt_9"));
        movieList.add(new Movie("Fight Club", "movie_10", "1999", "8.7", "http://m.imdb.com/title/tt0137523/?pf_rd_m=A2FGELUUNOQJNL&pf_rd_p=2398042122&pf_rd_r=0TPBKXRWSD4NSP2KYPED&pf_rd_s=top-1&pf_rd_t=15506&pf_rd_i=top&ref_=m_chttp_tt_10"));
        return movieList;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri;
        String selection = null;
        String[] selectionArgs = null;
        if (mCurFilter != null) {
            Log.e(TAG, "mCurFilter is not null");
            selection = MovieDataProvider.TOP_MOVIE_LIST_COLUMN_TITLE + " LIKE ?";
            selectionArgs = new String[] {"%"+mCurFilter+"%"};
        }

        return new CursorLoader(this, MovieDataProvider.CONTENT_URI, null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mCurFilter = !TextUtils.isEmpty(query) ? query : null;
        getLoaderManager().restartLoader(0, null, this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        getLoaderManager().restartLoader(0, null, this);
        return true;
    }
}
