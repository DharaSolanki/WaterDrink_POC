package com.gateway.waterdrink.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gateway.waterdrink.BaseActivity;
import com.gateway.waterdrink.R;
import com.gateway.waterdrink.utils.PrefUtils;

import androidx.appcompat.app.AppCompatActivity;

import static com.gateway.waterdrink.utils.PrefUtils.PREF_NAME;

public class WaterIntack extends BaseActivity {
    private SharedPreferences prefUserInfo;
    TextView txtCapacity;
    Button btnStart;
    int capacity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_intack);
        prefUserInfo = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        txtCapacity=(TextView)findViewById(R.id.text_daily_intact);
        btnStart=(Button)findViewById(R.id.btn_start);
        capacity = calculateWaterCapacity();
        txtCapacity.setText(String.valueOf(capacity)+" "+getString(R.string.text_ml));

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putInfoInPref(PrefUtils.KEYS.KEY_DAILY_TARGET, capacity);
                Intent in = new Intent(WaterIntack.this,DailyDashbord.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);

            }
        });

    }


    private void putInfoInPref(String key, Object value) {
        if (prefUserInfo != null) {
            SharedPreferences.Editor et = prefUserInfo.edit();
            et.putInt(key, (Integer) value);
            et.apply();
        }

    }
    private int calculateWaterCapacity() {
        int weight=PrefUtils.Defaults.WEIGHT;
        if (prefUserInfo != null) {
            weight = prefUserInfo.getInt(PrefUtils.KEYS.WEIGHT,PrefUtils.Defaults.WEIGHT);
        }
        double result= weight*33;
        return (int)result;

    }
}
