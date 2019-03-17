package com.t3h.buoi8_news;

import android.app.SearchManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.t3h.buoi8_news.adapter.NewsAdapter;
import com.t3h.buoi8_news.model.News;
import com.t3h.buoi8_news.parser.XMLAsync;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, XMLAsync.ParserXMLCallback, NewsAdapter.FaceItemListener {

    private List<News> data = new ArrayList<>();
    private NewsAdapter adapter;
    private RecyclerView lvNews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {

        adapter = new NewsAdapter(this, data);
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

        XMLAsync async = new XMLAsync(this);
        async.execute(s);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void onParserFinish(ArrayList<News> arr) {

    }

    @Override
    public void onClick(int position) {

    }

    @Override
    public void onLongClick(int position) {

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
