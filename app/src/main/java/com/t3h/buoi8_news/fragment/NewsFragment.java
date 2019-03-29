package com.t3h.buoi8_news.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.t3h.buoi8_news.AppDatabase;
import com.t3h.buoi8_news.MainActivity;
import com.t3h.buoi8_news.R;
import com.t3h.buoi8_news.WebviewActivity;
import com.t3h.buoi8_news.adapter.NewsAdapter;
import com.t3h.buoi8_news.connect.ConnectionDetector;
import com.t3h.buoi8_news.model.News;
import com.t3h.buoi8_news.parser.DownloadAsync;
import com.t3h.buoi8_news.parser.FileManager;
import com.t3h.buoi8_news.parser.XMLAsync;
import com.t3h.buoi8_news.utils.DialogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment implements NewsAdapter.FaceItemListener, XMLAsync.ParserXMLCallback, DownloadAsync.DownloadCallback {



    private RecyclerView lvNews;
    private List<News> data;
    private NewsAdapter adapter;
    private TextView tvCaption;
    private ProgressBar pbDownload;


    private List<File> dataFile;
    private String currentPath;

    private FileManager manager;
    private List<File> arrFile;


    private final String[] PERMISSTION_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };

    private boolean checkPermisstion(){
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            return true;
        }

        for (String p:PERMISSTION_LIST) {
            int accept = getActivity().checkSelfPermission(p);
            if(accept == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }

        return true;
    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        Log.e(getClass().getName(),"onActivityCreated");

        manager = new FileManager();

        if(checkPermisstion()){
            readFile(manager.path);
        }else {
            requestPermissions(PERMISSTION_LIST,0);
        }

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.frag_news;
    }

    @Override
    public String getTitle() {
        return "News";
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(getClass().getName(),"onDestroy");
    }

    private void initViews() {

        pbDownload=getActivity().findViewById(R.id.pb_download);

        lvNews = getActivity().findViewById(R.id.lv_news);

        adapter = new NewsAdapter(getActivity());
        tvCaption = getActivity().findViewById(R.id.tv_caption);
        lvNews.setAdapter(adapter);
//        getData();
        adapter.setListener(this);


    }

//    public void getData() {
//        data = AppDatabase.getInstance(getContext()).getNewsDao().getAll();
//        adapter.setData(data);
//    }

    @Override
    public void onClick(int position) {
//        Toast.makeText(getActivity(), "On click "+position, Toast.LENGTH_SHORT).show();


        byExtra(adapter.getData().get(position).getLink());


    }

    public void byExtra(String link){

        Intent intent = new Intent(getActivity(), WebviewActivity.class);

        intent.putExtra(MainActivity.REQUEST_LINK,link);
        this.startActivity(intent);
    }






    @Override
    public void onLongClick(int position) {
//        pbDownload.showContextMenu();
        pbDownload.setVisibility(View.VISIBLE);
        DialogUtils.show(this.getActivity());

//        Toast.makeText(getActivity(), "SAVED "+position, Toast.LENGTH_SHORT).show();
        data = adapter.getData();


        News news = new News();

        news.setTitle(data.get(position).getTitle());
        news.setImage(data.get(position).getImage());
        news.setDesc(data.get(position).getDesc());
        news.setPubDate(data.get(position).getPubDate());
        news.setLink(data.get(position).getLink());





//        act.showFragment(act.getFmSaved());
//        act.showFragment(act.getFmSaved());




        String link = news.getLink();
        if(link.isEmpty()){
            Toast.makeText(getActivity(), "Link is Empty", Toast.LENGTH_SHORT).show();
            return;
        }



        String path = currentPath+"/"+System.currentTimeMillis()+".html";//test vs hinh anh
        news.setPath(path);
        DownloadAsync download = new DownloadAsync(this);
        download.execute(link,path);


        MainActivity act = (MainActivity) getActivity();

        AppDatabase.getInstance(getContext())
                .getNewsDao().insert(news);

        act.getFmSaved().getData();

//        AppDatabase.getInstance(getContext())
//                .getNewsDao().insert(news);

        byExtra(adapter.getData().get(position).getLink());


    }



    @Override
    public void onParserFinish(ArrayList<News> arr) {

        ConnectionDetector connectionDetector = new ConnectionDetector(this.getContext());
        if(!connectionDetector.isInternetAvailble()){
            Toast.makeText(getActivity(), "Check Your Connection and Try Again", Toast.LENGTH_LONG).show();
            DialogUtils.dissmiss();
            return;
        }

        tvCaption.setVisibility(View.GONE);
        DialogUtils.dissmiss();

        getParent().showFragment(getParent().getFmNews());

        adapter.setData(arr);


    }

    @Override
    public void updatePercent(int percent) {

        pbDownload.setProgress(percent);

    }

    @Override
    public void downloadFinish(String path) {

        readFile(currentPath);

//        Toast.makeText(this.getActivity(), ""+currentPath, Toast.LENGTH_SHORT).show();

        pbDownload.setVisibility(View.GONE);



//        MainActivity act = (MainActivity) getActivity();
//
//
//
//        act.getFmSaved().getData();

        DialogUtils.dissmiss();


    }

    private void readFile(String path){
        currentPath = path;
        arrFile = manager.getFile(path);
        adapter.setDataFile(arrFile);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(checkPermisstion()){
            readFile(manager.path);
        }else {
           getActivity().finish();
        }
    }
}
