package com.gateway.waterdrink.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import com.gateway.waterdrink.BaseActivity;
import com.gateway.waterdrink.R;
import com.gateway.waterdrink.SplashscreenActivity;
import com.gateway.waterdrink.utils.PrefUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.gateway.waterdrink.utils.PrefUtils.PREF_NAME;

public class ChooseGenderActivity extends BaseActivity {
ImageButton ibtnMale,ibtnFemale;
Button btnNext;
    @Nullable
    private SharedPreferences prefUserInfo;

    @Nullable
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        ibtnMale = (ImageButton)findViewById(R.id.img_btn_male);
        ibtnFemale =(ImageButton)findViewById(R.id.img_btn_female);
        btnNext=(Button)findViewById(R.id.btn_next);
        ibtnMale.setSelected(false);
        ibtnFemale.setSelected(false);


        prefUserInfo = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        setUiData(prefUserInfo);

       /* SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(sharedPreferences)){
                    setUiData(prefUserInfo);
                }
            }
        };*/
        //prefListener = (sharedPreferences, s) -> setUiData(sharedPreferences);


        ibtnMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnFemale.setSelected(false);
                ibtnMale.setSelected(true);
                putInfoInPref(PrefUtils.KEYS.GENDER, PrefUtils.UserGender.MALE);
            }
        });

        ibtnFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnFemale.setSelected(true);
                ibtnMale.setSelected(false);
                putInfoInPref(PrefUtils.KEYS.GENDER, PrefUtils.UserGender.FEMALE);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ChooseGenderActivity.this,ChooseWeight.class);
                startActivity(in);

            }
        });
    }


    private void setUiData(SharedPreferences pref) {

        String genderEnum = PrefUtils.Defaults.GENDER;
        if (pref != null) {

            genderEnum = pref.getString(PrefUtils.KEYS.GENDER, PrefUtils.Defaults.GENDER);
        }
        if (genderEnum.equals(PrefUtils.UserGender.FEMALE.name())) {//1 set gender and show ui chang
            ibtnFemale.setSelected(true);
            ibtnMale.setSelected(false);
        } else {
            ibtnFemale.setSelected(false);
            ibtnMale.setSelected(true);


        }
    }

    private void putInfoInPref(String key, Object value) {
        if (prefUserInfo != null) {
            SharedPreferences.Editor et = prefUserInfo.edit();
                et.putString(key, ((PrefUtils.UserGender) value).name());

            et.apply();
        }

    }


}
