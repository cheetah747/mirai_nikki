package com.sibyl.mirainikki.activity.chatActivity.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.sibyl.mirainikki.MyApplication.MyApplication
import com.sibyl.mirainikki.reposity.FileData
import java.io.File

/**
 * @author Sasuke on 2019-9-6 0006.
 */
/**直接打开文件*/
fun openNikkiFile(context: Context, file: File) {
    context.startActivity(Intent("android.intent.action.VIEW").apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //        Uri uri = Uri.fromFile(FileData.getNikkiFile());
        setDataAndType(FileProvider.getUriForFile(MyApplication.app, FileData.fileProviderAuth, file), "text/plain")
    })
}