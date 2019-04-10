package com.t3h.buoi8_news.fragment;


import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;


import android.support.annotation.Nullable;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.t3h.buoi8_news.AppDatabase;

import com.t3h.buoi8_news.MainActivity;
import com.t3h.buoi8_news.R;
import com.t3h.buoi8_news.WebviewActivity;
import com.t3h.buoi8_news.adapter.NewsAdapter;
import com.t3h.buoi8_news.connect.ConnectionDetector;
import com.t3h.buoi8_news.model.News;
import com.t3h.buoi8_news.utils.DialogDelete;


import java.util.List;

public class SavedFragment extends BaseFragment implements NewsAdapter.FaceItemListener,DialogDelete.DeleteCallback {

    private RecyclerView lvSavedNews;
    private NewsAdapter adapter;
    private List<News> data;
    private DialogDelete dialogDelete;
    private int positionDelete;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();

        dialogDelete = new DialogDelete(this.getActivity());
        dialogDelete.setCallback(this);

        Log.e(getClass().getName(), "onActivityCreated");

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.frag_saved;
    }

    @Override
    public String getTitle() {
        return "Saved";
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
        positionDelete = position;
        showDeleteSaved();

    }


    private void showDeleteSaved() {

//        new AlertDialog.Builder(getActivity())
//                .setTitle("Delete")
//                .setMessage("Do you want to delete?")
//                .setCancelable(false)
//                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        AppDatabase.getInstance(getContext())
//                                .getNewsDao().delete(data.get(position));
//                        act.getFmSaved().getData();
////                        adapter.notifyDataSetChanged();
//                    }
//                })
//                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();


        dialogDelete.show();
        dialogDelete.setCancelable(true);

    }

    @Override
    public void onYesClick() {

        MainActivity act = (MainActivity) getActivity();

        AppDatabase.getInstance(getContext())
                                .getNewsDao().delete(data.get(positionDelete));
                        act.getFmSaved().getData();

    }

}
