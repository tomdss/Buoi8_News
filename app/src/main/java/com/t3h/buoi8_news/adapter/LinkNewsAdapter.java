package com.t3h.buoi8_news.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.t3h.buoi8_news.R;
import com.t3h.buoi8_news.model.LinkNews;

import java.util.List;

public class LinkNewsAdapter extends RecyclerView.Adapter<LinkNewsAdapter.LinkNewsHolder> {

    private LinkNewsListener listener;
    private LayoutInflater inflater;
    private List<LinkNews> lnData;

    public LinkNewsAdapter(Context context, List<LinkNews> lnData) {
        this.lnData = lnData;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public LinkNewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = inflater.inflate(R.layout.item_link_news, viewGroup, false);
        LinkNewsHolder linkNewsHolder = new LinkNewsHolder(v);
        return linkNewsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LinkNewsHolder linkNewsHolder, final int position) {

        LinkNews linkNews = lnData.get(position);
        linkNewsHolder.bindData(linkNews);
        linkNewsHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickLN(position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return lnData == null ? 0 : lnData.size();
    }

    public void setListener(LinkNewsListener listener) {
        this.listener = listener;
    }

    public class LinkNewsHolder extends RecyclerView.ViewHolder {

        //        private TextView tvLnTitle;
        private ImageView imLogo;

        public LinkNewsHolder(@NonNull View itemView) {
            super(itemView);
//            tvLnTitle = itemView.findViewById(R.id.tv_ln_tittle);
            imLogo = itemView.findViewById(R.id.im_logo);

        }

        public void bindData(final LinkNews linkNews) {
//            tvLnTitle.setText(linkNews.getTitle());
//            imLogo.setImageResource(linkNews.getImg());
            Glide.with(imLogo)
                    .load(linkNews.getImg())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_img)
                    .into(imLogo);
        }

    }

    public interface LinkNewsListener {
        void onClickLN(int position);
    }

}
