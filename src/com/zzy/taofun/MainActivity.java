
package com.zzy.taofun;

import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.api.TaobaoUtils;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
    
    private TopAndroidClient mTopAndClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTopAndClient=TopAndroidClient.getAndroidClientByAppKey("21451934");
    }
}
