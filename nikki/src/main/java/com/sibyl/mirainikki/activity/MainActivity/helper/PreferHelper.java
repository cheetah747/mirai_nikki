package com.sibyl.mirainikki.activity.MainActivity.helper;

import android.content.SharedPreferences;

import com.sibyl.mirainikki.MyApplication.MyApplication;

public class PreferHelper {
  public static final String NAME = "MIRAI_PREF";
  private static SharedPreferences sp;
  private static SharedPreferences.Editor editor;
  private volatile static PreferHelper mInstance;

  private PreferHelper() {
    sp = MyApplication.getInstance().getSharedPreferences(NAME, 0);
    editor = sp.edit();
  }

  // 增加了双重判断
  public static PreferHelper getInstance() {
    if (null == mInstance) {
      synchronized (PreferHelper.class) {
        if (null == mInstance) mInstance = new PreferHelper();
      }
    }
    return mInstance;
  }

  /**
   * 储存值
   */
  public void setString(String key, String value) {
    editor.putString(key, value);
    editor.commit();
  }

  public void setInt(String key, int value) {
    editor.putInt(key, value);
    editor.commit();
  }

  public void setLong(String key, long value) {
    editor.putLong(key, value);
    editor.commit();
  }

  public void setBoolean(String key, boolean value) {
    editor.putBoolean(key, value);
    editor.commit();
  }

  /**
   * 获取值
   */
  public String getString(String key) {
    return sp.getString(key, "");
  }

  public int getInt(String key) {
    return sp.getInt(key, -1);
  }

  public int getInt(String key, int defaultInt) {
    return sp.getInt(key, defaultInt);
  }

  public long getLong(String key) {
    return sp.getLong(key, 1);
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    return sp.getBoolean(key, defaultValue);
  }

  /**
   * @description：移除特定的
   * @date 2014年11月5日 下午4:30:08
   */
  public void remove(String name) {
    editor.remove(name);
    editor.commit();
  }


}
