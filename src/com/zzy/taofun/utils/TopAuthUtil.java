
package com.zzy.taofun.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.taobao.top.android.TOPUtils;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.auth.AccessToken;
import com.taobao.top.android.auth.AuthError;
import com.taobao.top.android.auth.AuthException;
import com.taobao.top.android.auth.AuthorizeListener;

import android.net.Uri;
import android.os.Bundle;

public class TopAuthUtil {

    public static boolean authChecker(Uri uri, AuthorizeListener listener) {
        boolean result = false;

        final TopAndroidClient client = TopAndroidClient.getAndroidClientByAppKey(Constants.APP_KEY);
        Uri u = Uri.parse(client.getRedirectURI());
        if (uri != null && uri.getScheme().equals(u.getScheme())
                && uri.getHost().equals(u.getHost())
                && uri.getPort() == u.getPort()
                && uri.getPath().equals(u.getPath())) {

            String errorStr = uri.getQueryParameter("error");
            if (errorStr == null) {// 授权成功
                // String ret = url.substring(url.indexOf("#") + 1);
                result = true;
                String ret = uri.getFragment();
                String[] kv = ret.split("&");
                Bundle values = new Bundle();
                for (String each : kv) {
                    String[] ss = each.split("=");
                    if (ss != null && ss.length == 2) {
                        values.putString(ss[0], ss[1]);
                    }
                }
                final AccessToken token = TOPUtils.convertToAccessToken(values);
                // Android3.0后ui主线程中同步访问网络会有限制。
                // 使用ExecutorService.invokeAll()阻塞主线程的方式起一个线程再去调用api
                Callable<Date> task = new Callable<Date>() {
                    @Override
                    public Date call() throws Exception {
                        Date date = client.getTime();
                        return date;
                    }
                };
                List<Callable<Date>> tasks = new ArrayList<Callable<Date>>();
                tasks.add(task);
                ExecutorService es = Executors.newSingleThreadExecutor();
                try {
                    List<Future<Date>> results = es.invokeAll(tasks);
                    Future<Date> future = results.get(0);
                    token.setStartDate(future.get());

                    client.addAccessToken(token);
                } catch (Exception e) {
                    listener.onAuthException(new AuthException(e));
                }
                listener.onComplete(token);
            } else {// 授权失败
                String errorDes = uri.getQueryParameter("error_description");
                AuthError error = new AuthError();
                error.setError(errorStr);
                error.setErrorDescription(errorDes);
                listener.onError(error);
            }

        }
        return result;
    }

    public static boolean authChecker(String url, AuthorizeListener listener) {
        return authChecker(Uri.parse(url), listener);
    }
}
