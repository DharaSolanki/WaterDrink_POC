package com.gateway.waterdrink.broadcastreciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.gateway.waterdrink.db.TodayEntry;
import com.gateway.waterdrink.db.WaterRepo;
import com.gateway.waterdrink.utils.AndroidBasedUtils;
import com.gateway.waterdrink.utils.NotificationUtils;
import com.gateway.waterdrink.utils.PrefUtils.KEYS;
import com.gateway.waterdrink.utils.PrefUtils.Defaults;
import com.gateway.waterdrink.utils.TimeUtils;

import static com.gateway.waterdrink.utils.PrefUtils.PREF_NAME;


public class NotificationBR extends BroadcastReceiver {
    private static final String TAG = "NotificationBR ::";
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getBooleanExtra("add", false)) {
            Log.e(TAG, "add ");
            doActionsForUserDrankWater(context);
        }
        if (intent.getBooleanExtra("cancel", false)) {
            Log.e(TAG, "Dismiss notification");
            NotificationUtils.cancelAll(context.getApplicationContext());
        }


    }


    private void doActionsForUserDrankWater(Context context) {
        SharedPreferences prefMain = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        WaterRepo repo = WaterRepo.getRepoInstance(context.getApplicationContext());

        String time = TimeUtils.getCurrentTime();
        TodayEntry entry = new TodayEntry();
        entry.setTime(time);
        entry.setAmountInMilliLitres(200);

        int achieved = prefMain.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
        prefMain.edit().putInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, achieved + 200).apply();
        repo.addNewTodayEntry(entry);
        AndroidBasedUtils.makeDateChanges(prefMain, repo);
        NotificationUtils.cancelAll(context.getApplicationContext());

    }
}
