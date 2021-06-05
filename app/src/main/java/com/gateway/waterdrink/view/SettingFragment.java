package com.gateway.waterdrink.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gateway.waterdrink.BaseActivity;
import com.gateway.waterdrink.R;
import com.gateway.waterdrink.service.StartNotification;
import com.gateway.waterdrink.utils.LocaleHelper;
import com.gateway.waterdrink.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.gateway.waterdrink.utils.PrefUtils.PREF_NAME;


public class SettingFragment extends Fragment {
    private static final String TAG = "SettingFragment ::";
Spinner spinner,spinnerReminder;
List<String> list;
    List<Integer> reminderList;
    String currentLanguage;
    ArrayAdapter<String> adapter;
    ArrayAdapter<Integer> adapterReminder;
    @Nullable
    private SharedPreferences prefBasicInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinnerReminder= (Spinner) view.findViewById(R.id.reminder_spinner);

        list = new ArrayList<String>();
        list.add(getResources().getString(R.string.text_english));
        list.add(getResources().getString(R.string.text_hindi));
        list.add(getResources().getString(R.string.text_arabic));

        reminderList=new ArrayList<Integer>();
        reminderList.add(30);
        reminderList.add(45);
        reminderList.add(60);
        reminderList.add(90);


         adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        adapterReminder = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, reminderList);
        adapterReminder.setDropDownViewResource(R.layout.spinner_row);
        spinnerReminder.setAdapter(adapterReminder);

        prefBasicInfo = view.getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        setUiFromPreferences(prefBasicInfo);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                String language = prefBasicInfo.getString(PrefUtils.KEYS.LANGUAGE, PrefUtils.Defaults.LANGUAGE);
                Log.i(TAG,"selected language is  : "+adapter.getItem(position).toString());
                if(!language.equals(adapter.getItem(position).toString()))
                {
                    String lang;
                    if(position==0)
                    {
                        lang="en";
                    }else if(position==1)
                    {
                        lang="hi";
                    }else
                    {
                        lang="ar";
                    }
                    prefBasicInfo.edit().putString(PrefUtils.KEYS.LANGUAGE, lang).apply();
                    Log.i(TAG,"language change is :"+prefBasicInfo.getString(PrefUtils.KEYS.LANGUAGE,PrefUtils.Defaults.LANGUAGE));
                    //setUiFromPreferences(prefBasicInfo);
                    //LocaleHelper.setLocale(getActivity().getBaseContext(),getLocale(adapter.getItem(position).toString()))
                    if(!((getResources().getConfiguration().locale).toString()).equals(lang)) {
                        ((DailyDashbord)getActivity()).recreate();
                    }

                }






            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerReminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StartNotification.stopServices(getActivity().getBaseContext());
                prefBasicInfo.edit().putInt(PrefUtils.KEYS.NOTI_REMINDER_INTERVAL, adapterReminder.getItem(position)).apply();
                Log.i(TAG,"reminder change is :"+prefBasicInfo.getInt(PrefUtils.KEYS.NOTI_REMINDER_INTERVAL,PrefUtils.Defaults.NOTI_REMINDER_INTERVAL));
                StartNotification.startServices(getActivity().getBaseContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);



    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void setUiFromPreferences(@Nullable SharedPreferences pref) {

        final String language;
        final int reminderInterval;
        final boolean showAsImperial;

        if (pref == null) {
            language = PrefUtils.Defaults.LANGUAGE;
            reminderInterval=PrefUtils.Defaults.NOTI_REMINDER_INTERVAL;
        } else {
            language = pref.getString(PrefUtils.KEYS.LANGUAGE, PrefUtils.Defaults.LANGUAGE);
            reminderInterval=pref.getInt(PrefUtils.KEYS.NOTI_REMINDER_INTERVAL, PrefUtils.Defaults.NOTI_REMINDER_INTERVAL);
        }

        String lang;
        if(language.equals("en"))
        {
            lang=getResources().getString(R.string.text_english);
        }else if(language.equals("hi"))
        {
            lang=getResources().getString(R.string.text_hindi);
        }else
        {
            lang=getResources().getString(R.string.text_arabic);
        }
        spinner.setSelection(adapter.getPosition(lang));
        spinnerReminder.setSelection(adapter.getPosition(String.valueOf(reminderInterval)));
    }

    public String getLocale(String lang) {

        if (lang.equals(getResources().getString(R.string.text_hindi))) {
            lang = "hi";
        } else if (lang.equals(getResources().getString(R.string.text_arabic))) {
            lang = "ar";
        } else {
            lang = "en";
        }

        return lang;
    }

}
