package com.gateway.waterdrink.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gateway.waterdrink.BaseActivity;
import com.gateway.waterdrink.R;
import com.gateway.waterdrink.utils.PrefUtils;
import com.gateway.waterdrink.utils.PrefUtils.KEYS;
import com.gateway.waterdrink.utils.PrefUtils.Defaults;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static com.gateway.waterdrink.utils.PrefUtils.PREF_NAME;

public class ChooseSleepTime extends BaseActivity {
    private static final String TAG = "ChooseSleepTime ::";
    String sleepHour,sleepMinute;
    Button btnNext;
    private SharedPreferences prefUserInfo;
    WheelView hoursWheel;
    WheelView minutesWheel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sleep_time);
        prefUserInfo = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        btnNext=(Button)findViewById(R.id.btn_next);
        List<String> hours;



            hours = createEuHours();



        final MediaPlayer mp = MediaPlayer.create(this, R.raw.scroll);

       hoursWheel = (WheelView) findViewById(R.id.hoursWheel);
        hoursWheel.setWheelAdapter(new ArrayWheelAdapter(this));
        hoursWheel.setSkin(WheelView.Skin.Holo);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.backgroundColor = Color.TRANSPARENT;
        style.selectedTextColor = Color.parseColor("#0016ff");
        style.holoBorderColor = Color.parseColor("#FF78E2FF");
        style.textColor = Color.parseColor("#FF78E2FF");
        style.textSize = 21;
        hoursWheel.setSelection(8);
        hoursWheel.setStyle(style);
        hoursWheel.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mp.start();
                sleepHour = o.toString();
            }
        });
        hoursWheel.setWheelData(hours);


        minutesWheel = (WheelView) findViewById(R.id.minutesWheel);
        minutesWheel.setWheelAdapter(new ArrayWheelAdapter(this));
        minutesWheel.setSkin(WheelView.Skin.Holo);
        WheelView.WheelViewStyle styleMetricsWheel = new WheelView.WheelViewStyle();
        styleMetricsWheel.backgroundColor = Color.TRANSPARENT;
        styleMetricsWheel.selectedTextColor = Color.parseColor("#0016ff");
        styleMetricsWheel.holoBorderColor = Color.parseColor("#FF78E2FF");
        styleMetricsWheel.textColor = Color.parseColor("#FF78E2FF");
        styleMetricsWheel.textSize = 21;
        minutesWheel.setStyle(style);
        minutesWheel.setWheelData(createMinutes());
        minutesWheel.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mp.start();
                sleepMinute = o.toString();

            }
        });
        setUiData(prefUserInfo);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"sleep storing is :"+sleepHour+":"+sleepMinute);
                putInfoInPref(KEYS.SLEEP_TIME, sleepHour+":"+sleepMinute);
                putInfoInPref(KEYS.KEY_SHOWN_INFO_ACTIVITY,true);
                Intent in = new Intent(ChooseSleepTime.this,WaterIntack.class);
                startActivity(in);

            }
        });
    }

    private void setUiData(SharedPreferences pref) {

        String sleep = PrefUtils.Defaults.SLEEP_TIME;
        Log.i(TAG,"weight default is :"+sleep);
        if (pref != null) {

            sleep = pref.getString(PrefUtils.KEYS.SLEEP_TIME, PrefUtils.Defaults.SLEEP_TIME);
            Log.i(TAG,"weight pref is :"+sleep);
        }

        String[] splitString = sleep.split(":");
        hoursWheel.setSelection(Integer.parseInt(splitString[0]));
        minutesWheel.setSelection(Integer.parseInt(splitString[1]));
    }

    private void putInfoInPref(String key, Object value) {
        if (prefUserInfo != null) {
            SharedPreferences.Editor et = prefUserInfo.edit();
            if (value instanceof String) {
            et.putString(key, (String) value);}
            else if (value instanceof Boolean) {
                et.putBoolean(key, (Boolean) value);}
            et.apply();
        }

    }
    private ArrayList<String> createEuHours() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add(i + "");
            }
        }
        return list;
    }

    private ArrayList<String> createUsaHours() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= 12; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add(i + "");
            }
        }
        return list;
    }

    private ArrayList<String> createMinutes() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add(i + "");
            }
        }
        return list;
    }

}
