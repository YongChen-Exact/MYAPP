package com.example.a24270.myapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;




public class CollectorAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Title_Collector> titleList = new ArrayList<>();
        private CollectorDataHelper helper;
        private Context context;
        static class ViewHolder_T extends RecyclerView.ViewHolder {
            View titleView;
            TextView authorText;
            TextView titleText;
            TextView collectorView;

            public ViewHolder_T(View view) {
                super(view);
                titleView = view;
                authorText = (TextView) view.findViewById(R.id.item_author1);
                titleText = (TextView) view.findViewById(R.id.item_title1);
                collectorView= (TextView) view.findViewById(R.id.collector_button);

            }
        }

        public CollectorAdapter(List<Title_Collector> titleList, Context context) {
            this.context = context;
            initData(titleList);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            helper = new CollectorDataHelper(context,"Book",null,2);
            helper.getWritableDatabase();
            if(titleList.size() == 0){
                Toast.makeText(context, "空空如也！", Toast.LENGTH_SHORT).show();
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.title_collector, parent, false);
            final RecyclerView.ViewHolder holder = new ViewHolder_T(view);

            ((ViewHolder_T)holder).titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Title_Collector title = titleList.get(position);
                    Intent intent = new Intent(context, web.class);
                    intent.putExtra("data", title.getLink());
                    context.startActivity(intent);

                }
            });

            ((ViewHolder_T)holder).collectorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("警告");
                    dialog.setMessage("确认取消收藏该内容？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int position = holder.getAdapterPosition();
                            Title_Collector title = titleList.get(position);
                            int id=title.getId();
                            SQLiteDatabase db = helper.getWritableDatabase();
                            db.delete("Book","id=?",new String[] {String.valueOf(id)});
                            ((ViewHolder_T)holder).collectorView.setText("已取消");
                            Toast.makeText(context,"已成功取消！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"我们还是继续相濡以沫吧!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
            });
            return holder;
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Title_Collector item = titleList.get(position);
            if  (holder instanceof ViewHolder_T){
                ((ViewHolder_T)holder).authorText.setText(item.getAuthor());
                ((ViewHolder_T)holder).titleText.setText(item.getTitle());
            }
        }

        @Override
        public int getItemCount() {
            return titleList.size();
        }


        public void initData(List<Title_Collector> itemBeansPart){
            titleList .addAll(itemBeansPart);
        }

    }


