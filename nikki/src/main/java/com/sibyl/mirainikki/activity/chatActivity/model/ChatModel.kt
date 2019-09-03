package com.sibyl.mirainikki.activity.chatActivity.model

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sibyl.mirainikki.MyToast.MyToast
import com.sibyl.mirainikki.activity.chatActivity.repo.ChatRepo
import com.sibyl.mirainikki.activity.chatActivity.ui.ChatDataItem

/**
 * @author Sasuke on 2019-9-2 0002.
 */
class ChatModel(val repo: ChatRepo) : ViewModel() {
    //聊天内容数据
    var dataList = MutableLiveData<MutableList<ChatDataItem>>().apply {
        mutableListOf<ChatDataItem>()
    }

    /**発信ボタンクリックリスナー*/
    fun onSendClick(view: View) {
        MyToast.show("onSendClick()")

    }

}