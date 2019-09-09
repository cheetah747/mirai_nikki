package com.sibyl.mirainikki.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.sibyl.mirainikki.MyApplication.MyApplication.app

/**
 * @author Sasuke on 2019-9-6 0006.
 */

/***
 * 复制到剪切板
 */
fun copy2Clipboard(text: String?) {
    text?.let {
        (app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText("", text)
    }
}


