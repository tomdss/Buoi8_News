package com.t3h.buoi8_news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.t3h.buoi8_news.adapter.NewsAdapter;

public class SavedActivity extends AppCompatActivity implements NewsAdapter.FaceItemListener {

    private NewsAdapter adapter;
    private RecyclerView lvSavedNews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        initViews();

    }

    private void initViews() {
        lvSavedNews=findViewById(R.id.lv_saved_news);
        adapter = new NewsAdapter(this);
        adapter.setListener(this);
        lvSavedNews.setAdapter(adapter);
    }




    @Override
    public void onClick(int position) {

    }

    @Override
    public void onLongClick(int position) {

    }
}
