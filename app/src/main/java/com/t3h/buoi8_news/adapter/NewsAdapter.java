package com.t3h.buoi8_news.adapter;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.t3h.buoi8_news.R;
import com.t3h.buoi8_news.model.News;


import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.FaceHolder> {

    private LayoutInflater inflater;//anh xa item_face thanh 1 view
    private ArrayList<News> data;
    private FaceItemListener listener;

    public NewsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<News> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public ArrayList<News> getData() {
        return data;
    }

    public void setListener(FaceItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FaceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = inflater.inflate(R.layout.item_news,
                viewGroup, false);
        FaceHolder holder = new FaceHolder(v);//anh xa layout  ra roi truyen vao holder
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FaceHolder faceHolder, final int position) {

        News f = data.get(position);
        faceHolder.bindData(f);
        faceHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

        faceHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (listener != null) {
                    listener.onLongClick(position);
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public class FaceHolder extends RecyclerView.ViewHolder {
        private ImageView ivNews;
        private TextView tvTitle;
        private TextView tvDesc;
        private TextView tvPubdate;


        public FaceHolder(@NonNull View itemView) {
            super(itemView);
            ivNews = itemView.findViewById(R.id.iv_news);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvPubdate = itemView.findViewById(R.id.tv_pubDate);

        }

        public void bindData(final News news) {

            tvTitle.setText(news.getTitle());
            tvPubdate.setText(news.getPubDate());
            tvDesc.setText(news.getDesc());


            Glide.with(ivNews)
                    .load(news.getImage())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_img)
                    .into(ivNews);
        }
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(news.getLink()));
//                    itemView.getContext().startActivity(intent);
//        }
//    });
    }

        public interface FaceItemListener {
            void onClick(int position);

            void onLongClick(int position);

        }


}


