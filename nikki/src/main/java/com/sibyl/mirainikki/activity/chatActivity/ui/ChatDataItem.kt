package com.sibyl.mirainikki.activity.chatActivity.ui

/**
 * @author Sasuke on 2019-9-3 0003.
 */
class ChatDataItem {
    var msg: String = ""//聊天内容

    var time: String = ""
//        //聊天时间
//        get() {
//            val now = SimpleDateFormat("HH:mm").format(Date())
//            //只有在和上次时间不一样时，才显示时间，否则隐藏
//            if (now != field) {
//                field = now
//                return field
//            }else{
//                return ""
//            }
//        }

    var isMe = false//是我自己吗？
}
