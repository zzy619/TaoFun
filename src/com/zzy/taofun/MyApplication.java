
package com.zzy.taofun;

import com.taobao.top.android.TopAndroidClient;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TopAndroidClient.registerAndroidClient(getApplicationContext(), "21451934",
                "60cc6028d20d92a55445d683c619e622", "com.zzy.taofun://www.baidu.com");
    }

}
