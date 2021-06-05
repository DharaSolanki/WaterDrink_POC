package com.gateway.waterdrink.db;

import com.gateway.waterdrink.utils.TimeUtils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;




@Entity(tableName = WaterDbUtils.TT.Names.TABLE_NAME)
public class TodayEntry {
    @ColumnInfo(name = WaterDbUtils.TT.Names.COL_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = WaterDbUtils.TT.Names.COL_TIME)  //pattern must be hh:mm a
    private String time;

    @ColumnInfo(name = WaterDbUtils.TT.Names.COL_AMOUNT)
    private int amountInMilliLitres;

    public TodayEntry() {
        this.time= TimeUtils.getCurrentTime();
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    public int getAmountInMilliLitres() {
        return amountInMilliLitres;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    public void setAmountInMilliLitres(int amountInMilliLitres) {
        this.amountInMilliLitres = amountInMilliLitres;
    }


    @Override @NonNull
    public String toString() {
        return String.format(Locale.ROOT, "TodayEntry{id=%d, time='%s', amount=%d}", id, time, amountInMilliLitres);
    }
}
