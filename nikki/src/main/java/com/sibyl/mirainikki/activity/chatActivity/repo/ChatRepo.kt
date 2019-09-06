package com.sibyl.mirainikki.activity.chatActivity.repo

import com.sibyl.mirainikki.reposity.FileData
import java.io.File
import kotlin.coroutines.suspendCoroutine

/**
 * @author Sasuke on 2019-9-2 0002.
 */
class ChatRepo {

    /**发送*/
//    suspend fun sendMsg(msg: String) = suspendCoroutine<Boolean> { conti ->
//
//    }

    /**获取nikki文件名列表*/
    fun getNikkiList(): Array<File> = FileData.rootFile.listFiles { file, fileName -> fileName.endsWith(".mirai") }.apply { sortByDescending { it.name } }

    /***
     * 保存nikki
     */
    suspend fun saveNikki() = suspendCoroutine<Boolean> {


    }

}