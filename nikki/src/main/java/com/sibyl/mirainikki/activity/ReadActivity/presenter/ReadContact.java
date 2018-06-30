package com.sibyl.mirainikki.activity.ReadActivity.presenter;

import com.sibyl.mirainikki.base.BasePresenter;
import com.sibyl.mirainikki.base.BaseView;

/**
 * Created by Sasuke on 2016/5/12.
 */
public interface ReadContact {
    interface View extends BaseView<Presenter>{

        void showMsg(String msg);
    }

    interface Presenter extends BasePresenter{

        boolean setNewPassword(String input1, String input2);
    }
}
