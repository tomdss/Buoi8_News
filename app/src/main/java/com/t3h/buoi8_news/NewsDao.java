package com.t3h.buoi8_news;

//truy van the trong bang

import android.arch.persistence.room.Dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

import com.t3h.buoi8_news.model.News;


@Dao
public interface NewsDao {
    @Query("SELECT * FROM News")
    List<News> getAll();


    @Insert
    void insert(News... news);
    @Update
    void update(News... news);
    @Delete
    void delete(News... news);
}
