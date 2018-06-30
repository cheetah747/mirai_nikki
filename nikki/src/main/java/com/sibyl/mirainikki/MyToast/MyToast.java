package com.sibyl.mirainikki.MyToast;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MyToast {
    public static Toast mToast;

    public static void show(Context context, String showText, int showTime) {
        WeakReference<Context> ref = new WeakReference<Context>(context);
        if (ref != null) {
            ((Activity) ref.get()).runOnUiThread(new ToastOnUiThreadRunnable(showText, showTime));
        }
    }

}
