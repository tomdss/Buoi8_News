package com.t3h.buoi8_news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.t3h.buoi8_news.AppDatabase;
import com.t3h.buoi8_news.MainActivity;
import com.t3h.buoi8_news.R;
import com.t3h.buoi8_news.WebviewActivity;
import com.t3h.buoi8_news.adapter.NewsAdapter;
import com.t3h.buoi8_news.model.News;
import com.t3h.buoi8_news.parser.XMLAsync;
import com.t3h.buoi8_news.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment implements NewsAdapter.FaceItemListener, XMLAsync.ParserXMLCallback {



    private RecyclerView lvNews;
    private List<News> data;
    private NewsAdapter adapter;
    private TextView tvCaption;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_news,container,false);
        Log.e(getClass().getName(),"onCreateView");
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        Log.e(getClass().getName(),"onActivityCreated");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(getClass().getName(),"onDestroy");
    }

    private void initViews() {

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
        Toast.makeText(getActivity(), "On click "+position, Toast.LENGTH_SHORT).show();


        byExtra(adapter.getData().get(position).getLink());


    }

    public void byExtra(String link){

        Intent intent = new Intent(getActivity(), WebviewActivity.class);

        intent.putExtra(MainActivity.REQUEST_LINK,link);
        this.startActivity(intent);
    }






    @Override
    public void onLongClick(int position) {

        Toast.makeText(getActivity(), "SAVED "+position, Toast.LENGTH_SHORT).show();
        data = adapter.getData();


        News news = new News();

        news.setTitle(data.get(position).getTitle());
        news.setImage(data.get(position).getImage());
        news.setDesc(data.get(position).getDesc());
        news.setPubDate(data.get(position).getPubDate());
        news.setLink(data.get(position).getLink());

        MainActivity act = (MainActivity) getActivity();

        AppDatabase.getInstance(getContext())
                .getNewsDao().insert(news);


        act.showFragment(act.getFmSaved());
        act.getFmSaved().getData();


    }



//    @Override
//    public boolean onQueryTextSubmit(String s) {
//
//        if(s.isEmpty()){
//            return false;
//        }
//
//        DialogUtils.show(getActivity());
//
//        XMLAsync async = new XMLAsync((XMLAsync.ParserXMLCallback) getActivity());
//        async.execute(s);
//
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        return false;
//    }

    @Override
    public void onParserFinish(ArrayList<News> arr) {

        tvCaption.setVisibility(View.GONE);
        DialogUtils.dissmiss();
        adapter.setData(arr);


    }

}
