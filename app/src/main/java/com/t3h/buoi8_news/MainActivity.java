package com.t3h.buoi8_news;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.widget.Toast;

import com.t3h.buoi8_news.adapter.NewsAdapter;
import com.t3h.buoi8_news.model.News;
import com.t3h.buoi8_news.parser.XMLAsync;
import com.t3h.buoi8_news.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, XMLAsync.ParserXMLCallback, NewsAdapter.FaceItemListener {

    public static final String REQUEST_LINK = "request.link";
    private NewsAdapter adapter;
    private RecyclerView lvNews;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {

        adapter = new NewsAdapter(this);
        adapter.setListener(this);

        lvNews = findViewById(R.id.lv_news);
        lvNews.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);//giup hien thi menu main

//        SearchManager manager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(this);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        if(s.isEmpty()){
            return false;
        }

        DialogUtils.show(this);

        XMLAsync async = new XMLAsync(this);
        async.execute(s);

        lvNews.getLayoutManager().scrollToPosition(0);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void onParserFinish(ArrayList<News> arr) {

        DialogUtils.dissmiss();
        adapter.setData(arr);

    }


    public void byExtra(String link){

        Intent intent = new Intent(MainActivity.this, WebviewActivity.class);

        intent.putExtra(REQUEST_LINK,link);
        this.startActivity(intent);
    }




    @Override
    public void onClick(int position) {

//        Toast.makeText(this, adapter.getData().get(position).getLink(), Toast.LENGTH_SHORT).show();

        byExtra(adapter.getData().get(position).getLink());

    }

    @Override
    public void onLongClick(int position) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    //xu ly su kien click item
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.menu_search:
//                Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.menu_setting:
//                Toast.makeText(this, "Setting Clicked", Toast.LENGTH_SHORT).show();
//                break;
//
//        }
//
//
//        return true;
//    }
}
