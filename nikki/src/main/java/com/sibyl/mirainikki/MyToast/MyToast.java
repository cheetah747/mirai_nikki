package com.sibyl.mirainikki.MyToast;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.sibyl.mirainikki.MyApplication.MyApplication;

import java.lang.ref.WeakReference;

public class MyToast {

    public static Toast toast;
    public static Snackbar sb;
    public static int MY_DURATION = Toast.LENGTH_LONG;

    public static void show(String text) {
        if (null != toast) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(MyApplication.getInstance(), text, MY_DURATION);
        toast.show();
    }

    public static void show(Context context, String text, int length) {
        if (null != toast) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(MyApplication.getInstance(), text, length);
        toast.show();
    }

    public static void showToast(Context context, String text, int gravity) {
        if (null != toast) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    public static void showSnack(Activity activity, String text) {
        WeakReference<View> ref = new WeakReference<View>(activity.getWindow().getDecorView().findViewById(android.R.id.content));
        if (null != sb) {
            sb.dismiss();
            sb = null;
        }
        if (ref.get() != null) {
            sb = Snackbar.make(ref.get(), text, Snackbar.LENGTH_LONG);
            sb.show();
        }
    }


    private TimeCounter counter;
    private Toast mToast;
    private String showText;

    /**
     * 专门写一套可以自定义duration时长的非静态版本Toast
     * 食用方法：MyToast(msg,5000).show()
     *
     * @param showText
     * @param duration
     */
    public MyToast(String showText, long duration) {
        this.showText = showText;
        counter = new TimeCounter(duration, 1000);
        counter.start();
    }

    public void show() {
        mToast = Toast.makeText(MyApplication.getInstance(), showText, MY_DURATION);
        mToast.show();
    }

    class TimeCounter extends CountDownTimer {
        long millisInFuture;

        public TimeCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.millisInFuture = millisInFuture;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //如果已计时的等于Toast设的默认的，那就说明快消失了，那就再挣扎显示一下
            if ((millisInFuture - millisUntilFinished) % MY_DURATION == 0) {
                show();
            }
        }

        @Override
        public void onFinish() {
            mToast.cancel();
        }
    }

}
