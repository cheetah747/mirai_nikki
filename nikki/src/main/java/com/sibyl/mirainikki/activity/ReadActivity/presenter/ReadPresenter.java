package com.sibyl.mirainikki.activity.ReadActivity.presenter;

import android.text.TextUtils;

import com.sibyl.mirainikki.activity.MainActivity.helper.PasswordHelper;
import com.sibyl.mirainikki.activity.ReadActivity.model.FileTreeData;
import com.sibyl.mirainikki.base.BaseActivity;
import com.sibyl.mirainikki.reposity.FileData;

import java.io.File;

/**
 * Created by Sasuke on 2016/5/12.
 */
public class ReadPresenter implements ReadContact.Presenter{
    ReadContact.View mView;
    FileTreeData tree;
    public ReadPresenter(ReadContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if(tree == null){
            tree = new FileTreeData();
        }

        if(FileData.getRootFile() == null || !FileData.getRootFile().exists()){//实体文件根目录
            FileData.initFilePath();
        }

        File [] rootFiles = FileData.getRootFile().listFiles();//获取根目录下的所有文件列表
        //未完待续。。。。。


    }

    @Override
    public boolean setNewPassword(String input1, String input2) {
        if(TextUtils.isEmpty(input1) || TextUtils.isEmpty(input2)){//如果密码输入框为空
            mView.showMsg("入力枠が空いちゃダメだぞ");
            return false;
        }

        if(input1.equals(input2)){//如果两次输入的密码相同
            PasswordHelper.setPassword((BaseActivity)mView,input1);
            mView.showMsg("パスワード設置済み");
            return true;
        }else{
            mView.showMsg("二つの入力内容は一致してない");
            return false;
        }

    }
}
