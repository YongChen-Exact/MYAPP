package com.example.a24270.myapp;

/**
 * Created by 24270 on 2018/5/30.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import static android.support.constraint.Constraints.TAG;


public class TitleAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Title> titleList = new ArrayList<>();
    private CollectorDataHelper helper;
    private Context context;
    private int jug;

    private boolean flag;

    private static final int ITEM_HOME = 0;

    private static final int ITEM_TITLE = 1;

     static class ViewHolder_T extends RecyclerView.ViewHolder {
        View titleView;
        TextView authorText;
        TextView chapterNameText;
        TextView niceDateText;
        TextView titleText;
        ImageView collectorView;

        public ViewHolder_T(View view) {
            super(view);
            titleView = view;
            authorText = (TextView) view.findViewById(R.id.item_author);
            titleText = (TextView) view.findViewById(R.id.item_title);
            chapterNameText = (TextView) view.findViewById(R.id.item_chapterName);
            niceDateText = (TextView) view.findViewById(R.id.item_niceDate);
            collectorView= view.findViewById(R.id.collector_item);

        }
    }


//    public TitleAdapter(List<Title> titleList, Context context) {
//        this.context = context;
//        this.titleList=titleList;
//        initData(titleList);
//    }

    public TitleAdapter( List<Title> itemBeansPart,Context context) {
        this.context = context;
        titleList = itemBeansPart;
        Log.e("title", "TitleAdapter: "+itemBeansPart);
        Log.d(TAG, "TitleAdapter: "+titleList.get(0).getAuthor()+titleList.get(0).getChapterName());
        Log.e("title", "TitleAdapter: "+titleList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                 helper = new CollectorDataHelper(context,"Book",null,2);
                helper.getWritableDatabase();
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.title, parent, false);
            final RecyclerView.ViewHolder holder = new ViewHolder_T(view);
            ((ViewHolder_T) holder).titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Title title = titleList.get(position);
                    Intent intent = new Intent(context, web.class);
                    intent.putExtra("data", title.getLink());
                    context.startActivity(intent);

                }
            });

        ((ViewHolder_T)holder).collectorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Title title = titleList.get(position);
                int id=title.getId();
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.query("Book",null,null,null,null,null,null);
                if (cursor.moveToFirst()){
                    jug = 0;
                    do {
                        int p = cursor.getInt(cursor.getColumnIndex("id"));
                        if (id == p){
                            db.delete("Book","id=?",new String[] {String.valueOf(id)});
                            ((ViewHolder_T) holder).collectorView.setImageResource(R.drawable.favorites);
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
                    ((ViewHolder_T) holder).collectorView.setImageResource(R.drawable.favorite);
                    Toast.makeText(context,"收藏成功",Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Title item = titleList.get(position);
        if (holder instanceof ViewHolder_T){
            ((ViewHolder_T)holder).authorText.setText(item.getAuthor());
            ((ViewHolder_T)holder).niceDateText.setText(item.getNiceDate());
            ((ViewHolder_T)holder).titleText.setText(item.getTitle());
            ((ViewHolder_T)holder).chapterNameText.setText(item.getChapterName());
            Title title = titleList.get(position);
            int id=title.getId();
            int a=1;
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor = db.query("Book",null,null,null,null,null,null);
            if (cursor.moveToFirst()){
                do {
                    int p = cursor.getInt(cursor.getColumnIndex("id"));
                    if (id == p){
                        ((ViewHolder_T) holder).collectorView.setImageResource(R.drawable.favorites);
                        a = 0;
                        break;
                    }
                }while (cursor.moveToNext());
            }
            if (a == 1){
                ((ViewHolder_T) holder).collectorView.setImageResource(R.drawable.favorites);
            }
        }

    }

    @Override
    public int getItemCount() {
            return titleList.size();
    }


    public void initData(List<Title> itemBeansPart){
        titleList .addAll(itemBeansPart);
    }

}
