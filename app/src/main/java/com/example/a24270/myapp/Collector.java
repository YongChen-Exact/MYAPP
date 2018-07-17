package com.example.a24270.myapp;

import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Collector extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    CollectorDataHelper helper;
    private List<Title_Collector> titlelist = new ArrayList<>();
    private RecyclerView recyclerView;
    CollectorAdapter collectorAdapter;
    private int J = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collector);

        recyclerView = (RecyclerView) findViewById(R.id.collector_item);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(Collector.this);
        layoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager3);
        recyclerView.addItemDecoration(new DividerItemDecoration(Collector.this, DividerItemDecoration.VERTICAL));//分隔线

        Loader();
        setRecycleView();
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Loader();
                setRecycleView();
                collectorAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "刷新成功！", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void Loader(){
        helper=new CollectorDataHelper(getApplicationContext(),"Book",null,2);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("Book",null,null,null,
                null,null,null);//一行数据，逐行查找
        cursor.moveToFirst();//光标移动到头部
        long count = cursor.getCount();
        if(count == 0){
            Toast.makeText(getApplicationContext(), "没有哒！", Toast.LENGTH_SHORT).show();
        }
        if(J>0){
            titlelist.clear();
        }J++;
        if (cursor.moveToFirst()){
            do {
                String author = cursor.getString(cursor.getColumnIndex("author"));
                String link = cursor.getString(cursor.getColumnIndex("link"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                Title_Collector m = new Title_Collector(author,link,title,id);
                titlelist.add(m);
            }while (cursor.moveToNext());
        }
    }
    private void setRecycleView(){
        collectorAdapter = new CollectorAdapter(titlelist,Collector.this);
        recyclerView.setAdapter(collectorAdapter);
    }
}
