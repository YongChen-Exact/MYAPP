package com.example.a24270.myapp;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    private static final int UPDATE_TEXT1 = 1;
    private static final int UPDATE_TEXT2 = 2;
    private static final int UPDATE_TEXT3 = 3;
    private static  int page = 0;
    private static final String TAG = "MainActivity";

    private AllAdapter allAdapter;
    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;

    private List<App> homelist = new ArrayList<>();
    private List<Title> titlelist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));//分隔线
        recyclerView.addOnScrollListener(scrollListener);

        sendRequestWithOkHttp1();
        sendRequestWithOkHttp2();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.chc);
        }
        navView.setCheckedItem(R.id.collector);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = new Intent(MainActivity.this,Collector.class);
                startActivity(intent);
                return true;
            }
        });
        View view = navView.inflateHeaderView(R.layout.nav_header);
        TextView textView = view.findViewById(R.id.changeUser);
        textView.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.changeUser:
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    break;
                 default:
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
                default:
        }
        return true;
    }


    private void sendRequestWithOkHttp1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://www.wanandroid.com/banner/json")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject1(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void parseJSONWithJSONObject1(String jsonData) {
        try {
            JSONObject object = new JSONObject(jsonData);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                final JSONObject jsonObject = data.getJSONObject(i);
                String title = jsonObject.getString("title");
                String imagePath = jsonObject.getString("imagePath");
                String url = jsonObject.getString("url");
                App m1 = new App(title,url,imagePath);
                homelist.add(m1);
            }
            Message message = new Message();
            message.what = UPDATE_TEXT1;
            handler.sendMessage(message);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void sendRequestWithOkHttp2(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String U="http://www.wanandroid.com/article/list/"+page+"/json";
                page++;
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(U)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject2(responseData,page);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithJSONObject2(String jsonData,int page) {
        List<Title> mlist = new ArrayList<>();
        try {
            JSONObject object1 = new JSONObject(jsonData);
            JSONObject object2 = object1.getJSONObject("data");
            JSONArray data = object2.getJSONArray("datas");
            for (int i = 0; i < data.length(); i++) {
                final JSONObject jsonObject = data.getJSONObject(i);
                String author = jsonObject.getString("author");
                String chapterName = jsonObject.getString("chapterName");
                String niceDate = jsonObject.getString("niceDate");
                String title = jsonObject.getString("title");
                String link = jsonObject.getString("link");
                int id = jsonObject.getInt("id");
                Title m = new Title(author,chapterName,niceDate,title,link,id);
                mlist.add(m);
            }

            Message message = new Message();
            message.what = UPDATE_TEXT2;
            message.arg1 = page;
            /**这个mlist拿到handler 里面是因为不能在子线程添加数据。之前在解析后直接add到titlelist里面的写法是错误的！要保证同步必须拿到主线程赋值并且添加数据
             * 下面的pages 也是同理*/
            message.obj = mlist;
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<ImageView> imageViewList = new ArrayList<>();
    private List<TextView> textViewList = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT1:
                    for (int i = 0; i < homelist.size(); i++) {
                        ImageView imageView = new ImageView(MainActivity.this);
                        imageViewList.add(imageView);
                    }
                        allAdapter = new AllAdapter(imageViewList, homelist, MainActivity.this, titlelist);
                        recyclerView.setAdapter(allAdapter);
//                    ViewPager viewPager = findViewById(R.id.viewpager);
//                    for (int i = 0; i < homelist.size(); i++) {
//                        ImageView imageView = new ImageView(MainActivity.this);
//                        imageViewList.add(imageView);
//                    }
//                    ViewPageAdapter pagerAdapter = new ViewPageAdapter(homelist,imageViewList,MainActivity.this);
//                    viewPager.setAdapter(pagerAdapter);

//                    RecyclerView recyclerView1=(RecyclerView) findViewById(R.id.recycler_item1);
//                    LinearLayoutManager layoutManager1 = new LinearLayoutManager(MainActivity.this);
//                    layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
//                    recyclerView1.setLayoutManager(layoutManager1);//将实例存于recyclerview中;
//                    HomeAdapter adapter1= new HomeAdapter(homelist,MainActivity.this);
//                    recyclerView1.setAdapter(adapter1);
                        break;
                        case UPDATE_TEXT2:
                            int pages = (int)msg.arg1;
                            titlelist = (List<Title>)msg.obj;
                            /**TitleAdapter要new，之前会崩的原因是没有new */
                            allAdapter.setTitleList(titlelist);
                            loadMore(pages);
                            break;
                        case UPDATE_TEXT3:
                            break;
                        default:
                            break;
                    }
                 }
               };
    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (newState == RecyclerView.SCROLL_STATE_IDLE){
                int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                Log.e(TAG, "onScrollStateChanged: "+lastVisiblePosition);
                if (lastVisiblePosition+1 == recyclerView.getAdapter().getItemCount()){
                    sendRequestWithOkHttp2();
                }
            }
        }
    };

    private void loadMore(int pages) {
        if (pages < 5) {
            allAdapter.initData(titlelist);
            allAdapter.notifyDataSetChanged();
            titlelist.clear();
        } else {
            Toast.makeText(getApplicationContext(), "没有哒！", Toast.LENGTH_SHORT).show();
        }
    }
}