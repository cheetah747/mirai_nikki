package com.sibyl.mirainikki.activity.MainActivity.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.sibyl.mirainikki.base.BasePresenter;
import com.sibyl.mirainikki.base.BaseView;

import java.io.File;

/**
 * Created by Sasuke on 2016/5/8.
 */
public interface DialogContract {
    interface View extends BaseView<Presenter>{
        void start2Activity(Intent intent);

        void init();
    }


    interface Presenter extends BasePresenter{
        void saveText(String content);

        boolean verifyPassword(String content);

        FingerprintManagerCompat getFingerManager(Context context);

        void openNikkiFile(File file);

        File [] getNikkiList();

        CancellationSignal getCancelSignal();
    }


}
