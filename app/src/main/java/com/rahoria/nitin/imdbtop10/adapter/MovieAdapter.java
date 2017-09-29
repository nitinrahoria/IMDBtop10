package com.rahoria.nitin.imdbtop10.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rahoria.nitin.imdbtop10.R;
import com.rahoria.nitin.imdbtop10.WebViewActivity;
import com.rahoria.nitin.imdbtop10.provider.MovieDataProvider;

/**
 * Created by nitin on 9/29/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder>{

    private final Context mContext;
    private Cursor mCursor;
    
    public MovieAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.setId(mCursor.getInt(MovieDataProvider.TOP_MOVIE_LIST_ID_INDEX));
        holder.setUrl(mCursor.getString(MovieDataProvider.TOP_MOVIE_LIST_URL_INDEX));
        String uri = "@drawable/"+mCursor.getString(MovieDataProvider.TOP_MOVIE_LIST_IMAGE_INDEX);

        int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
        Drawable res = mContext.getResources().getDrawable(imageResource);
        holder.img.setImageDrawable(res);
        holder.title.setText(mCursor.getString(MovieDataProvider.TOP_MOVIE_LIST_TITLE_INDEX)
                +"("+mCursor.getString(MovieDataProvider.TOP_MOVIE_LIST_YEAR_INDEX)+")");
        holder.rating.setText(mCursor.getString(MovieDataProvider.TOP_MOVIE_LIST_RATING_INDEX));
    }

    @Override
    public int getItemCount() {
            if(mCursor != null)
                return mCursor.getCount();
            else
                return 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, rating;
        public ImageView img;
        private int id;
        private String url;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            img = (ImageView) view.findViewById(R.id.movieRowImageView);
            title = (TextView) view.findViewById(R.id.movie_title);
            rating = (TextView) view.findViewById(R.id.rating);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(mContext, WebViewActivity.class);
            i.putExtra("url", url);
            i.putExtra("title", title.getText());
            mContext.startActivity(i);
        }

        public void setId(int id) {
            this.id = id;
        }
        public void setUrl(String url){
            this.url = url;
        }
    }
    public void swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return;
        }

        if (newCursor != null) {
            mCursor = newCursor;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
            mCursor = null;
        }
    }
}
