package com.gateway.waterdrink.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gateway.waterdrink.R;
import com.gateway.waterdrink.db.TodayEntry;
import com.gateway.waterdrink.db.WaterRepo;
import com.gateway.waterdrink.utils.AndroidBasedUtils;
import com.gateway.waterdrink.utils.PrefUtils;
import com.gateway.waterdrink.utils.TimeUtils;
import com.github.guilhe.views.CircularProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;
import static com.gateway.waterdrink.utils.PrefUtils.PREF_NAME;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment ::";
    @Nullable
    View fragRootView;
    @Nullable
    RecyclerView rvButtons;

    TodayEntriesAdapter adpEntries;

    @Nullable
    private WaterRepo repo;

    @Nullable
    private SharedPreferences prefBasicInfo;

    /// -----------</global>-----<lifecycle funcs>---------------------------

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View fragView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragView, savedInstanceState);
        fragRootView = fragView;
        adpEntries = new TodayEntriesAdapter();
        //initUI
        TextView txtempty=(TextView)fragView.findViewById(R.id.empty_view);
        ImageButton ibtnAddIntake=(ImageButton)fragView.findViewById(R.id.ibtn_add_intake);
        ibtnAddIntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQtyButtonClick(200);
            }
        });

        RecyclerView rvTodayEntries = fragView.findViewById(R.id.rv_today_entries);
        if (rvTodayEntries != null) {
            adpEntries.setEntryList(new ArrayList<TodayEntry>());


            rvTodayEntries.setLayoutManager(new LinearLayoutManager(fragView.getContext()));
            rvTodayEntries.setAdapter(adpEntries);
            if(adpEntries.getItemCount()==0)
            {
                rvTodayEntries.setVisibility(View.GONE);
                txtempty.setVisibility(View.VISIBLE);
            }
        }

        //init preferences
        prefBasicInfo = fragView.getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        setUiFromPreferences(prefBasicInfo);
        AndroidBasedUtils.makeDateChanges(prefBasicInfo, repo);

        //setRepoAndLiveData
        repo = WaterRepo.getRepoInstance(fragView.getContext().getApplicationContext());
        LiveData<List<TodayEntry>> liveEntries = repo.getAllTodayEntriesObservable();
        if (liveEntries != null) {
            liveEntries.observe( this, new Observer<List<TodayEntry>>() {
                @Override
                public void onChanged(List<TodayEntry> todayEntries) {
                    adpEntries.setEntryList(todayEntries);
                    if(adpEntries.getItemCount()!=0)
                    {
                        rvTodayEntries.setVisibility(View.VISIBLE);
                        txtempty.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (prefBasicInfo == null || repo == null) return;

        setUiFromPreferences(prefBasicInfo);

        AndroidBasedUtils.makeDateChanges(prefBasicInfo, repo);

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    //----------------</lifecycle funcs>----<funcs from implemented interfaces>---------------------------



    public void onQtyButtonClick(int qty) {
        if (repo == null || prefBasicInfo == null) return;

        String time = TimeUtils.getCurrentTime();

        int origQty = prefBasicInfo.getInt(PrefUtils.KEYS.KEY_TODAY_INTAKE_ACHIEVED, PrefUtils.Defaults.TODAY_INTAKE_ACHIEVED);

        TodayEntry entry = new TodayEntry();
        entry.setTime(time);
        entry.setAmountInMilliLitres(qty);

        //---
        repo.addNewTodayEntry(entry);
        Log.i(TAG,"achived intack old is :"+origQty+" new is : "+(origQty+qty));
        prefBasicInfo.edit().putInt(PrefUtils.KEYS.KEY_TODAY_INTAKE_ACHIEVED, origQty + qty).apply();

        AndroidBasedUtils.makeDateChanges(prefBasicInfo, repo);

        setUiFromPreferences(prefBasicInfo);

    }


    //----------------</funcs from implemented interfaces>-------<other funcs>---------------------------


    private void setUiFromPreferences(@Nullable SharedPreferences pref) {
        setProgressBar(pref);
        setTargetAndAchievedAmount(pref);
    }


    private void setTargetAndAchievedAmount(@Nullable SharedPreferences pref) {
        if (fragRootView == null) return;

        final int target;
        final int achieved;

        if (pref == null) {
            target = PrefUtils.Defaults.DAILY_TARGET;
            achieved = PrefUtils.Defaults.TODAY_INTAKE_ACHIEVED;
        } else {
            target = pref.getInt(PrefUtils.KEYS.KEY_DAILY_TARGET, PrefUtils.Defaults.DAILY_TARGET);
            achieved = pref.getInt(PrefUtils.KEYS.KEY_TODAY_INTAKE_ACHIEVED, PrefUtils.Defaults.TODAY_INTAKE_ACHIEVED);
        }

        if (achieved >= target) {
            ImageView ivProgressViewImage = fragRootView.findViewById(R.id.iv_progress_centre);
            ivProgressViewImage.setVisibility(View.VISIBLE);
        }
        TextView tvAchieved = fragRootView.findViewById(R.id.fdv_tv_achieved);
        TextView tvTarget = fragRootView.findViewById(R.id.fdv_tv_target);

        tvTarget.setText(String.valueOf(target));
        tvAchieved.setText(String.valueOf(achieved));


    }

    private void setProgressBar(@Nullable SharedPreferences pref) {
        if (fragRootView == null) return;

        int target;
        int achieved;
        final boolean showAsImperial;

        if (pref == null) {
            target = PrefUtils.Defaults.DAILY_TARGET;
            achieved = PrefUtils.Defaults.TODAY_INTAKE_ACHIEVED;
            showAsImperial = PrefUtils.Defaults.SHOW_IMPERIAL_MM;
        } else {
            target = pref.getInt(PrefUtils.KEYS.KEY_DAILY_TARGET, PrefUtils.Defaults.DAILY_TARGET);
            achieved = pref.getInt(PrefUtils.KEYS.KEY_TODAY_INTAKE_ACHIEVED, PrefUtils.Defaults.TODAY_INTAKE_ACHIEVED);
           // showAsImperial = pref.getBoolean(PrefUtils.KEYS.KEY_SHOW_IMPERIAL_MM, PrefUtils.Defaults.SHOW_IMPERIAL_MM);
        }

       /* if (showAsImperial) {
            achieved = JVMBasedUtils.convertToFluidOunces(achieved);
            target = JVMBasedUtils.convertToFluidOunces(target);
        }*/

        CircularProgressView progressView = fragRootView.findViewById(R.id.progress_daily_intake);

        //setInitialData();
        progressView.setProgress(achieved);
        progressView.setMax(target);

    }
}
