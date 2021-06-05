package com.gateway.waterdrink;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.gateway.waterdrink.utils.LocaleHelper;
import com.gateway.waterdrink.utils.PrefUtils;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import static com.gateway.waterdrink.utils.PrefUtils.PREF_NAME;

public class BaseActivity extends AppCompatActivity {
    private Locale mCurrentLocale;
    SharedPreferences pref;

    @Override
    protected void attachBaseContext(Context newBase) {

       /* final Resources resources = getResources();
        final Configuration configuration = resources.getConfiguration();*/
        //  final Locale newLocale = getLocale(this);
        /*if (!configuration.locale.equals(locale)) {
            configuration.setLocale(locale);
            if (Build.VERSION.SDK_INT >= 17) {
                configuration.setLayoutDirection(locale);
            }
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            //resources.updateConfiguration(configuration, null);
        }*/

        // super.attachBaseContext(newBase);
        super.attachBaseContext(LocaleHelper.setLocale(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }


    @Override
    protected void onStart() {
        super.onStart();
       // LocaleHelper.setLocale(this,getLocale(this).toString());
        //mCurrentLocale = getResources().getConfiguration().locale;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       /* Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        Locale locale = getLocale(this);

        if (!locale.equals(mCurrentLocale)) {

            mCurrentLocale = locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                conf.setLayoutDirection(locale);// API 17+ only.
            }
            // recreate();
        }


        res.updateConfiguration(conf, dm);*/
    }

    public Locale getLocale() {
        String lang = pref.getString(PrefUtils.KEYS.LANGUAGE, PrefUtils.Defaults.LANGUAGE);

        if (lang.equals(getResources().getString(R.string.text_hindi))) {
            lang = "hi";
        } else if (lang.equals(getResources().getString(R.string.text_arabic))) {
            lang = "ar";
        } else {
            lang = "en";
        }

        return new Locale(lang);
    }
}
