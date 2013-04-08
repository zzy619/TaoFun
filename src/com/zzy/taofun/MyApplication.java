
package com.zzy.taofun;

import com.taobao.top.android.TopAndroidClient;
import com.zzy.taofun.utils.Constants;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TopAndroidClient.registerAndroidClient(getApplicationContext(), Constants.APP_KEY,
                Constants.APP_SECRET, Constants.CALLBACK_URL);
    }

}
