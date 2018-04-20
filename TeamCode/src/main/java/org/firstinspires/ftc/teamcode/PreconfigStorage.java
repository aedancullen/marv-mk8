package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by aedan on 4/19/18.
 */

public class PreconfigStorage {

    SharedPreferences sharedPref;

    public PreconfigStorage(Context appContext) {
        sharedPref = ((Activity)appContext).getPreferences(Context.MODE_PRIVATE);
    }

    public void writeInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int readInt(String key) {
        return sharedPref.getInt(key, 0);
    }

}
