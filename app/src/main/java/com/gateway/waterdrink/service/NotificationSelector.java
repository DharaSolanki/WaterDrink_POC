package com.gateway.waterdrink.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.gateway.waterdrink.BaseActivity;
import com.gateway.waterdrink.db.TodayEntry;
import com.gateway.waterdrink.db.WaterRepo;
import com.gateway.waterdrink.utils.AndroidBasedUtils;
import com.gateway.waterdrink.utils.NotificationUtils;
import com.gateway.waterdrink.utils.PrefUtils;
import com.gateway.waterdrink.utils.PrefUtils.KEYS;
import com.gateway.waterdrink.utils.PrefUtils.Defaults;
import com.gateway.waterdrink.utils.TimeUtils;
import com.gateway.waterdrink.view.DailyDashbord;
import com.gateway.waterdrink.view.WaterIntack;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gateway.waterdrink.R;

import static com.gateway.waterdrink.utils.PrefUtils.PREF_NAME;

public class NotificationSelector extends BaseActivity {
    private static final String TAG = "NotificationSelector ::";
    private SharedPreferences prefUserInfo;
    TextView txtCapacity;
    Button btnAdd,btnCancel;
    int capacity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_selector);
        this.setFinishOnTouchOutside(false);
        btnAdd=(Button)findViewById(R.id.btn_Add);
        btnCancel=(Button)findViewById(R.id.btn_cancel);
        prefUserInfo = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        txtCapacity=(TextView)findViewById(R.id.text_daily_intact);
        capacity = calculateWaterCapacity();
        Log.i(TAG,"capacity is : "+String.valueOf(capacity));
        txtCapacity.setText(String.valueOf(capacity));


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doActionsForUserDrankWater(getApplicationContext());

                finish();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }
    private void showContinueDialog(Context ctx) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);

        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.activity_notification_selector, null, false);



        dialogBuilder
                .setView(v)
                .setPositiveButton("Add 200ml", (dialogInterface, i) -> {

                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                })
        ;

        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }

    private int calculateWaterCapacity() {
        int weight=PrefUtils.Defaults.WEIGHT;
        if (prefUserInfo != null) {
            weight = prefUserInfo.getInt(PrefUtils.KEYS.WEIGHT,PrefUtils.Defaults.WEIGHT);
        }
        double result= weight*33;
        return (int)result;

    }

    private void doActionsForUserDrankWater(Context context) {
        Log.i(TAG,"inside doActionsForUserDrankWater");
        SharedPreferences prefMain = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        WaterRepo repo = WaterRepo.getRepoInstance(context.getApplicationContext());

        String time = TimeUtils.getCurrentTime();
        TodayEntry entry = new TodayEntry();
        entry.setTime(time);
        entry.setAmountInMilliLitres(200);

        int achieved = prefMain.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
        prefMain.edit().putInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, achieved + 200).apply();
        repo.addNewTodayEntry(entry);
        AndroidBasedUtils.makeDateChanges(prefMain, repo);
        NotificationUtils.cancelAll(context.getApplicationContext());

    }
}
