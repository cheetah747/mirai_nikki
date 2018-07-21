package com.sibyl.mirainikki.activity.MainActivity.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.sibyl.mirainikki.MyToast.MyToast
import com.sibyl.mirainikki.base.BaseActivity

/**
 * @author Sasuke on 2018/7/21.
 */
class JumpDownloadAct : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jump()
        finish()
    }

    fun jump() {
        try {
            startActivity(Intent().apply {
                setClassName("com.sibyl.mydownloadmanager", "com.sibyl.sasukedownloadmanager.activity.MainActivity")
            })
        }catch (e: ActivityNotFoundException){
            MyToast.show(this@JumpDownloadAct, "G Downloader は見つからなかった", Toast.LENGTH_SHORT)
        }

    }
}