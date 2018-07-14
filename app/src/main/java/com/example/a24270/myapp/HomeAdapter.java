package com.example.a24270.myapp;

/**
 * Created by 24270 on 2018/5/26.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;


public class HomeAdapter  extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
    private List<App> mHomeList;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder {
        View homeView;
        TextView homeName;
        ImageView homeImage;
        public ViewHolder(View view){
            super(view);
            homeView = view;
            homeName = (TextView)view.findViewById(R.id.zd_name);
            homeImage = (ImageView)view.findViewById(R.id.zd_image);
        }
    }
    public HomeAdapter(List<App> homeList,Context context){
        mHomeList = homeList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.homeView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                App home = mHomeList.get(position);
                Intent intent = new Intent(context,web.class);
                intent.putExtra("data",home.getUrl());
                context.startActivity(intent);

            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        App home = mHomeList.get(position);
        Glide.with(context).load(home.getImagePath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .into(holder.homeImage);
        holder.homeName.setText(home.getTitle());
    }

    @Override
    public int getItemCount(){
        return mHomeList.size();
    }
}
