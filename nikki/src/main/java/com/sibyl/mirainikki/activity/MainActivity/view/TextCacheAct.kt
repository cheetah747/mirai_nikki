package com.sibyl.mirainikki.activity.MainActivity.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.sibyl.mirainikki.MyToast.MyToast
import com.sibyl.mirainikki.base.BaseActivity
import com.sibyl.mirainikki.reposity.FileData
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import java.io.BufferedWriter
import java.io.FileWriter


/**
 * Created by Sasuke on 2017/10/15.
 */
class TextCacheAct: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        MyToast.show(this,"TextCacheAct",Toast.LENGTH_SHORT)
        val text = getTextFromClipboard()
        write2File(text)
        finish()
    }

    fun getTextFromClipboard(): String{
        val myClipboard = (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        val abc: ClipData?  = myClipboard.getPrimaryClip()
        return abc?.getItemAt(0)?.getText().toString()
    }

    fun write2File(text: String){
        if(text.isNullOrEmpty()){
            MyToast.show(this,"空っぽだよ",Toast.LENGTH_SHORT)
            return
        }

        async {
            FileData.createMyFile(this@TextCacheAct ,FileData.getMiraiCacheFile())
            try {
                val writer = BufferedWriter(FileWriter(FileData.getMiraiCacheFile(), false))
                writer.write(text)
                writer.flush()
                writer.close()
                uiThread {
                    MyToast.show(this@TextCacheAct, "セーブしました:${FileData.getMiraiCacheFile().absolutePath.toString()}", Toast.LENGTH_SHORT)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}