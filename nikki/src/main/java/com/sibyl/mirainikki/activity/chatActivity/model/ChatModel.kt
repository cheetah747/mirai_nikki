package com.sibyl.mirainikki.activity.chatActivity.model

import android.os.Handler
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sibyl.mirainikki.MyApplication.MyApplication
import com.sibyl.mirainikki.R
import com.sibyl.mirainikki.activity.chatActivity.repo.ChatRepo
import com.sibyl.mirainikki.activity.chatActivity.ui.ChatDataItem
import com.sibyl.mirainikki.reposity.TimeData
import kotlinx.coroutines.launch

/**
 * @author Sasuke on 2019-9-2 0002.
 */
class ChatModel(val repo: ChatRepo, val app: MyApplication) : ViewModel() {
    //聊天内容数据
    var dataList = MutableLiveData<MutableList<ChatDataItem>>().apply {
        value = mutableListOf()
    }

    /**列出文件名
     * //显示年份列表，用户可以选择查看哪一年的记录。
     * */
    val nikkiFilesList by lazy {
        repo.getNikkiList()
    }

    //刷新列表（其实只用刷新最后一项就行了）
    var isFreshRv: MutableLiveData<Boolean> = MutableLiveData()

    //校验指纹，跳转历史
    var isCheckFinger: MutableLiveData<Boolean> = MutableLiveData()

    //需要刷新的具体位置
    var refreshRvPos: MutableLiveData<Int> = MutableLiveData()

    //当前输入
    var inputText: ObservableField<String> = ObservableField("")

    var longClickedPos = -1//记录长按位置

    /**关闭页面*/
    var isFinish: MutableLiveData<Boolean> = MutableLiveData()

    /**正在保存*/
    var isSavingFile = MutableLiveData<Boolean>().apply { value = false }

    /**选择图片*/
    var isSelectPhoto = MutableLiveData<Boolean>()

    /**聊天背景*/
    var backgroundPath = ObservableField<String>()

    /**発信*/
    fun sendMsg(msg: String, isMe: Boolean = true, view: View? = null) {
        if (msg.isBlank()) return
        dataList.value?.add(ChatDataItem().apply {
            this.time = TimeData.makeTime()//SimpleDateFormat("HH時mm分").format(Date())
            this.year = TimeData.getYear()
            this.date = TimeData.makeDate()
            this.yearMonth = this.date.substring(0, 7)
            this.weekOfYear = TimeData.makeWeekOfYear()
            this.msg = msg.trim()
            this.isMe = isMe
            this.view = view
        })
        isFreshRv.value = true
        inputText.set("")
        //判断并执行指令
        checkOrder(msg.trim())
    }


    /**発信ボタンクリックリスナー*/
    fun onSendClick(view: View) = sendMsg(inputText.get()!!)

    /**ユーザーが入力したオーダーに応じる*/
    private fun checkOrder(orderInput: String) {//返回该条输入是否是指令
        //只检查我自己的消息，其它消息跳过
        if (!(dataList.value?.last()?.isMe ?: false)) return

        //查看历史 (未来)========================
        if (dataList.value?.last()?.msg ?: "" in arrayOf(app.resources.getString(R.string.mirai), "みらい", "ミライ")) {
            dataList.value?.last()?.isOrder = true
            Handler().postDelayed({
                isCheckFinger.value = true
            }, 500)
            return
        }
        //设置背景 ========================
        if (dataList.value?.last()?.msg ?: "" in arrayOf("背景")) {
            dataList.value?.last()?.isOrder = true
            Handler().postDelayed({
                sendMsg("ピクチャを選択してください",false)
                isSelectPhoto.value = true
            }, 500)
            return
        }

        try {
            dataList.value?.get(dataList.value!!.size - 2)?.msg?.run {
                //撤回消息=====================
                if (endsWith(app.resources.getString(R.string.delete_msg))) {
                    dataList?.value?.last()?.isOrder = true//指令标记
                    if (orderInput in arrayOf("y", "Y", "是", "はい", "うん", "あ")) {//确定
                        dataList.value?.get(longClickedPos)?.msg = ""
                        dataList.value?.get(longClickedPos)?.time = app.resources.getString(R.string.msg_is_deleted)
                        refreshRvPos.value = longClickedPos
                        longClickedPos = -1
                        //撤回完成，再发个消息来提示
                        Handler().postDelayed({
                            sendMsg(app.resources.getString(R.string.msg_is_deleted_success), false)
                        }, 500)
                    }
                    return
                }

            }
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }

    }


    /**保存并退出*/
    fun saveNikkiAndExit() = viewModelScope.launch {
        isSavingFile.value = true
        try {
            val result = repo.saveNikki(dataList.value?.filter { it.isMsg4Save() })
            if (result) {
                sendMsg("セーブ成功", false)
                Handler().postDelayed({ isFinish.value = true }, 600)
            } else {
                sendMsg("セーブ失敗", false)
            }
        } catch (e: Exception) {
            sendMsg("${e.message}", false)
            //如果并不是保存异常，而只是单纯的没有内容，不需要存，那就直接退出
            if (e.message == app.resources.getString(R.string.empty_straight_exit)) {
                Handler().postDelayed({ isFinish.value = true }, 600)
            }
            e.printStackTrace()
        }

        isSavingFile.value = false
    }

}