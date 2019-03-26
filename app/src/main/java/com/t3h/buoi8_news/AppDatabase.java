package com.t3h.buoi8_news;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.t3h.buoi8_news.model.News;

@Database(entities = {News.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance = null;


    public static AppDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context,AppDatabase.class, "news_manager")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }


    public abstract NewsDao getNewsDao();
}
