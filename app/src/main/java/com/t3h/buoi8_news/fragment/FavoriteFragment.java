package com.t3h.buoi8_news.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.t3h.buoi8_news.AppDatabase;
import com.t3h.buoi8_news.AppDatabaseFavorite;
import com.t3h.buoi8_news.MainActivity;
import com.t3h.buoi8_news.R;
import com.t3h.buoi8_news.WebviewActivity;
import com.t3h.buoi8_news.adapter.NewsAdapter;
import com.t3h.buoi8_news.model.News;

import java.util.List;

public class FavoriteFragment extends Fragment implements NewsAdapter.FaceItemListener {

    private RecyclerView lvFavoriteNews;
    private NewsAdapter adapter;
    private List<News> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_favorite,container,false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lvFavoriteNews = getActivity().findViewById(R.id.lv_favorite_news);
        adapter = new NewsAdapter(getContext());
        lvFavoriteNews.setAdapter(adapter);
        getData();
        adapter.setListener(this);
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

//        Toast.makeText(getActivity(), "onClick", Toast.LENGTH_SHORT).show();

        byExtra(adapter.getData().get(position).getLink());

    }

    public void byExtra(String link){

        Intent intent = new Intent(getActivity(), WebviewActivity.class);

        intent.putExtra(MainActivity.REQUEST_LINK,link);
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
