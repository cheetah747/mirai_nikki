package com.sibyl.mirainikki.base;

import android.Manifest;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.sibyl.mirainikki.MyApplication.MyApplication;
import com.sibyl.mirainikki.reposity.FileData;

/**
 * Created by Sasuke on 2016/6/19.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置模糊玻璃
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        requestPermissions();
        MyApplication.getInstance().executor.submit(
                new Runnable() {
                    @Override
                    public void run() {
                        FileData.initFilePath();
                    }
                }
        );
    }

    public void requestPermissions() {
        String [] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                                Manifest.permission.READ_EXTERNAL_STORAGE};
        if (! PermissionsUtil.hasPermission(this, permissions)) {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {
                    //用户授予了权限
                    MyApplication.getInstance().executor.submit(
                            new Runnable() {
                                @Override
                                public void run() {
                                    FileData.initFilePath();
                                }
                            }
                    );
                }

                @Override
                public void permissionDenied(@NonNull String[] permissions) {
                    //用户拒绝了权限
                    requestPermissions();
                }
            }, permissions);
        }
    }
}
