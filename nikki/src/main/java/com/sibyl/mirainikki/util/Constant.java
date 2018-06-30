package com.sibyl.mirainikki.util;

/**
 * Created by Sasuke on 2016/6/26.
 */
public interface Constant {
    int LONG_PRESS_MAX = 2;//长按时长(次数），4的话就是说从1加到4，因为1前面要走1秒，数到5后还要再走一秒才到验证，也就是说要长按5秒。
    float ALPHA = 0.25f;//全局各个组件的透明度统一设置

    String IS_FROM_SHORTCUT = "IS_FROM_SHORTCUT";//是否是从长按图标菜单进来的

}
