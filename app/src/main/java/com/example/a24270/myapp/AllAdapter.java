package com.example.a24270.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AllAdapter extends RecyclerView.Adapter{

    private List<Title> titleList = new ArrayList<>();
    private List<App> homelist = new ArrayList<>();
    private List<ImageView> imageViewList = new ArrayList<>();

    private CollectorDataHelper helper;
    private Context context;
    private int jug;

    public AllAdapter(List<ImageView> imageViewList, List<App> homelist, Context context ){
        this.homelist.addAll(homelist);
        this.imageViewList.addAll(imageViewList);
        this.context = context;
    }

    class ViewPagerHolder extends RecyclerView.ViewHolder{
        private ViewPager viewPager;

        public ViewPagerHolder(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.viewpager);
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder{
        View titleView;
        TextView authorText;
        TextView chapterNameText;
        TextView niceDateText;
        TextView titleText;
        ImageView collectorView;
        TitleViewHolder(View view){
            super(view);
            titleView = view;
            authorText = (TextView) view.findViewById(R.id.item_author);
            titleText = (TextView) view.findViewById(R.id.item_title);
            chapterNameText = (TextView) view.findViewById(R.id.item_chapterName);
            niceDateText = (TextView) view.findViewById(R.id.item_niceDate);
            collectorView= view.findViewById(R.id.collector_item);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.banner,parent,false);
                holder = new ViewPagerHolder(view);
                break;
            default:
                view = LayoutInflater.from(context).inflate(R.layout.title,parent,false);
                holder = new TitleViewHolder(view);
                ((TitleViewHolder)holder).titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = viewType;
                        Title title = titleList.get(position-1);
                        Intent intent = new Intent(context,web.class);
                        intent.putExtra("data",title.getLink());
                        context.startActivity(intent);

                    }
                });
                final RecyclerView.ViewHolder cholder = holder;
                ((TitleViewHolder)holder).collectorView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = viewType;
                        Title title = titleList.get(position-1);
                        int id = title.getId();
                        helper = new CollectorDataHelper(context,"Book",null,2);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        Cursor cursor = db.query("Book",null,null,null,null,null,null);
                        if (cursor.moveToFirst()){
                            jug = 0;
                            do {
                                int p = cursor.getInt(cursor.getColumnIndex("id"));
                                if (id == p){
                                    db.delete("Book","id=?",new String[] {String.valueOf(id)});
                                    ((TitleViewHolder)cholder).collectorView.setImageResource(R.drawable.favorites);
                                    Toast.makeText(context,"取消收藏",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                jug++;
                            }while (cursor.moveToNext());
                        }
                        if (jug == cursor.getCount()){
                            ContentValues values = new ContentValues();
                            values.put("title",title.getTitle());
                            values.put("author",title.getAuthor());
                            values.put("link",title.getLink());
                            values.put("id",title.getId());
                            db.insert("Book",null,values);
                            ((TitleViewHolder) cholder).collectorView.setImageResource(R.drawable.favorite);
                            Toast.makeText(context,"收藏成功",Toast.LENGTH_SHORT).show();
                        }
                        cursor.close();
                    }
                });
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                ViewPagerHolder Oholder = (ViewPagerHolder) holder;
                imageViewList = new ArrayList<>();
                for (int i = 0; i < homelist.size(); i++) {
                    ImageView imageView = new ImageView(context);
                    imageViewList.add(imageView);
                }
                ViewPageAdapter pagerAdapter = new ViewPageAdapter(homelist, imageViewList, context);
                Oholder.viewPager.setAdapter(pagerAdapter);
                break;
            default:
               final TitleViewHolder Tholder = (TitleViewHolder) holder;
                Title title = titleList.get(position-1);
                Tholder.chapterNameText.setText(title.getChapterName());
                Tholder.authorText.setText(title.getAuthor());
                Tholder.niceDateText.setText(title.getNiceDate());
                Tholder.titleText.setText(title.getTitle());
                int id = title.getId();
                int a = 1;
                helper = new CollectorDataHelper(context, "Book", null, 2);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()){
                    do {
                        int p = cursor.getInt(cursor.getColumnIndex("id"));
                        if (id == p){
                            Tholder.collectorView.setImageResource(R.drawable.favorite);
                            a = 0;
                            break;
                        }
                    }while (cursor.moveToNext());
                }
                if (a == 1){
                    Tholder.collectorView.setImageResource(R.drawable.favorites);
                }
        }
    }

    @Override public int getItemViewType(int position) {
        return position;
    }
    @Override public int getItemCount() {
        return titleList.size()+1;
    }
    public void initData(List<Title> itemBeansPart){
        titleList .addAll(itemBeansPart);
    }

}