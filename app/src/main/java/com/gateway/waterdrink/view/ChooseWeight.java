package com.gateway.waterdrink.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.gateway.waterdrink.BaseActivity;
import com.gateway.waterdrink.R;
import com.gateway.waterdrink.utils.PrefUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.gateway.waterdrink.utils.PrefUtils.PREF_NAME;

public class ChooseWeight extends BaseActivity {

    Button btnNext;
    private static final String TAG = "ChooseWeight ::";
    @Nullable
    private SharedPreferences prefUserInfo;
    NumberPicker numberPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_weight);
        prefUserInfo = getSharedPreferences(PREF_NAME,MODE_PRIVATE);

        numberPicker = new NumberPicker(ChooseWeight.this);
        numberPicker = findViewById(R.id.pickerKg);
        btnNext=(Button)findViewById(R.id.btn_next);

        if (numberPicker != null) {
            numberPicker.setMinValue(20);
            numberPicker.setMaxValue(150);
            numberPicker.setWrapSelectorWheel(true);
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    Log.i(TAG,"weight Selected is ::"+newVal);
                    putInfoInPref(PrefUtils.KEYS.WEIGHT, newVal);
                }
            });
        }
        setUiData(prefUserInfo);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ChooseWeight.this,ChooseWakeupTime.class);
                startActivity(in);

            }
        });

    }

    private void setUiData(SharedPreferences pref) {

        int weight = PrefUtils.Defaults.WEIGHT;
        Log.i(TAG,"weight default is :"+weight);
        if (pref != null) {

            weight = pref.getInt(PrefUtils.KEYS.WEIGHT, PrefUtils.Defaults.WEIGHT);
            Log.i(TAG,"weight pref is :"+weight);
        }
        numberPicker.setValue(weight);
    }

    private void putInfoInPref(String key, Object value) {
        if (prefUserInfo != null) {
            SharedPreferences.Editor et = prefUserInfo.edit();
            et.putInt(key, (Integer) value);
            et.apply();
        }

    }

}
