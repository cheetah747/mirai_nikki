package com.sibyl.mirainikki.activity.MainActivity.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.sibyl.mirainikki.MyToast.MyToast;

import java.lang.ref.WeakReference;

/**
 * Created by Sasuke on 2016/5/11.
 */
public class PasswordHelper {
    Context context;
    public static SharedPreferences passwordPref;
    public static SharedPreferences.Editor prefEditor;

    public static void init(Context context){
        WeakReference<Context> ref = new WeakReference<Context>(context);
        if(passwordPref == null && ref != null){
            passwordPref = ref.get().getSharedPreferences("passwordConf",Context.MODE_PRIVATE);
        }
        if(prefEditor == null){
            prefEditor = passwordPref.edit();
        }

    }

    /**
     * 设置口令
     * @param password
     */
    public static void setPassword(Context context, String password){
        init(context);
        prefEditor.putString("password",password);
        prefEditor.commit();
    }

    public static String getPassword(Context context){
        init(context);
        return passwordPref.getString("password","");
    }
    /**
     * 检验口令
     * @param password
     */
    public static boolean checkPassword(Context context, String password){
        WeakReference<Context> ref = new WeakReference<Context>(context);
        if(ref == null){
            return false;
        }
        if(TextUtils.isEmpty(getPassword(ref.get()))){//如果本身就没设置密码
            if(!TextUtils.isEmpty(password.trim())){//只有在输入的密码不为空的时候，才看作是要设置新密码
                setPassword(ref.get(),password.trim());
                MyToast.show(ref.get(),"パスワードは設定済みです",Toast.LENGTH_LONG);
            }else{
                MyToast.show(ref.get(),"パスワードが設定してない、設定を勧めます",Toast.LENGTH_LONG);
            }
            return true;
        }else{//如果有设置密码，那就进行对比
            if(getPassword(ref.get()).equals(password.trim())){//如果成功匹配，说明密码正确，返回true
               // MyToast.show(ref.get(),"ようこそ",Toast.LENGTH_LONG);
                return true;
            }else{
                MyToast.show(ref.get(),"立ち入り禁止",Toast.LENGTH_LONG);
                return false;
            }
        }
    }
}
