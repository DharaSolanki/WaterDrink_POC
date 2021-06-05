package com.gateway.waterdrink.utils;

import com.gateway.waterdrink.R;

public class PrefUtils {

    public static final String PREF_NAME = "userdetail";
    public interface KEYS {
        String DATE_TODAY = "user_date_today";
        String WAKEUP_TIME = "user_time_wakeup";
        String SLEEP_TIME = "user_time_sleep";
        String GENDER = "user_gender";
        String WEIGHT = "user_weight";
        String SHOW_NOTIFS = "show_notif";
        String KEY_ACTIVITY = "user_activity";
        String KEY_SHOWN_INFO_ACTIVITY = "launched_info_activity_once";
        String LANGUAGE="language";
        String KEY_DAILY_TARGET = "user_daily_intake";
        String KEY_TODAY_INTAKE_ACHIEVED = "user_today_intake";
        String NOTI_REMINDER_INTERVAL = "noti_reminder_interval";


        String KEY_SHOW_LOGS ="user_start_daily_logger_service" ;
    }
    public interface Defaults {
        int WEIGHT = 40;
        int DAILY_TARGET = 2000;
        String GENDER = UserGender.MALE.name();
        String WAKEUP_TIME = "06:00";
        String SLEEP_TIME = "11:30";
        String LANGUAGE="en";

        int TODAY_INTAKE_ACHIEVED = 0;
        String DATE_TODAY = TimeUtils.getCurrentDate();
        int NOTI_REMINDER_INTERVAL = 30;
        boolean SHOW_NOTIF = false;
        boolean HAS_SHOWN_INTRO_INFO_ACTIVITY = false;
        boolean SHOW_LOGS =false;
        Boolean SHOW_IMPERIAL_MM = false;

        int QTY = 200;

    }

    public enum UserGender {MALE, FEMALE}
    public enum LANGUAGE {English, Hindi,Arabic}
}
