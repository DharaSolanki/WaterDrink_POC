package com.gateway.waterdrink.utils;

import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.gateway.waterdrink.db.WaterRepo;

import androidx.annotation.Nullable;

import com.gateway.waterdrink.utils.PrefUtils.Defaults;
import com.gateway.waterdrink.utils.PrefUtils.KEYS;

public class AndroidBasedUtils {

    /**
     * A function to check weather the current date(i.e the date at which this method is called)
     * is equal to 'savedDate' attribute inside prefUserDetails. if True, it won't do anything.
     * If false, it will:  </br>
     *     1. add old date's log to logs table   </br>
     *     2. reset date and achieved qty in preferences   </br>
     *     3. clear allitems in today entries table table   </br>
     * */
    public static void makeDateChanges(@Nullable SharedPreferences prefBasicInfo,@Nullable WaterRepo repo) {
        if(prefBasicInfo == null || repo == null) return;
        boolean showLogs = prefBasicInfo.getBoolean(PrefUtils.KEYS.KEY_SHOW_LOGS, PrefUtils.Defaults.SHOW_LOGS);
        if (showLogs) {
            String dateFromPref = prefBasicInfo.getString(KEYS.DATE_TODAY, Defaults.DATE_TODAY);
            String today = TimeUtils.getCurrentDate();
            if (!dateFromPref.equals(today)) {
                int achievedQty = prefBasicInfo.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
                int targetQty = prefBasicInfo.getInt(KEYS.KEY_DAILY_TARGET, Defaults.DAILY_TARGET);


                //2.resetted date and intake to today,0
                prefBasicInfo.edit()
                        .putString(PrefUtils.KEYS.DATE_TODAY, Defaults.DATE_TODAY)
                        .putInt(
                                KEYS.KEY_TODAY_INTAKE_ACHIEVED,
                                Defaults.TODAY_INTAKE_ACHIEVED
                        )
                        .apply();

                //3
                repo.removeAllTodayEntries();
            }
        }


    }
    



}
