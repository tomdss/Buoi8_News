package com.t3h.buoi8_news.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.t3h.buoi8_news.AppDatabase;
import com.t3h.buoi8_news.AppDatabaseFavorite;
import com.t3h.buoi8_news.MainActivity;
import com.t3h.buoi8_news.R;
import com.t3h.buoi8_news.WebviewActivity;
import com.t3h.buoi8_news.adapter.NewsAdapter;
import com.t3h.buoi8_news.connect.ConnectionDetector;
import com.t3h.buoi8_news.model.News;
import com.t3h.buoi8_news.parser.DownloadAsync;
import com.t3h.buoi8_news.parser.FileManager;
import com.t3h.buoi8_news.utils.DialogUtils;

import java.io.File;
import java.util.List;

public class FavoriteFragment extends BaseFragment implements NewsAdapter.FaceItemListener, DownloadAsync.DownloadCallback {

    private RecyclerView lvFavoriteNews;
    private NewsAdapter adapter;
    private List<News> data;


    private ProgressBar pbDownload;


    private List<File> dataFile;
    private String currentPath;

    private FileManager manager;
    private List<File> arrFile;

    private final String[] PERMISSTION_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };

    private boolean checkPermisstion() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            return true;
        }

        for (String p : PERMISSTION_LIST) {
            int accept = getActivity().checkSelfPermission(p);
            if (accept == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }

        return true;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();


        manager = new FileManager();

        if (checkPermisstion()) {
            readFile(manager.path);
        } else {
            requestPermissions(PERMISSTION_LIST, 0);
        }

    }

    private void initViews() {

        lvFavoriteNews = getActivity().findViewById(R.id.lv_favorite_news);
        pbDownload = getActivity().findViewById(R.id.pb_download);
        adapter = new NewsAdapter(getContext());
        lvFavoriteNews.setAdapter(adapter);
        getData();
        adapter.setListener(this);


    }

    @Override
    protected int getLayoutRes() {
        return R.layout.frag_favorite;
    }

    @Override
    public String getTitle() {
        return "Favorite";
    }


    public void getData() {
        data = AppDatabaseFavorite.getInstance(getContext()).getNewsDao().getAll();
        adapter.setData(data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(int position) {
        ConnectionDetector connectionDetector = new ConnectionDetector(this.getContext());

//        Toast.makeText(getActivity(), "onClick", Toast.LENGTH_SHORT).show();

        if (connectionDetector.isInternetAvailble())
            byExtra(adapter.getData().get(position).getLink());
        else
            byExtraPath(adapter.getData().get(position).getPath());

//        Toast.makeText(getActivity(), ""+adapter.getData().get(position).getPath(), Toast.LENGTH_SHORT).show();

    }

    public void byExtra(String link) {

        Intent intent = new Intent(getActivity(), WebviewActivity.class);

        intent.putExtra(MainActivity.REQUEST_LINK, link);
        this.startActivity(intent);
    }

    public void byExtraPath(String path) {

        Intent intent = new Intent(getActivity(), WebviewActivity.class);

        intent.putExtra(MainActivity.REQUEST_PATH, path);
        this.startActivity(intent);
    }


    @Override
    public void onLongClick(final int position) {


        PopupMenu popupMenu = new PopupMenu(this.getActivity(), lvFavoriteNews.findViewHolderForAdapterPosition(position).itemView);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_saved:
                        addSavedNews(position);
                        break;
                    case R.id.mn_delete:
                        showDeleteSaved(position);
                        break;
                    default:
                        break;

                }

                return false;
            }
        });


    }


    private void addSavedNews(int position) {
//        pbDownload.showContextMenu();
//        pbDownload.setVisibility(View.VISIBLE);
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
        if (link.isEmpty()) {
            Toast.makeText(getActivity(), "Link is Empty", Toast.LENGTH_SHORT).show();
            return;
        }


//        String path = currentPath+"/"+System.currentTimeMillis()+".html";//test vs hinh anh
//        news.setPath(path);
//        DownloadAsync download = new DownloadAsync(this);
//        download.execute(link,path);

        String path = currentPath + "/" + System.currentTimeMillis() + ".html";//test vs hinh anh
        news.setPath(path);
        DownloadAsync download = new DownloadAsync(this);
        download.execute(link, path);


        MainActivity act = (MainActivity) getActivity();

        AppDatabase.getInstance(getContext())
                .getNewsDao().insert(news);

        act.getFmSaved().getData();

//        AppDatabase.getInstance(getContext())
//                .getNewsDao().insert(news);

        byExtra(adapter.getData().get(position).getLink());


    }


    private void showDeleteSaved(final int position) {
        final MainActivity act = (MainActivity) getActivity();
        AppDatabaseFavorite.getInstance(getContext())
                .getNewsDao().delete(data.get(position));
        act.getFmFavorite().getData();
//                        adapter.notifyDataSetChanged();

    }


    private void readFile(String path) {
        currentPath = path;
        arrFile = manager.getFile(path);
        adapter.setDataFile(arrFile);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermisstion()) {
            readFile(manager.path);
        } else {
            getActivity().finish();
        }
    }


    @Override
    public void updatePercent(int percent) {

//        pbDownload.setProgress(percent);

    }

    @Override
    public void downloadFinish(String path) {

        readFile(currentPath);

//        Toast.makeText(this.getActivity(), ""+currentPath, Toast.LENGTH_SHORT).show();

//        pbDownload.setVisibility(View.GONE);


//        MainActivity act = (MainActivity) getActivity();
//
//
//
//        act.getFmSaved().getData();

        DialogUtils.dissmiss();


    }


}
