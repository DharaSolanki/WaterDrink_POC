package com.gateway.waterdrink.view;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.gateway.waterdrink.BaseActivity;
import com.gateway.waterdrink.R;
import com.gateway.waterdrink.service.ReminderService;
import com.gateway.waterdrink.utils.PrefUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.viewpager2.widget.ViewPager2;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.github.guilhe.views.CircularProgressView;

import java.util.concurrent.TimeUnit;

public class DailyDashbord extends BaseActivity {
    TabLayout tbTabs;
    ViewPager2 viewPager2;


    SharedPreferences prefMain;
    private static final String TAG = "DailyDashbord ::";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_dashbord);
        tbTabs = findViewById(R.id.tl_dashboard);
        viewPager2 = findViewById(R.id.vp2_dashboard);
        prefMain = getSharedPreferences(PrefUtils.PREF_NAME, MODE_PRIVATE);

        loadFragments();
    }




    private void loadFragments() {
        DashboardFragmentsAdapter adpPager = new DashboardFragmentsAdapter(this);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setAdapter(adpPager);
        viewPager2.setUserInputEnabled(false);

        adpPager.addFragment(new HomeFragment());
        adpPager.addFragment(new SettingFragment());

        viewPager2.setCurrentItem(0);


        new TabLayoutMediator(tbTabs, viewPager2, (tab, position) -> {
            tab.setText("");
            int iconRes = android.R.drawable.btn_star;
            String title=getResources().getString(R.string.text_home);
            switch (position) {
                case 0: {
                   // iconRes = R.drawable.ic_200;
                    title=this.getResources().getString(R.string.text_home);
                    break;
                }
                case 1: {
                    //iconRes = R.drawable.ic_200;
                    title=this.getResources().getString(R.string.text_setting);
                    break;
                }
            }
            //tab.setIcon(iconRes);
            tab.setText(title);


        }).attach();

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        Log.i(TAG,"onConfigurationChanged called :::");
        super.onConfigurationChanged(newConfig);
    }


}
