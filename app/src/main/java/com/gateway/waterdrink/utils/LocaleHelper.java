package com.gateway.waterdrink.utils;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.gateway.waterdrink.R;

import java.util.Locale;

import androidx.appcompat.view.ContextThemeWrapper;

import static android.content.Context.MODE_PRIVATE;
import static com.gateway.waterdrink.utils.PrefUtils.PREF_NAME;

public class LocaleHelper {
    static SharedPreferences pref;
    private static final String TAG = "LocaleHelper ::";
    public static Context setLocale(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String language= getLocale(context).toString();
        // updating the language for devices above android nougat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }
        // for devices having lower version of android os
        return updateResourcesLegacy(context, language);
    }
    public static Context setLocale(Context context, String language) {

        // updating the language for devices above android nougat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }
        // for devices having lower version of android os
        return updateResourcesLegacy(context, language);
    }


    // the method is used update the language of application by creating
    // object of inbuilt Locale class and passing language argument to it
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Log.i(TAG,"language updateResources is  ::: "+language);
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context.createConfigurationContext(configuration);
    }


    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Log.i(TAG,"language updateResourcesLegacy is  ::: "+language);
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }
    public static  Locale getLocale(Context con) {

        String lang = pref.getString(PrefUtils.KEYS.LANGUAGE, PrefUtils.Defaults.LANGUAGE);

        Log.i(TAG, "getlocal new lang :"+lang);
        return new Locale(lang);
    }
}
