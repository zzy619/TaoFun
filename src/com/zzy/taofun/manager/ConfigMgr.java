
package com.zzy.taofun.manager;

import com.zzy.taofun.utils.Constants;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigMgr {

    private static SharedPreferences sPrefs = null;

    public static final String USER_ID = "user_id";

    private static SharedPreferences initSharedPreferences(Context ctx) {
        if (sPrefs == null) {
            sPrefs = ctx.getSharedPreferences(Constants.SHAREPERFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return sPrefs;
    }

    public static boolean getAuthUserID(Context ctx) {
        SharedPreferences prefs = initSharedPreferences(ctx);
        return prefs.getBoolean(USER_ID, false);
    }

    public static void setAuthUserID(Context ctx, String userId) {
        SharedPreferences prefs = initSharedPreferences(ctx);
        SharedPreferences.Editor spe = prefs.edit();
        spe.putString(USER_ID, userId);
        spe.commit();
    }

}
