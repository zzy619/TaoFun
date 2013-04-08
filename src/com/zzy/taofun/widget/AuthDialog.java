
package com.zzy.taofun.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.taobao.top.android.auth.AccessToken;
import com.taobao.top.android.auth.AuthError;
import com.taobao.top.android.auth.AuthException;
import com.taobao.top.android.auth.AuthorizeListener;
import com.zzy.taofun.R;
import com.zzy.taofun.manager.ConfigMgr;
import com.zzy.taofun.utils.Constants;
import com.zzy.taofun.utils.TopAuthUtil;

public class AuthDialog extends Dialog implements AuthorizeListener {

    private WebView mWebView;
    private AuthorizeListener mAuthListener;
    private Context mContext;

    public AuthDialog(Context context) {
        super(context, R.style.MyTheme_CustomDialog);
        setContentView(R.layout.auth_webview);
        mContext = context;
        initView();
    }

    public void initView() {
        mWebView = (WebView) findViewById(R.id.auth_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(Constants.CALLBACK_URL)) {
                TopAuthUtil.authChecker(url, AuthDialog.this);
                AuthDialog.this.dismiss();
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

    }

    public void showDialog(String url, AuthorizeListener listener) {
        mAuthListener = listener;
        this.show();
        mWebView.loadUrl(url);
    }

    @Override
    public void onComplete(AccessToken accessToken) {
        ConfigMgr.setAuthUserID(mContext,
                accessToken.getAdditionalInformation().get(AccessToken.KEY_TAOBAO_USER_ID));
        if (mAuthListener != null) {
            mAuthListener.onComplete(accessToken);
        }
    }

    @Override
    public void onError(AuthError e) {

    }

    @Override
    public void onAuthException(AuthException e) {
    }
}
