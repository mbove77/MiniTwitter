package com.bove.martin.minitwitter.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Martín Bove on 22-Jul-20.
 * E-mail: mbove77@gmail.com
 */
public class SharedPreferecesManager {

    private SharedPreferecesManager() {

    }

    private static SharedPreferences getSharedPreferes() {
        return MyApp.getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void setStringValue(String label, String valor) {
       SharedPreferences.Editor editor = getSharedPreferes().edit();
       editor.putString(label, valor);
       editor.commit();
    }

    public static void setBooleanValue(String label, Boolean valor) {
        SharedPreferences.Editor editor = getSharedPreferes().edit();
        editor.putBoolean(label, valor);
        editor.commit();
    }

    public static String getStringValue(String label) {
        return getSharedPreferes().getString(label, null);
    }

    public static Boolean getBooleanValue(String label) {
        return getSharedPreferes().getBoolean(label, false);
    }
}
