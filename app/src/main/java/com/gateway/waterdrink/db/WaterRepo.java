package com.gateway.waterdrink.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class WaterRepo {
    //always check if you are handling null correctly
    private final WaterDao dao;
    private final ExecutorService ioExecutorService;
    private static volatile WaterRepo REPO_INSTANCE;

    private static final String TAG = "WATER_REPO ::";

    @NonNull
    public static synchronized WaterRepo getRepoInstance(Context appCtx) {
        if (REPO_INSTANCE == null) {
            REPO_INSTANCE = new WaterRepo(appCtx);
        }
        return REPO_INSTANCE;
    }

    private WaterRepo(Context appCtx) {
        this.dao = WaterDB.getInstance(appCtx, false).getWaterDao();
        this.ioExecutorService = Executors.newSingleThreadExecutor();
    }

    public void removeAllTodayEntries() {
        ioExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAllEntries();
            }
        });
    }



    public void addNewTodayEntry(@Nullable final TodayEntry entry) {
        if (entry != null) {

            ioExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    dao.insertToday(entry);
                }
            });
        }
        else {
            Log.e(TAG, "addNewTodayEntry: passed entry is null");
        }

    }




//    public void modifyOldTodayEntry(@Nullable final TodayEntry entry) {
//        if (entry != null) {
//            ioExecutorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    dao.updateToday(entry);
//                }
//            });
//        } else {
//            Log.e(TAG, "modifyOldTodayEntry: passed entry is null");
//        }
//
//    }


//    @Nullable
//    public TodayEntry getTodayEntryByID(final long id) {
//        try {
//            return ioExecutorService.submit(new Callable<TodayEntry>() {
//                @Override
//                public TodayEntry call() {
//                    return dao.getTodayByID(id);
//                }
//            }).get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Nullable
//    public List<TodayEntry> getAllTodayEntries() {
//        try {
//            return ioExecutorService.submit(new Callable<List<TodayEntry>>() {
//                @Override
//                public List<TodayEntry> call() {
//                    return dao.getTodayList();
//                }
//            }).get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    @Nullable
    public LiveData<List<TodayEntry>> getAllTodayEntriesObservable() {
        return dao.getTodayListLive();
    }

}
