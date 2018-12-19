package com.sibyl.mirainikki.activity.MainActivity.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.sibyl.mirainikki.base.BaseActivity
import java.io.*
import java.net.URLDecoder


/**
 * Created by Sasuke on 2017/10/16.
 */
class UrlOpenerActivity: BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadByBrowser(
                readUrlFromFile(
                        transUrl2Stream(
                                getIntentPath())))
    }

    /**
     * 从“选择打开方式”那里传进来点击的文件所属路径
     */
    fun getIntentPath(): String? {
        var pathTemp = ""
        if (Intent.ACTION_VIEW == intent.action) {
            //filePath = Uri.decode(intent.getDataString().replace("file:///storage/emulated/0", "/sdcard"));
            try {
                pathTemp = URLDecoder.decode(intent.dataString.replace("file:///storage/emulated/0", "/sdcard"), "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }

        if (pathTemp.isNullOrEmpty()) finish()
        Log.d("SasukeLog", "getPath()获取路径成功")
        return pathTemp
    }

    /**两种情况，为了同时兼容Solid Explorer和Root Explorer */
    fun transUrl2Stream(filePath: String?): InputStream? {
        try {
            if (filePath?.startsWith("content://") ?: false) {//这里Solid Explorer的返回。
                return contentResolver.openInputStream(Uri.parse(filePath))
            } else {//这里Root Explorer的返回。
                return FileInputStream(filePath)
            }
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        return null
    }

    fun loadByBrowser(url: String?) {
        if (! url.isNullOrEmpty()) {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
            Log.d("SasukeLog", "loadByBrowser()成功执行所有工作")
        }
        finish()
    }

    fun readUrlFromFile(input: InputStream?): String? {
        var url: String? = ""
        try {
            if(input == null) return null
            var length: Int = 0
            length = input.available()
            val buffer = ByteArray(length)
            input.read(buffer)
            //url = EncodingUtils.getString(buffer, "UTF-8");
            url = String(buffer, Charsets.UTF_8)
            Log.d("SasukeLog", url)
            input.close()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        } catch (e: IOException) {
            Toast.makeText(this, "IOException", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        url = url?.replaceFirst("\\s", "")?.replace("[InternetShortcut]", "")?.replace("URL=","")?.replace("\r\n","")?.replace("\n","")
        Log.d("SasukeLog", "readUrlFromFile()成功解析出Url")
        return url

    }
}