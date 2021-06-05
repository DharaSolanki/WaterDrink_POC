package com.gateway.waterdrink.db;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
import com.gateway.waterdrink.db.TodayEntry;


@Dao
public interface WaterDao {





//    @Query("DELETE FROM table_daily_logs")
//    void deleteAllLogs();

//    @Update
//    void updateDailyLog(DailyLog dailyLog);


//    @Nullable
//    @Query("SELECT * FROM table_daily_logs WHERE ID_LOGS =:id")
//    DailyLog getDailyLogByID(long id);

//    @Nullable
//    @Query("SELECT * FROM table_daily_logs ORDER BY ID_LOGS DESC")
//    List<DailyLog> getDailyLogList();



    //------------------------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertToday(TodayEntry today);
    @Delete
    void deleteToday(TodayEntry today);


    @Query("DELETE FROM table_today_entries")
    void deleteAllEntries();

//    @Update
//    void updateToday(TodayEntry today);
//
//
//
//
//    @Nullable
//    @Query("SELECT * FROM table_today_entries WHERE ID_TODAY =:id")
//    TodayEntry getTodayByID(long id);
//
//    @Nullable
//    @Query("SELECT * FROM table_today_entries ORDER BY ID_TODAY DESC")
//    List<TodayEntry> getTodayList();

    @Nullable
    @Query("SELECT * FROM table_today_entries ORDER BY ID_TODAY DESC")
    LiveData<List<TodayEntry>> getTodayListLive();


}
