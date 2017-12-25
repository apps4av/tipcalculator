package org.apps4av.tipcalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Preferences for main activity
 */
public class Preferences {


    /**
     * Preferences
     */
    private SharedPreferences mPref;
    private Context mContext;

    /**
     * @param ctx
     */
    public Preferences(Context ctx) {
        /*
         * Load preferences.
         */
        mContext = ctx;
        /*
         * Set default prefs.
         */
        mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
    }


    /**
     *
     * @return
     */
    public String getMinimumTip() {
        return mPref.getString(mContext.getString(R.string.minimum), "15");
    }


    /**
     * @return
     */
    public void setMinimumTip(String val) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(mContext.getString(R.string.minimum), val);
        editor.commit();
    }

    /**
     *
     * @return
     */
    public String getUsualTip() {
        return mPref.getString(mContext.getString(R.string.usual), "18");
    }


    /**
     * @return
     */
    public void setUsualTip(String val) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(mContext.getString(R.string.usual), val);
        editor.commit();
    }

    /**
     *
     * @return
     */
    public String getHighTip() {
        return mPref.getString(mContext.getString(R.string.high), "21");
    }


    /**
     * @return
     */
    public void setHighTip(String val) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(mContext.getString(R.string.high), val);
        editor.commit();
    }

}


