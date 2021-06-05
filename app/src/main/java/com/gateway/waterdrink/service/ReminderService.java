package com.gateway.waterdrink.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.gateway.waterdrink.R;
import com.gateway.waterdrink.broadcastreciver.NotificationBR;
import com.gateway.waterdrink.utils.NotificationUtils;
import com.gateway.waterdrink.utils.PrefUtils;
import com.gateway.waterdrink.utils.PrefUtils.KEYS;
import com.gateway.waterdrink.utils.PrefUtils.Defaults;
import com.gateway.waterdrink.utils.TimeUtils;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReminderService extends Worker {
    private static final String TAG = "ReminderService ::";

    public ReminderService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context ctx = getApplicationContext();
        Log.i(TAG,"in dowork :::::::");
        SharedPreferences pref = ctx.getSharedPreferences(PrefUtils.PREF_NAME, Context.MODE_PRIVATE);
        boolean showDailyNotifs = pref.getBoolean(KEYS.SHOW_NOTIFS, Defaults.SHOW_NOTIF);

        String sleepTime = pref.getString(KEYS.SLEEP_TIME, Defaults.SLEEP_TIME);
        String wakeupTime = pref.getString(KEYS.WAKEUP_TIME, Defaults.WAKEUP_TIME);
        String currentTime = TimeUtils.getCurrentTime();
        boolean isBetweenSleep = TimeUtils.isTimeInBetween2Times(sleepTime, wakeupTime, currentTime);

        /*if (isBetweenSleep) {*/
            createNotification(pref, ctx);

        //}

        return Result.success();
    }

    private void createNotification(SharedPreferences pref, Context context) {
        Log.i(TAG,"in createNotification  ");
        String date = pref.getString(KEYS.DATE_TODAY, Defaults.DATE_TODAY);
        int achieved = pref.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
        int target = pref.getInt(KEYS.KEY_DAILY_TARGET, Defaults.DAILY_TARGET);

        String unit  = context.getString(R.string.text_ml);
        String Add  = context.getString(R.string.text_add);
        String Cancel  = context.getString(R.string.text_cancel);



        String title = context.getString(R.string.text_noti_title);

        String details = String.format(Locale.ROOT, "%s . Today's Progress:%d / %d %s. ", date, achieved, target,unit);
        //details += "\n Tap To Add "+Defaults.QTY+unit+" water to your daily Progress.";
        int resId = R.drawable.ic_200;

        Intent intent = new Intent(getApplicationContext(), NotificationSelector.class);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, 0);

        //creating actions
        Intent i = new Intent(getApplicationContext(), NotificationBR.class);
        i.putExtra("add", true);
        PendingIntent piDrankWater = PendingIntent.getBroadcast(
                context, 111, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action1 = new NotificationCompat.Action(
                android.R.drawable.ic_menu_add, Add+" "+Defaults.QTY+unit, piDrankWater);


        Intent i2 = new Intent(getApplicationContext(), NotificationBR.class);
        i2.putExtra("cancel", true);
        PendingIntent piCancelNoti = PendingIntent.getBroadcast(
                context, 222, i2, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action2 = new NotificationCompat.Action(
                android.R.drawable.ic_delete, Cancel, piCancelNoti);


        NotificationUtils notifMaker = new NotificationUtils(resId, context, title, details);
        notifMaker.setPendingIntent(pIntent);
        notifMaker.setActionBtn1(action1);
        notifMaker.setActionBtn2(action2);
        notifMaker.show();
    }
}
