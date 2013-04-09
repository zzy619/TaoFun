
package com.zzy.taofun;

import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.auth.AccessToken;
import com.taobao.top.android.auth.AuthError;
import com.taobao.top.android.auth.AuthException;
import com.taobao.top.android.auth.AuthorizeListener;
import com.zzy.taofun.manager.ConfigMgr;
import com.zzy.taofun.utils.Constants;
import com.zzy.taofun.widget.AuthDialog;

public class MainActivity extends Activity {

    private TopAndroidClient mTopAndClient;
    private TextView mTextView;
    private Long mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTopAndClient = TopAndroidClient.getAndroidClientByAppKey(Constants.APP_KEY);
        mTextView = (TextView) findViewById(R.id.text);
        mUserId = ConfigMgr.getAuthUserID(this);

        if (mUserId == 0) {

            new AuthDialog(this).showDialog(mTopAndClient.getAuthorizeUrl(),
                    new AuthorizeListener() {

                        @Override
                        public void onError(AuthError e) {
                        }

                        @Override
                        public void onComplete(AccessToken accessToken) {
                            setText(accessToken);
                        }

                        @Override
                        public void onAuthException(AuthException e) {
                        }
                    });
        } else {
            setText(mTopAndClient.getAccessToken(mUserId));
        }
    }

    private void setText(AccessToken accessToken) {
        Map<String, String> map = accessToken.getAdditionalInformation();
        Set<String> keySet = map.keySet();
        String retult = "";
        for (String key : keySet) {
            retult += key + " : " + map.get(key) + "\n";
        }
        mTextView.setText(retult);
    }
}
