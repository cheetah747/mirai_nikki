package com.sibyl.mirainikki.base;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

/**
 * Created by Sasuke on 2016/6/19.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置模糊玻璃
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        requestPermissions();
    }

    public void requestPermissions() {
        String [] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                                Manifest.permission.READ_EXTERNAL_STORAGE};
        if (! PermissionsUtil.hasPermission(this, permissions)) {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {
                    //用户授予了权限
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
