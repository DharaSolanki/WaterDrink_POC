package com.gateway.waterdrink.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gateway.waterdrink.db.TodayEntry;


@Database(entities = { TodayEntry.class}, version = 1, exportSchema = false)
abstract class WaterDB extends RoomDatabase {
    abstract WaterDao getWaterDao();
    private static volatile WaterDB DB_INSTANCE;

    @NonNull
    static synchronized WaterDB getInstance(final Context appCtx, boolean inMemoryDBEnable) {
        if (DB_INSTANCE == null) {
            if (!inMemoryDBEnable) {
                DB_INSTANCE = Room
                        .databaseBuilder(appCtx, WaterDB.class, WaterDbUtils.DB_NAME)
                        .build(); }
            else {
                DB_INSTANCE = Room
                        .inMemoryDatabaseBuilder(appCtx, WaterDB.class)
                        .build(); }
        }
        return DB_INSTANCE;
    }

}
