package com.sibyl.mirainikki.activity.MainActivity.view

import android.content.Intent
import android.os.Bundle
import com.sibyl.mirainikki.base.BaseActivity
import com.sibyl.mirainikki.util.Constant

/**
 * Created by Sasuke on 2017/8/20.
 * 专门用于长按图标菜单进来的处理
 */
class ShortcutMainAct: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra(Constant.IS_FROM_SHORTCUT,true);
        startActivity(intent)
        finish()
    }
}