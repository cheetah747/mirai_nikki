package com.sibyl.mirainikki.activity.ReadActivity.view;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.sibyl.mirainikki.MyToast.MyToast;
import com.sibyl.mirainikki.R;
import com.sibyl.mirainikki.activity.ReadActivity.presenter.ReadContact;
import com.sibyl.mirainikki.activity.ReadActivity.presenter.ReadPresenter;
import com.sibyl.mirainikki.base.BaseActivity;
import com.sibyl.mirainikki.util.Constant;

/**
 * Created by Sasuke on 2016/5/12.
 */
public class ReadActivity extends BaseActivity implements ReadContact.View{
    ReadContact.Presenter mPresenter;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_read);

        initUI();

        mPresenter = new ReadPresenter(this);
        mPresenter.start();
    }


    public void initUI(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置Toolbar的返回键UI
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.getNavigationIcon().setAlpha(63);//255的四分之一
        //设置Toolbar的标题字颜色
        toolbar.setTitleTextColor(Color.parseColor("#3FFFFFFF"));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finishWithAnim();
                break;
            case R.id.change_password://修改密码
                View inputView = LayoutInflater.from(this).inflate(R.layout.dialog_reset_password,null);
                final EditText firstInput = (EditText)inputView.findViewById(R.id.password_input_1st);
                final EditText secondInput = (EditText)inputView.findViewById(R.id.password_input_2nd);
                final AlertDialog dialog = new AlertDialog.Builder(this)
                                                .setTitle("パスワード変更")
                                                .setView(inputView)
                                                .setPositiveButton("そうしよう",null).create();
                Window window = dialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.alpha = Constant.ALPHA;
                lp.flags = WindowManager.LayoutParams.FLAG_BLUR_BEHIND;//使弹窗周围不变黑
                window.setWindowAnimations(R.style.dialog_anim);
                window.setAttributes(lp);
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//单独获取对话框的button并添加普通的View点击监听器而不是Dialog点击监听器，可以防止点击后关闭对话框
                        if(mPresenter.setNewPassword(firstInput.getText().toString().trim(),
                                secondInput.getText().toString().trim())){//成功true手动关闭Dialog，失败false不做事
                            dialog.dismiss();
                        }
                    }
                });
                break;
            case R.id.search_text://检索文字
                MyToast.show(this,"R.id.search_text",Toast.LENGTH_LONG);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read_act_menu,menu);
        return true;
    }


    @Override
    public void setPresenter(ReadContact.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showMsg(String msg) {
        MyToast.show(this,msg,Toast.LENGTH_LONG);
    }

    @Override
    public void onBackPressed() {
        finishWithAnim();
    }

    public void finishWithAnim(){
        finish();
        overridePendingTransition(0,R.anim.act_read_exit);
    }
}
