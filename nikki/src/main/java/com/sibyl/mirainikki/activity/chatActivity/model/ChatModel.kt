package com.sibyl.mirainikki.activity.chatActivity.model

import android.view.View
import androidx.databinding.ObservableField
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
        value = mutableListOf()
    }

    //刷新列表（其实只用刷新最后一项就行了）
    var isFreshRv: MutableLiveData<Boolean> = MutableLiveData()

    //当前输入
    var inputText: ObservableField<String> = ObservableField("")

    /**発信*/
    fun sendMsg(msg: String, isMe: Boolean = true) {
        if (msg.isBlank()) return
        dataList.value?.add(ChatDataItem().apply {
            this.time = SimpleDateFormat("HH:mm").format(Date())
            this.msg = msg.trim()
            this.isMe = isMe
        })
        isFreshRv.value = true
        inputText.set("")
    }


    /**発信ボタンクリックリスナー*/
    fun onSendClick(view: View) {
        sendMsg(inputText.get()!!)
    }


}