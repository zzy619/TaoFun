
package com.zzy.taofun;

import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Item;
import com.taobao.api.response.ItemGetResponse;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.api.TopTqlListener;
import com.taobao.top.android.auth.AccessToken;
import com.taobao.top.android.auth.AuthError;
import com.taobao.top.android.auth.AuthException;
import com.taobao.top.android.auth.AuthorizeListener;
import com.zzy.taofun.manager.ConfigMgr;
import com.zzy.taofun.parse.LocalObjectJsonParser;
import com.zzy.taofun.utils.Constants;
import com.zzy.taofun.widget.AuthDialog;


public class MainActivity extends Activity implements OnClickListener {

    private TopAndroidClient mTopAndClient;
    private TextView mTextView;
    private Long mUserId;
    private Button mSelectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTopAndClient = TopAndroidClient.getAndroidClientByAppKey(Constants.APP_KEY);
        mTextView = (TextView) findViewById(R.id.text);
        mSelectBtn = (Button) findViewById(R.id.select);
        mSelectBtn.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        Log.e("ql", "onClick");
        // String ql =
        // "select user_id,nick,sex from user.buyer where nick=zhou405680603";
        String ql="{select num,price,type,stuff_status,title from item where num_iid=15435709307}";
        //ql+="{select num,price,type,stuff_status,title from item where num_iid=17546088521}";
        if (mUserId != 0) {
            Log.e("ql", ql);
            mTopAndClient.tql(ql, mUserId, new TopTqlListener() {

                @Override
                public void onException(Exception e) {

                }

                @Override
                public void onComplete(String result) {
                    Log.e("ql", result);
                    LocalObjectJsonParser parser = new LocalObjectJsonParser<ItemGetResponse>(
                            ItemGetResponse.class);
                    try {
                        ItemGetResponse userRespond = (ItemGetResponse) parser
                                .parse(result);
                        Item item = userRespond.getItem();
                        Message msg=new Message();
                        msg.obj=item;
                        mHandler.sendMessage(msg);
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            }, true);
        }
    }

    Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            Item item=(Item) msg.obj;
            mTextView.setText(item.getTitle()+"\n"+item.getPrice());
        };
    };
}
