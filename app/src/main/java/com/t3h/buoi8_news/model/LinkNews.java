package com.t3h.buoi8_news.model;

import com.t3h.buoi8_news.R;

public class LinkNews {
    private String link;
    private String title;
    private int img;


    public LinkNews(String link, String title,int img) {
        this.link = link;
        this.title = title;
        this.img = img;

    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
