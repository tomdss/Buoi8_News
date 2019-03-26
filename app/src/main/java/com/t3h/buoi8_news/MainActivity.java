package com.t3h.buoi8_news;


import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.t3h.buoi8_news.fragment.FavoriteFragment;
import com.t3h.buoi8_news.fragment.NewsFragment;
import com.t3h.buoi8_news.fragment.SavedFragment;

import com.t3h.buoi8_news.parser.XMLAsync;
import com.t3h.buoi8_news.utils.DialogUtils;



public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,  View.OnClickListener {


    public static final String REQUEST_LINK = "request.link";

    private TextView tvNews;
    private TextView tvSaved;
    private TextView tvFavorite;

    private NewsFragment fmNews = new NewsFragment();
    private SavedFragment fmSaved = new SavedFragment();
    private FavoriteFragment fmFavorite = new FavoriteFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initFragments();
        showFragment(fmSaved);
    }

    private void initViews() {

        tvNews = findViewById(R.id.tv_news);
        tvSaved=findViewById(R.id.tv_saved);
        tvFavorite=findViewById(R.id.tv_favorite);
        tvNews.setOnClickListener(this);
        tvSaved.setOnClickListener(this);
        tvFavorite.setOnClickListener(this);
//        adapter = new NewsAdapter(this);


    }



    private void initFragments() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.panel_news,fmNews);
        transaction.add(R.id.panel_news,fmSaved);
        transaction.add(R.id.panel_news,fmFavorite);
        transaction.commitNowAllowingStateLoss();

    }


    public void showFragment(Fragment fm) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        transaction.hide(fmNews);
        transaction.hide(fmSaved);
        transaction.hide(fmFavorite);
        transaction.show(fm);
        showTextColor(fm);
        transaction.commitNowAllowingStateLoss();
    }

    private void showTextColor(Fragment fm) {
        if(fm==fmNews){
            tvNews.setTextColor(getResources().getColor(R.color.colorAccent));
            tvSaved.setTextColor(getResources().getColor(R.color.colorWhite));
            tvFavorite.setTextColor(getResources().getColor(R.color.colorWhite));
        }else if(fm==fmSaved){
            tvSaved.setTextColor(getResources().getColor(R.color.colorAccent));
            tvNews.setTextColor(getResources().getColor(R.color.colorWhite));
            tvFavorite.setTextColor(getResources().getColor(R.color.colorWhite));
        }else if(fm==fmFavorite){
            tvFavorite.setTextColor(getResources().getColor(R.color.colorAccent));
            tvNews.setTextColor(getResources().getColor(R.color.colorWhite));
            tvSaved.setTextColor(getResources().getColor(R.color.colorWhite));
        }
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

        XMLAsync async = new XMLAsync(this.fmNews);
        async.execute(s);

//        lvNews.getLayoutManager().scrollToPosition(0);
        showTextColor(fmNews);
        showFragment(fmNews);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

//    @Override
//    public void onParserFinish(ArrayList<News> arr) {
//
////        tvCaption.setVisibility(View.GONE);
//        DialogUtils.dissmiss();
//        adapter.setData(arr);
//
//    }




    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_news:
                showFragment(fmNews);


                break;

            case R.id.tv_saved:
                showFragment(fmSaved);

                break;

            case R.id.tv_favorite:
                showFragment(fmFavorite);

                break;

            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

//        Toast.makeText(this, "Back press", Toast.LENGTH_SHORT).show();

        new AlertDialog.Builder(this)
                .setTitle("Notify")
                .setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(MainActivity.this, "YES", Toast.LENGTH_SHORT).show();
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(MainActivity.this, "NO", Toast.LENGTH_SHORT).show();
                        return;
                    }
                })
                .show();

//        super.onBackPressed();

    }

    public NewsFragment getFmNews() {
        return fmNews;
    }

    public SavedFragment getFmSaved() {
        return fmSaved;
    }

    public FavoriteFragment getFmFavorite() {
        return fmFavorite;
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
