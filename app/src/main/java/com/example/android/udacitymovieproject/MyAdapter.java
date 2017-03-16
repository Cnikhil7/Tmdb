package com.example.android.udacitymovieproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private Context mContext;
    private ArrayList<MovieObject> mList;
    private ItemClickListener mClickListener;


    //interface to handle on item clicks on views in main activity grid items
    interface ItemClickListener {
        void onItemClick(String id, String posterUrl);
    }

    //constructor that accepts context and an arrayList of movieObjects parsed by
    //JsonUtils

    MyAdapter(Context context, ArrayList<MovieObject> vList, ItemClickListener vClickListener) {
        mContext = context;
        mList = vList;
        mClickListener = vClickListener;
    }

    @Override
    public MyAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_grid_item, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyHolder holder, int position) {

        final MovieObject movieObject = mList.get(position);
        TextView textView = holder.mTitle;
        textView.setText(movieObject.getTitle());
        final ImageView image = holder.mPoster;
        Picasso.with(mContext).load(movieObject.getPosterUrl())
                .error(R.drawable.ic_image_black_24dp).into(image);

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    //a holder class that keeps references of the views that are inflated by recycler view in
    //onBindViewHolder

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitle;
        private ImageView mPoster;

        MyHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.list_card_item);
            mPoster = (ImageView) itemView.findViewById(R.id.image_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            MovieObject movieObject = mList.get(position);
            mClickListener.onItemClick(movieObject.getId(), movieObject.getPosterUrl());
        }
    }
}
