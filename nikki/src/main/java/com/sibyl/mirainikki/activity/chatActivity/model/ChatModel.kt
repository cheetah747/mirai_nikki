package com.sibyl.mirainikki.activity.chatActivity.model

import android.os.Handler
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sibyl.mirainikki.MyApplication.MyApplication
import com.sibyl.mirainikki.R
import com.sibyl.mirainikki.activity.chatActivity.repo.ChatRepo
import com.sibyl.mirainikki.activity.chatActivity.ui.ChatDataItem
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Sasuke on 2019-9-2 0002.
 */
class ChatModel(val repo: ChatRepo, val app: MyApplication) : ViewModel() {
    //聊天内容数据
    var dataList = MutableLiveData<MutableList<ChatDataItem>>().apply {
        value = mutableListOf()
    }

    //刷新列表（其实只用刷新最后一项就行了）
    var isFreshRv: MutableLiveData<Boolean> = MutableLiveData()

    //需要刷新的具体位置
    var refreshRvPos: MutableLiveData<Int> = MutableLiveData()

    //当前输入
    var inputText: ObservableField<String> = ObservableField("")

    var longClickedPos = -1//记录长按位置

    /**発信*/
    fun sendMsg(msg: String, isMe: Boolean = true, orders: List<String> = listOf()) {
        if (msg.isBlank()) return
        dataList.value?.add(ChatDataItem().apply {
            this.time = SimpleDateFormat("HH時mm分").format(Date())
            this.msg = msg.trim()
            this.isMe = isMe
            this.orders = orders
        })
        isFreshRv.value = true
        inputText.set("")
        //判断并执行指令
        checkOrder(msg.trim())
    }


    /**発信ボタンクリックリスナー*/
    fun onSendClick(view: View) {
        sendMsg(inputText.get()!!)
    }

    /**ユーザーが入力したオーダーに応じる*/
    private fun checkOrder(orderInput: String) {//返回该条输入是否是指令
        //发出的消息不是我自己，那就不是指令
        if (!(dataList.value?.last()?.isMe ?: false)) return

        try {
            dataList.value?.get(dataList.value!!.size - 2)?.msg?.run {
                //撤回消息
                if (orderInput != "y" && endsWith(app.resources.getString(R.string.delete_msg))) {
                    dataList.value?.get(longClickedPos)?.msg = ""
                    dataList.value?.get(longClickedPos)?.time = app.resources.getString(R.string.msg_is_deleted)
                    refreshRvPos.value = longClickedPos
                    longClickedPos = -1
                    //撤回完成，再发个消息来提示
                    Handler().postDelayed({
                        //                    sendMsg(app.resources.getString(R.string.msg_is_deleted_success), false)
                    }, 1_000)
                    return
                }

//            if (endsWith()){
//
//            }
            }
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }

    }


}