package com.example.a24270.myapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class CollectorDataHelper extends SQLiteOpenHelper{

    public static final String CREATE_BOOK = "create table Book("
            + "title text,"
            + "author text,"
            +"link text,"
            +"id integer primary key)";
    private Context mContext;
    public CollectorDataHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_BOOK);
        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
    }
}
