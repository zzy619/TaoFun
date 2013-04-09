
package com.zzy.taofun.widget;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.taobao.top.android.TOPUtils;
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
    private ProgressDialog mSpinner;

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
        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");
        mSpinner.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                AuthDialog.this.dismiss();
                return false;
            }

        });
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(Constants.CALLBACK_URL)) {
                TopAuthUtil.authChecker(url, AuthDialog.this);
                view.stopLoading();
                AuthDialog.this.dismiss();
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            AuthDialog.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mSpinner.isShowing()) {
                mSpinner.dismiss();
            }
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

    }

    public void showDialog(String url, AuthorizeListener listener) {
        mAuthListener = listener;
        this.show();
        mWebView.loadUrl(url);
    }

    @Override
    public void onComplete(AccessToken accessToken) {
        ConfigMgr.setAuthUserID(mContext, TOPUtils.getUserIdFromAccessToken(accessToken));
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

    @Override
    public void dismiss() {
        mSpinner.dismiss();
        if (null != mWebView) {
            mWebView.stopLoading();
            mWebView.destroy();
        }
        super.dismiss();
    }

}
