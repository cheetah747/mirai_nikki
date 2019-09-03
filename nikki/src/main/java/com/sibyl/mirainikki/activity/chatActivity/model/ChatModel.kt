package com.sibyl.mirainikki.activity.chatActivity.model

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sibyl.mirainikki.activity.chatActivity.repo.ChatRepo
import com.sibyl.mirainikki.activity.chatActivity.ui.ChatDataItem
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Sasuke on 2019-9-2 0002.
 */
class ChatModel(val repo: ChatRepo) : ViewModel() {
    //聊天内容数据
    var dataList = MutableLiveData<MutableList<ChatDataItem>>().apply {
        mutableListOf<ChatDataItem>()
    }

    //当前输入
    var inputText = ""
    /**発信*/
    fun sendMsg(msg: String, isMe: Boolean = true)  {
        dataList.value?.add(ChatDataItem().apply {
            this.time = SimpleDateFormat("HH:mm").format(Date())
            this.msg = msg
            this.isMe = isMe
        })
    }


    /**発信ボタンクリックリスナー*/
    fun onSendClick(view: View) {
        sendMsg(inputText)
    }



}