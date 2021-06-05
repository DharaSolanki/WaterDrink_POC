package com.gateway.waterdrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.gateway.waterdrink.service.StartNotification;
import com.gateway.waterdrink.utils.PrefUtils;
import com.gateway.waterdrink.view.ChooseGenderActivity;
import com.gateway.waterdrink.view.DailyDashbord;

import static com.gateway.waterdrink.utils.PrefUtils.PREF_NAME;
import static com.gateway.waterdrink.utils.PrefUtils.KEYS;
import static com.gateway.waterdrink.utils.PrefUtils.Defaults;
public class SplashscreenActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        findViewById(R.id.ll_splash).setVisibility(View.VISIBLE);
        new Thread(() -> {
            SystemClock.sleep(1000);
            runOnUiThread(() -> {
                Animation anim = AnimationUtils.loadAnimation(SplashscreenActivity.this, R.anim.slide_out);
                findViewById(R.id.ll_splash).startAnimation(anim);
                findViewById(R.id.ll_splash).setVisibility(View.GONE);

                StartNotification.startServices(this);
                SharedPreferences pref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                boolean shownOneTime=pref.getBoolean(PrefUtils.KEYS.KEY_SHOWN_INFO_ACTIVITY, Defaults.HAS_SHOWN_INTRO_INFO_ACTIVITY);
                //noinspection rawtypes
                Class classToBeLaunched = shownOneTime ? DailyDashbord.class : ChooseGenderActivity.class;
                startActivity(new Intent(SplashscreenActivity.this, classToBeLaunched));
                finish();

            });
        }).start();

    }

    private void handleSplash() {


    }
}
