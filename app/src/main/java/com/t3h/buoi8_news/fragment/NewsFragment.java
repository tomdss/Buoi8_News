package com.t3h.buoi8_news.fragment;


import android.content.Intent;

import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;


import android.widget.TextView;
import android.widget.Toast;

import com.t3h.buoi8_news.AppDatabaseFavorite;
import com.t3h.buoi8_news.MainActivity;
import com.t3h.buoi8_news.R;
import com.t3h.buoi8_news.WebviewActivity;
import com.t3h.buoi8_news.adapter.LinkNewsAdapter;
import com.t3h.buoi8_news.adapter.NewsAdapter;
import com.t3h.buoi8_news.connect.ConnectionDetector;
import com.t3h.buoi8_news.model.LinkNews;
import com.t3h.buoi8_news.model.News;

import com.t3h.buoi8_news.parser.XMLAsync;
import com.t3h.buoi8_news.utils.DialogUtils;


import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment implements NewsAdapter.FaceItemListener, XMLAsync.ParserXMLCallback, LinkNewsAdapter.LinkNewsListener {


    private RecyclerView lvNews;
    private List<News> data;
    private NewsAdapter adapter;
    private TextView tvCaption;


    private LinkNewsAdapter linkNewsAdapter;
    private RecyclerView lvLinkNews;
    private List<LinkNews> lnData;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addLinkNewsData();
        initViews();
        Log.e(getClass().getName(), "onActivityCreated");


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
        Log.e(getClass().getName(), "onDestroy");
    }

    private void initViews() {
        tvCaption = getActivity().findViewById(R.id.tv_caption);

        lvNews = getActivity().findViewById(R.id.lv_news);
        lvNews.setVisibility(View.GONE);
        adapter = new NewsAdapter(getActivity());
        lvNews.setAdapter(adapter);

        adapter.setListener(this);
//        getData();
        lvLinkNews = getActivity().findViewById(R.id.lv_linkNews);
        linkNewsAdapter = new LinkNewsAdapter(getActivity(), lnData);
        lvLinkNews.setAdapter(linkNewsAdapter);
        linkNewsAdapter.setListener(this);
    }

    private void addLinkNewsData() {
        lnData = new ArrayList<>();
        lnData.add(new LinkNews("https://vnexpress.net/", "VnExpress", R.drawable.vnex));
        lnData.add(new LinkNews("https://news.zing.vn/", "Zing", R.drawable.zings));
        lnData.add(new LinkNews("https://www.24h.com.vn/", "24h", R.drawable.haituh));
        lnData.add(new LinkNews("http://genk.vn/", "Genk", R.drawable.genk));
        lnData.add(new LinkNews("https://www.facebook.com/", "Facebook", R.drawable.fb));
        lnData.add(new LinkNews("http://gamek.vn/", "Gamek", R.drawable.gamek));

        lnData.add(new LinkNews("https://dantri.com.vn/", "Dantri", R.drawable.dantris));
        lnData.add(new LinkNews("https://thanhnien.vn/", "Thanhnien", R.drawable.thanhnien));
        lnData.add(new LinkNews("http://kenh14.vn/", "Kenh14", R.drawable.kenh14));
        lnData.add(new LinkNews("https://tuoitre.vn/", "Tuoitre", R.drawable.tuoitre));
        lnData.add(new LinkNews("https://techtalk.vn/", "TechTalk", R.drawable.techts));
        lnData.add(new LinkNews("http://cafebiz.vn/", "Cafebiz", R.drawable.cafebiz));

        lnData.add(new LinkNews("https://www.techz.vn/", "TechZ", R.drawable.techz));
        lnData.add(new LinkNews("https://www.google.com.vn/", "Google", R.drawable.google));
        lnData.add(new LinkNews("https://www.instagram.com/", "Instagram", R.drawable.insta));

        lnData.add(new LinkNews("http://f.m.bongdaplus.vn/", "BongdaPlus", R.drawable.bdplus));
        lnData.add(new LinkNews("https://m.livescore.com/", "Livescore", R.drawable.livescore));
        lnData.add(new LinkNews("https://twitter.com/", "Twitter", R.drawable.twitter));

    }


    @Override
    public void onClick(int position) {
//        Toast.makeText(getActivity(), "On click "+position, Toast.LENGTH_SHORT).show();


        byExtra(adapter.getData().get(position).getLink());


    }

    public void byExtra(String link) {

        Intent intent = new Intent(getActivity(), WebviewActivity.class);

        intent.putExtra(MainActivity.REQUEST_LINK, link);
        this.startActivity(intent);
    }


    @Override
    public void onLongClick(final int position) {


        PopupMenu popupFavor = new PopupMenu(this.getActivity(), lvNews.findViewHolderForAdapterPosition(position).itemView);
        popupFavor.getMenuInflater().inflate(R.menu.popup_favor, popupFavor.getMenu());
        popupFavor.show();

        popupFavor.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_favorite:
                        addFavoriteNews(position);

                        break;
//                    case R.id.mn_delete:
//                        showDeleteSaved(position);
//                        break;
                    default:
                        break;

                }

                return false;
            }
        });


    }


    private void addFavoriteNews(int position) {
//        Toast.makeText(getActivity(), "My Favorite", Toast.LENGTH_SHORT).show();

        data = adapter.getData();
        News news = new News();

        news.setTitle(data.get(position).getTitle());
        news.setImage(data.get(position).getImage());
        news.setDesc(data.get(position).getDesc());
        news.setPubDate(data.get(position).getPubDate());
        news.setLink(data.get(position).getLink());
        news.setPath(data.get(position).getPath());


        MainActivity act = (MainActivity) getActivity();

        AppDatabaseFavorite.getInstance(getContext())
                .getNewsDao().insert(news);


//        act.showFragment(act.getFmFavorite());
        act.getFmFavorite().getData();
        act.setCurrentItem(1);

    }


    @Override
    public void onParserFinish(ArrayList<News> arr) {

        lvLinkNews.setVisibility(View.GONE);
        lvNews.setVisibility(View.VISIBLE);



        //check co mang hay khong
        ConnectionDetector connectionDetector = new ConnectionDetector(this.getContext());
        if (!connectionDetector.isInternetAvailble()) {
            Toast.makeText(getActivity(), "Check Your Connection and Try Again", Toast.LENGTH_LONG).show();
            DialogUtils.dissmiss();
            return;
        }

        tvCaption.setVisibility(View.GONE);
        lvLinkNews.setVisibility(View.GONE);
        DialogUtils.dissmiss();

        getParent().showFragment(getParent().getFmNews());

        adapter.setData(arr);


    }


    @Override
    public void onClickLN(int position) {
//        Toast.makeText(getActivity(), position+"ON CLICK", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this.getActivity(), WebviewActivity.class);
        i.putExtra(MainActivity.REQUEST_LINK, lnData.get(position).getLink());
        startActivity(i);

    }

}
