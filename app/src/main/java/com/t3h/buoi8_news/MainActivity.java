package com.t3h.buoi8_news;


import android.Manifest;
import android.content.DialogInterface;

import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;

import com.t3h.buoi8_news.adapter.PageAdapter;
import com.t3h.buoi8_news.fragment.FavoriteFragment;
import com.t3h.buoi8_news.fragment.NewsFragment;
import com.t3h.buoi8_news.fragment.SavedFragment;

import com.t3h.buoi8_news.parser.XMLAsync;
import com.t3h.buoi8_news.utils.DialogUtils;



public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, ViewPager.OnPageChangeListener {


    public static final String REQUEST_LINK = "request.link";
    public static final String REQUEST_PATH = "request.path";


    private ViewPager pager;

    private PageAdapter adapter;

    private TabLayout tabTitle;

    private NewsFragment fmNews = new NewsFragment();
    private SavedFragment fmSaved = new SavedFragment();
    private FavoriteFragment fmFavorite = new FavoriteFragment();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
//        initFragments();
//        showFragment(fmSaved);
    }

    private void initViews() {

        pager = findViewById(R.id.pager);
        adapter = new PageAdapter(getSupportFragmentManager(),fmNews,fmSaved,fmFavorite);
        pager.setAdapter(adapter);



        tabTitle=findViewById(R.id.tab_title);
        tabTitle.setupWithViewPager(pager);

        pager.addOnPageChangeListener(this);

        pager.setCurrentItem(1);

//        adapter = new NewsAdapter(this);


    }



//    private void initFragments() {
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.panel_news,fmNews);
//        transaction.add(R.id.panel_news,fmSaved);
//        transaction.add(R.id.panel_news,fmFavorite);
//        transaction.commitNowAllowingStateLoss();
//
//    }


//    public void showFragment(Fragment fm) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
//        transaction.hide(fmNews);
//        transaction.hide(fmSaved);
//        transaction.hide(fmFavorite);
//        transaction.show(fm);
//        transaction.commitNowAllowingStateLoss();
//    }

    public void showFragment(Fragment fm){
        if(fm==fmNews)pager.setCurrentItem(0);
        if(fm==fmSaved)pager.setCurrentItem(1);
        if(fm==fmFavorite)pager.setCurrentItem(2);
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
//        showFragment(fmNews);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    public void onBackPressed() {
//        super.onBackPressed();

//        Toast.makeText(this, "Back press", Toast.LENGTH_SHORT).show();

        if(pager.getCurrentItem()==0){
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
        }else pager.setCurrentItem(pager.getCurrentItem()-1);



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

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {


    }

    @Override
    public void onPageScrollStateChanged(int i) {

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
