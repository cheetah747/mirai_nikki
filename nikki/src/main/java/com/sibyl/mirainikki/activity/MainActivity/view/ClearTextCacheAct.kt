package com.sibyl.mirainikki.activity.MainActivity.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.sibyl.mirainikki.MyToast.MyToast
import com.sibyl.mirainikki.base.BaseActivity

/**
 * Created by Sasuke on 2017/11/4.
 */
class ClearTextCacheAct: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clearTextCache()
        finish()
    }

    /**
     * 把剪切板里的内容清除掉
     */
    fun clearTextCache(){
        val myClipboard = (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        val newData: ClipData = ClipData.newPlainText("","");
        myClipboard.primaryClip = newData
        MyToast.show(this,"クリアしました！",Toast.LENGTH_SHORT)
    }
}