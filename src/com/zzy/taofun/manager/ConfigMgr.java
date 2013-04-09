
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

    public static Long getAuthUserID(Context ctx) {
        SharedPreferences prefs = initSharedPreferences(ctx);
        return prefs.getLong(USER_ID,0L);
    }

    public static void setAuthUserID(Context ctx, Long userId) {
        SharedPreferences prefs = initSharedPreferences(ctx);
        SharedPreferences.Editor spe = prefs.edit();
        spe.putLong(USER_ID, userId);
        spe.commit();
    }

}
