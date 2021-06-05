package com.gateway.waterdrink.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.gateway.waterdrink.utils.PrefUtils;
import com.gateway.waterdrink.utils.PrefUtils.KEYS;
  import      com.gateway.waterdrink.utils.PrefUtils.Defaults;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import static com.gateway.waterdrink.utils.PrefUtils.PREF_NAME;

public class StartNotification {
    private static StartNotification INSTANCE = null;
    private static final String TAG = "StartNotification ::";
    int NOTIF_MINUTES=15;
    private static final String TAG_REMINDER_NOTIFY_REQ = "reminder_notify_service_req";
    private static final String PREF_KEY_NOTIFIER_S = "reminder_notify_service";
    private final WorkManager workManagerObj;
    private final SharedPreferences prefUUID;
    private static Context con;

    private StartNotification(Context ctx) {
        Context appCtx = ctx.getApplicationContext();
        this.workManagerObj = WorkManager.getInstance(appCtx);

        String prefName = "UUID_HANDLER";
        this.prefUUID = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }
    private static StartNotification getInstance(Context con) {
        if (INSTANCE == null) {
            INSTANCE = new StartNotification(con);
        }
        return INSTANCE;
    }
    public static void  startServices(Context ctx) {
        con=ctx;
        StartNotification.getInstance(ctx).startNotificationReminder();
    }

    public static void  stopServices(Context ctx){
        StartNotification.getInstance(ctx).stopPeriodicNotifierService();
    }

    private boolean isreminderNotifyServiceRunning() {
        Log.e(TAG, "isPeriodicNotifyServiceRunning: called.");

        boolean isRunning = false;

        UUID periodicNotifyServiceID = getUUID(PREF_KEY_NOTIFIER_S);
        Log.e(TAG, "isPeriodicNotifyServiceRunning: getting service uuid from prefUUID=" + periodicNotifyServiceID.toString());

        WorkInfo workInfo = getWorkInfoForService(periodicNotifyServiceID);

        if (workInfo != null) {
            WorkInfo.State state = workInfo.getState();
            Log.e(TAG, "isPeriodicNotifyServiceRunning: workinfo state:" + state.name());

            if (state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED || state == WorkInfo.State.BLOCKED) {
                Log.e(TAG, "isPeriodicNotifyServiceRunning:state ==RUNNING || ENQUEUED || BLOCKED??,therefore isrunning=true");
                isRunning = true;
            }
        } else {
            Log.e(TAG, "isPeriodicNotifyServiceRunning: work info is null");
        }

        return isRunning;
    }

    @Nullable
    private WorkInfo getWorkInfoForService(UUID uuid) {
        Log.e(TAG, "getWorkInfoForService: called for uuid:" + uuid.toString());
        WorkInfo workInfo = null;
        try {
            workInfo = workManagerObj.getWorkInfoById(uuid).get();
            Log.e(TAG, "getWorkInfoForService: workinfo for this id=" + workInfo);

        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "getWorkInfoForService: Exception occurred!");
            e.printStackTrace();
        }
        return workInfo;
    }

    private UUID getUUID(String key) {
        //will return uuid of either a previous service running, or a garbage uuid(if preferences doesn't have a value
        String garbageID = UUID.randomUUID().toString();
        Log.e(TAG, "getUUID: garbageUUID string=" + garbageID);

        UUID generatedID = UUID.fromString(prefUUID.getString(key, garbageID));
        Log.e(TAG, "getUUID: genertedUUID string=" + generatedID.toString());

        return generatedID;
    }

    private void startNotificationReminder() {
        Log.e(TAG, "startNotificationReminder: called" );
        if (isreminderNotifyServiceRunning()) {
            Log.e(TAG, "startSingltonNotifierService: service already running, therefore returning");
            //return;
        } else {
            Log.e(TAG, "startSingltonNotifierService: building a new service and starting it");

            PeriodicWorkRequest NotifyServiceRequest = buildPeriodicNotifierServiceRequest();
            putUUID(PREF_KEY_NOTIFIER_S, NotifyServiceRequest.getId());
            workManagerObj.enqueue(NotifyServiceRequest);
        }

    }
    private PeriodicWorkRequest buildPeriodicNotifierServiceRequest() {
        int showNotifs =15;
        if(con!=null) {
            SharedPreferences prefMain = con.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            showNotifs = prefMain.getInt(PrefUtils.KEYS.NOTI_REMINDER_INTERVAL, Defaults.NOTI_REMINDER_INTERVAL);
        }

        Constraints.Builder constraintsBuilder = new Constraints.Builder()
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(false)
                .setRequiresStorageNotLow(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            constraintsBuilder.setRequiresDeviceIdle(false);
        }

        return new PeriodicWorkRequest
                .Builder(ReminderService.class, showNotifs, TimeUnit.MINUTES)
                .setConstraints(Constraints.NONE)
                //.setConstraints(constraintsBuilder.build())
                .addTag(TAG_REMINDER_NOTIFY_REQ)
                .build();
    }

    private void putUUID(String key, UUID uuidString) {
        //will put uuid in prefs
        Log.e(TAG, "putUUID: called for key" + key + "with uuid string" + uuidString);
        prefUUID.edit().putString(key, uuidString.toString()).apply();
    }

    private void  stopPeriodicNotifierService(){
        Log.e(TAG, "stopPeriodicNotifierService: called" );
        workManagerObj.cancelAllWorkByTag(TAG_REMINDER_NOTIFY_REQ);
        putUUID(PREF_KEY_NOTIFIER_S, UUID.randomUUID());
    }
}
