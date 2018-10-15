package com.furkanturkmen.gamebacklog.utils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.furkanturkmen.gamebacklog.models.Game;


@Database(entities = {Game.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {

    public abstract GameDao gameDao();
    private final static String NAME_DATABASE = "gamebacklog_db";

    //Static instance
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {

        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context, AppDatabase.class, NAME_DATABASE).build();
        }
        return sInstance;
    }
}