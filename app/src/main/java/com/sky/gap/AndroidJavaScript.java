package com.sky.gap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * JS的调用的方法
 *
 * @author yung
 *         <p/>
 *         2014年6月24日 09:26:14
 *         <p/>
 *         此类中的打开的QQ 和微信是直接通过包名和类名调用虽然QQ微信包名不容易变 但是主界面好事可能会变
 *         如果发现打不开QQ微信应用可以查看是否是QQ微信升级更改了类名
 */
public class AndroidJavaScript {

    Context c;

    public AndroidJavaScript(Context c) {
        this.c = c;
    }


    // 获取在webview上获取js生成的html的源码
    @JavascriptInterface
    public void PhoneFtn(String phone) {
        Log.i("main", phone);
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        c.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone)));
    }
}