package com.unsullied.chottabheem.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;

import static android.os.Build.VERSION_CODES.N;

public class LocaleManager {

    public static final  String LANGUAGE_ENGLISH   = "en";
    public static final  String LANGUAGE_TAMIL = "ta";
    private static final String LANGUAGE_KEY       = "language_key";

    private final SharedPreferences prefs;

    public LocaleManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Utility.isAtLeastVersion(N) ? config.getLocales().get(0) : config.locale;
    }

    public Context setLocale(Context c) {
        return updateResources(c, getLanguage());
    }

    public Context setNewLocale(Context c, String language) {
        Log.w("Success","setNewLocale:::"+language);
        persistLanguage(language);
        return updateResources(c, language);
    }

    public String getLanguage() {
        return prefs.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH);
    }

    @SuppressLint("ApplySharedPref")
    private void persistLanguage(String language) {
        Log.w("Success","presistLanguage:::"+language);
        // use commit() instead of apply(), because sometimes we kill the application process immediately
        // which will prevent apply() to finish
        prefs.edit().putString(LANGUAGE_KEY, language).commit();
    }

    private Context updateResources(Context context, String language) {
        Log.w("Success","updateResources:::"+language);
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
      /*  if (Utility.isAtLeastVersion(JELLY_BEAN_MR1)) {
            Log.w("Success","Comes updateResources if");
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {*/
            Log.w("Success","Comes updateResources else");
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        //}
        return context;
    }
}