package com.sibyl.mirainikki.activity.chatActivity.ui

import android.view.View

/**
 * @author Sasuke on 2019-9-3 0003.
 */
class ChatDataItem {
    var msg: String = ""//聊天内容

    var time: String = ""
//        //聊天时间
//        get() {
//            val now = SimpleDateFormat("HH時mm分").format(Date())
//            //只有在和上次时间不一样时，才显示时间，否则隐藏
//            if (now != field) {
//                field = now
//                return field
//            }else{
//                return ""
//            }
//        }

    var isMe = true//是我自己吗？
    var isOrder = false//是否是指令

    /**在最终保存时，判断是否需要保存这一段*/
    fun isMsg4Save() = isMe && !isOrder && msg.isNotBlank()

    /**自定义View，目前用于显示*/
    var view: View? = null

}
