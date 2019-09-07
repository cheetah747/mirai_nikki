package com.sibyl.mirainikki.activity.chatActivity.repo

import com.sibyl.mirainikki.MyApplication.MyApplication.app
import com.sibyl.mirainikki.R
import com.sibyl.mirainikki.activity.MainActivity.helper.PreferHelper
import com.sibyl.mirainikki.activity.chatActivity.ui.ChatDataItem
import com.sibyl.mirainikki.reposity.FileData
import com.sibyl.mirainikki.reposity.TimeData
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
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
    suspend fun saveNikki(textList: List<ChatDataItem>?) = suspendCoroutine<Boolean> { conti ->
        if (textList.isNullOrEmpty()) {
            conti.resumeWithException(Exception(app.resources.getString(R.string.empty_straight_exit)))
            return@suspendCoroutine
        }

//        val sb = StringBuffer()

//        if (TextUtils.isEmpty(TimeData.getTime())) {
//            TimeData.initNow()
//        }
        FileData.createMyFile(app, FileData.getNikkiFile())
        val writer = BufferedWriter(FileWriter(FileData.getNikkiFile(), true))
        try {
            textList.forEach {
                var isLastDayChanged = false
                //年份有变
                if (PreferHelper.getInstance().getString(TimeData.LAST_YEAR) != it.year) {
                    writer.write(it.year)
                    for (i in 0..99) {
                        writer.write("\r\n")
                    }
                }
                //月份有变
                if (PreferHelper.getInstance().getString(TimeData.LAST_MONTH) != it.yearMonth) {
                    writer.write("\r\n")
                    writer.write("\r\n")
                    writer.write("《《《《《《《《《《 " + it.yearMonth + "月 》》》》》》》》》》")
                }
                //日期有变
                if (PreferHelper.getInstance().getString(TimeData.LAST_WEEK_OF_YEAR) != it.weekOfYear) {
                    isLastDayChanged = true
                    writer.write("\r\n")
                    writer.write("\r\n")
                    writer.write("====================================")
                    writer.write("\r\n")
                    writer.write(it.date)
                } else if (PreferHelper.getInstance().getString(TimeData.LAST_DAY) != it.date) {
                    isLastDayChanged = true
                    writer.write("\r\n")
                    writer.write("\r\n")
                    writer.write(".......................................")
                    writer.write("\r\n")
                    writer.write(it.date)
                }else{//如果没变，相当于在同一天内，那就需要空行了
                    writer.write("\r\n")
                }

                it.time?.takeIf { it.isNotBlank() && ( isLastDayChanged || PreferHelper.getInstance().getString(TimeData.LAST_TIME) != it) }?.let {
                    writer.write("\r\n")
                    writer.write(it)
                    writer.write("\r\n")
                }
                writer.write(it.msg)
//                textList.last().run { TimeData.saveAllDatePrefs(date, yearMonth, weekOfYear, year, time) }//写入成功后就保存一下最新日期标记
                TimeData.saveAllDatePrefs(it.date, it.yearMonth, it.weekOfYear, it.year, it.time)
                writer.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            conti.resumeWithException(e)
        } finally {
            writer.close()
        }
        conti.resume(true)
    }

}