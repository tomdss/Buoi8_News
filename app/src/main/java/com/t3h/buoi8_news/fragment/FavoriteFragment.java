package com.t3h.buoi8_news.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.t3h.buoi8_news.AppDatabaseFavorite;
import com.t3h.buoi8_news.MainActivity;
import com.t3h.buoi8_news.R;
import com.t3h.buoi8_news.WebviewActivity;
import com.t3h.buoi8_news.adapter.NewsAdapter;
import com.t3h.buoi8_news.connect.ConnectionDetector;
import com.t3h.buoi8_news.model.News;

import java.util.List;

public class FavoriteFragment extends BaseFragment implements NewsAdapter.FaceItemListener {

    private RecyclerView lvFavoriteNews;
    private NewsAdapter adapter;
    private List<News> data;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lvFavoriteNews = getActivity().findViewById(R.id.lv_favorite_news);
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

        if(connectionDetector.isInternetAvailble())
            byExtra(adapter.getData().get(position).getLink());
        else
            byExtraPath(adapter.getData().get(position).getPath());

//        Toast.makeText(getActivity(), ""+adapter.getData().get(position).getPath(), Toast.LENGTH_SHORT).show();

    }

    public void byExtra(String link){

        Intent intent = new Intent(getActivity(), WebviewActivity.class);

        intent.putExtra(MainActivity.REQUEST_LINK,link);
        this.startActivity(intent);
    }

    public void byExtraPath(String path){

        Intent intent = new Intent(getActivity(), WebviewActivity.class);

        intent.putExtra(MainActivity.REQUEST_PATH,path);
        this.startActivity(intent);
    }


    @Override
    public void onLongClick(final int position) {

        showDeleteSaved(position);

    }


    private void showDeleteSaved(final int position){
        final MainActivity act = (MainActivity) getActivity();

        new AlertDialog.Builder(getActivity())
                .setTitle("Delete")
                .setMessage("Do you want to delete?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AppDatabaseFavorite.getInstance(getContext())
                                .getNewsDao().delete(data.get(position));
                        act.getFmFavorite().getData();
//                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
