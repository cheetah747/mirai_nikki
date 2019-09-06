package com.sibyl.mirainikki.activity.MainActivity.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sibyl.mirainikki.R;
import com.sibyl.mirainikki.activity.MainActivity.presenter.DialogContract;
import com.sibyl.mirainikki.activity.MainActivity.presenter.DialogPresenter;
import com.sibyl.mirainikki.activity.MainActivity.ui.YearListAdapter;
import com.sibyl.mirainikki.base.BaseActivity;
import com.sibyl.mirainikki.util.Constant;

import java.io.File;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements DialogContract.View {
    EditText text;
    ImageView fingerLogo;//指纹LOGO
    Button submitBtn;
    DialogContract.Presenter mPre;
    public static int longPressed = 1;//已激起几次长按事件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        getView();//取得
        setListener();
        mPre = new DialogPresenter(MainActivity.this);
        mPre.start();
    }

    @Override
    public void init() {
        if (getIntent().getBooleanExtra(Constant.IS_FROM_SHORTCUT, false)) {
            checkFinger();
        }
    }


    @Override
    public void setPresenter(DialogContract.Presenter presenter) {
        mPre = (DialogContract.Presenter) presenter;
    }

    public void getView() {
        text = (EditText) findViewById(R.id.text);
        submitBtn = (Button) findViewById(R.id.commit_button);
        fingerLogo = (ImageView) findViewById(R.id.fingerLogo);
    }

    public void setListener() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = text.getText().toString();
                mPre.saveText(content);
                finish();
            }
        });

        //现在加了长按菜单，应该也不需要什么按钮的长按监听了吧。。
//        submitBtn.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                circulatePerformLongClick(submitBtn);
//                return true;//据说默认长按的返回值就是false，长按之后会让事件继续蔓延到单击监听上去。。
//            }
//        });
    }

    /**
     * 循环发送长按事件 ，实现自定义长按有效时长。
     *
     * @param button
     */
//    public void circulatePerformLongClick(final View button) {
//        if (!button.isPressed()) {
//            longPressed = 1;
//            return;//如果松手了，那后面就不用做了
//        }
//        if (longPressed < Constant.LONG_PRESS_MAX) {//每改变一次数值为1秒，MAX为几次就是长按要几秒后才生效
//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//                    longPressed++;
//                    button.performLongClick();
//                }
//            }, 1000);
//        }
//        if (longPressed == Constant.LONG_PRESS_MAX) {//说明前面已经进行了LONG_PRESS_MAX次的事件循环，即已经长按了多少秒
//            longPressed = 1;
////            boolean isCorrect = mPre.verifyPassword(text.getText().toString());//验证密码
////            if (isCorrect) {
//                //跳转的ReadActivity没有完成，现在改成直接打开文件
////                Intent intent = new Intent(MainActivity.this, ReadActivity.class);
////                startActivity(intent);
////                overridePendingTransition(R.anim.act_read_enter,0);
//                checkFinger();
////            }
//        }
//    }

    /**
     * 弹出指纹验证对话框，验证成功就打开文件
     */
    @TargetApi(23)
    public void checkFinger() {
        switchFingerMode(true);//切换成指纹验证模式
//        mPre.getFingerManager(this).authenticate(null, 0, mPre.getCancelSignal(), new FingerprintManagerCompat.AuthenticationCallback() {
//            @Override
//            public void onAuthenticationError(int errMsgId, CharSequence errString) {//指纹全错，停止验证
//                super.onAuthenticationError(errMsgId, errString);
//                switchFingerMode(false);//关闭指纹验证模式
//                MyToast.show(MainActivity.this, "指纹验证失败", Toast.LENGTH_SHORT);
//            }
//
//            @Override
//            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
//                super.onAuthenticationHelp(helpMsgId, helpString);
//            }
//
//            @Override
//            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
//                super.onAuthenticationSucceeded(result);
////                mPre.openNikkiFile();
//                File[] fileList = mPre.getNikkiList();//显示年份列表，用户可以选择查看哪一年的记录。
//                //如果没有日记
//                if (fileList.length == 0) {
//                    MyToast.show(MainActivity.this, "空っぽ", Toast.LENGTH_LONG);
//                    finish();
//                }
//                //如果有一条日记
//                if (fileList.length == 1) {
//                    mPre.openNikkiFile(FileData.nikkiFile);//如果只有一个文件，那就直接打开就完事了
//                    finish();
//                }
//                //如果大于一条，那就显示列表，供选择哪一年
//                if (fileList.length > 1) {
//                    showYearListDialog(fileList);
//                }
//                switchFingerMode(false);//验证对了，就关闭指纹验证
//
//            }
//
//            @Override
//            public void onAuthenticationFailed() {
//                super.onAuthenticationFailed();
//                MyToast.show(MainActivity.this, "指纹验证失败", Toast.LENGTH_SHORT);
//            }
//        }, null);
    }

    @Override
    public void start2Activity(Intent intent) {
        startActivity(intent);
    }

    /**
     * 在存在多条记录的时候，显示列表来选择。
     */
    private void showYearListDialog(final File[] fileList) {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.year_list_layout, null);
        RecyclerView yearList = view.findViewById(R.id.yearListRv);
        yearList.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        yearList.setAdapter(new YearListAdapter(this, fileList, new Function1<File, Unit>() {
            @Override
            public Unit invoke(File file) {
                mPre.openNikkiFile(file);//点击跳转
                return null;
            }
        }));
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert)
                .setView(view)
                .setPositiveButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finish();
                    }
                }).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    /**
     * 切换是否是指纹验证模式
     *
     * @param isFingerMode
     */
    public void switchFingerMode(boolean isFingerMode) {
        if (isFingerMode) {
            fingerLogo.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
            text.setText("");
            submitBtn.setText("指押してください");
        } else {
            mPre.getCancelSignal().cancel();//取消指纹验证
            fingerLogo.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
            submitBtn.setText(getResources().getString(R.string.input_button));//書き込み
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        mPre.getCancelSignal().cancel();
        super.onDestroy();
    }

}
