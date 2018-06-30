package com.sibyl.mirainikki.MyToast;

import android.content.Context;
import android.widget.Toast;

import com.sibyl.mirainikki.MyApplication.MyApplication;

public class ToastOnUiThreadRunnable implements Runnable{
	Context context;
	String showText;
	int showTime;
	public ToastOnUiThreadRunnable(String showText, int showTime){
		this.context = MyApplication.getInstance();
		this.showText = showText;
		this.showTime = showTime;
	}
	@Override
	public void run() {
		if(MyToast.mToast == null){
			MyToast.mToast = Toast.makeText(context,showText,showTime);
			//MyToast.mToast.setGravity(Gravity.CENTER, 0, 48);
		}else{//这样免得如果要连续出现多个Toast时，后面的Toast非要等前面的显示完了才能显示。
			//这样写就不需要排队了，直接覆盖前面的
			MyToast.mToast.cancel();
			MyToast.mToast = null;
			MyToast.mToast =Toast.makeText(context,showText,showTime);
			//MyToast.mToast.setGravity(Gravity.CENTER, 0, 48);
		}
		MyToast.mToast.show();
	}

}
