package com.sibyl.mirainikki.activity.MainActivity.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.text.TextUtils;
import android.widget.Toast;

import com.sibyl.mirainikki.MyApplication.MyApplication;
import com.sibyl.mirainikki.MyToast.MyToast;
import com.sibyl.mirainikki.activity.MainActivity.helper.PasswordHelper;
import com.sibyl.mirainikki.activity.MainActivity.helper.PreferHelper;
import com.sibyl.mirainikki.reposity.FileData;
import com.sibyl.mirainikki.reposity.TimeData;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by Sasuke on 2016/5/10.
 */
public class DialogPresenter implements DialogContract.Presenter{
    DialogContract.View mView;
    MyApplication app;
    FingerprintManagerCompat fingerManager;
    CancellationSignal cancelSignal;//取消指纹验证的监听

    public DialogPresenter(DialogContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {
        app = MyApplication.getInstance();
        app.executor.execute(new Runnable() {
            @Override
            public void run() {
                TimeData.initNow();
                mView.init();
            }
        });
    }

    @Override
    public CancellationSignal getCancelSignal(){
        if(cancelSignal == null){
            cancelSignal = new CancellationSignal();
        }
        return cancelSignal;
    }

    @Override
    public void openNikkiFile() {
        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri = Uri.fromFile(FileData.getNikkiFile());
        Uri uri = FileProvider.getUriForFile(app, app.getApplicationContext().getPackageName() + ".fileProvider", FileData.getNikkiFile());
        intent.setDataAndType(uri, "text/plain");
        mView.start2Activity(intent);
    }

    /**
     * 初始化指纹验证Manager
     * @param context
     */
    public FingerprintManagerCompat getFingerManager(Context context){
        if(fingerManager == null){
            fingerManager = FingerprintManagerCompat.from(context);
        }
        return fingerManager;
    }

    /**
     * 验证口令
     * @param content
     */
    @Override
    public boolean verifyPassword(String content) {
       return PasswordHelper.checkPassword((Context)mView,content);
    }

    /**
     * 保存输入框里输入的文字，（附带了防空校验
     * @param content
     */
    @Override
    public void saveText(String content) {
        final StringBuffer sb = new StringBuffer(content);
        if(TextUtils.isEmpty(content)){
            return;
        }

        if(TextUtils.isEmpty(TimeData.getTime())){
            TimeData.initNow();
        }

        FileData.createMyFile((Context)mView,FileData.getNikkiFile());

        app.executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(FileData.getNikkiFile(),true));
                    if(! PreferHelper.getInstance().getString(TimeData.LAST_YEAR)
                            .equals(TimeData.getYear())){
                        writer.write(TimeData.getYear());
                        for(int i = 0; i < 100 ; i++){
                            writer.write("\r\n");
                        }
                    }
                    if(! PreferHelper.getInstance().getString(TimeData.LAST_MONTH)
                            .equals(TimeData.getYearMonth())){
                        writer.write("\r\n");
                        writer.write("《《《《《《《《《《 "+TimeData.getYearMonth()+"月 》》》》》》》》》》");
                        writer.write("\r\n");
                    }
                    if(! PreferHelper.getInstance().getString(TimeData.LAST_WEEK_OF_YEAR)
                            .equals(TimeData.getWeekOfYear())){
                        writer.write("\r\n");
                        writer.write("====================================");
                        writer.write("\r\n");
                        writer.write(TimeData.getDate());
                    }else if(! PreferHelper.getInstance().getString(TimeData.LAST_DAY)
                            .equals(TimeData.getDate())){
                        writer.write("\r\n");
                        writer.write(".......................................");
                        writer.write("\r\n");
                        writer.write(TimeData.getDate());
                    }
                    writer.write("\r\n");
                    writer.write(TimeData.getTime());
                    writer.write("\r\n");
                    writer.write(sb.toString());
                    writer.write("\r\n");
                    writer.flush();
                    writer.close();
                    MyToast.show((Context) mView, "セーブしました", Toast.LENGTH_SHORT);
                    TimeData.saveAllDatePrefs();//写入成功后就保存一下最新日期标记
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
