package com.t3h.buoi8_news.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.t3h.buoi8_news.AppDatabase;
import com.t3h.buoi8_news.AppDatabaseFavorite;
import com.t3h.buoi8_news.MainActivity;
import com.t3h.buoi8_news.R;
import com.t3h.buoi8_news.WebviewActivity;
import com.t3h.buoi8_news.adapter.NewsAdapter;
import com.t3h.buoi8_news.model.News;

import java.util.List;

public class SavedFragment extends Fragment implements NewsAdapter.FaceItemListener {

    private RecyclerView lvSavedNews;
    private NewsAdapter adapter;
    private List<News> data;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_saved,container,false);
        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        Log.e(getClass().getName(),"onActivityCreated");
    }

    private void initViews() {
        lvSavedNews = getActivity().findViewById(R.id.lv_saved_news);

        adapter = new NewsAdapter(getActivity());
        lvSavedNews.setAdapter(adapter);
        getData();
        adapter.setListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getData() {
        data = AppDatabase.getInstance(getContext()).getNewsDao().getAll();
        adapter.setData(data);
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
        PopupMenu popupMenu = new PopupMenu(this.getActivity(),lvSavedNews.findViewHolderForAdapterPosition(position).itemView);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mn_favorite:
                        addFavoriteNews(position);
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


        Toast.makeText(getActivity(), "onLongClick", Toast.LENGTH_SHORT).show();


    }

    private void addFavoriteNews(int position) {
        Toast.makeText(getActivity(), "My Favorite", Toast.LENGTH_SHORT).show();

        data = adapter.getData();
        News news = new News();

        news.setTitle(data.get(position).getTitle());
        news.setImage(data.get(position).getImage());
        news.setDesc(data.get(position).getDesc());
        news.setPubDate(data.get(position).getPubDate());
        news.setLink(data.get(position).getLink());

        MainActivity act = (MainActivity) getActivity();

        AppDatabaseFavorite.getInstance(getContext())
                .getNewsDao().insert(news);


        act.showFragment(act.getFmFavorite());
        act.getFmFavorite().getData();

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
                        AppDatabase.getInstance(getContext())
                                .getNewsDao().delete(data.get(position));
                        act.getFmSaved().getData();
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
