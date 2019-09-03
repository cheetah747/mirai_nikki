package com.sibyl.mirainikki.activity.chatActivity.view

import android.os.Bundle
import android.os.Handler
import android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sibyl.mirainikki.R
import com.sibyl.mirainikki.activity.chatActivity.model.ChatFactory
import com.sibyl.mirainikki.activity.chatActivity.model.ChatModel
import com.sibyl.mirainikki.activity.chatActivity.repo.ChatRepo
import com.sibyl.mirainikki.activity.chatActivity.ui.ChatAdapter
import com.sibyl.mirainikki.activity.chatActivity.ui.CustomLinearLayoutManager
import com.sibyl.mirainikki.base.BaseActivity
import com.sibyl.mirainikki.databinding.ChatActivityBinding

/**
 * @author Sasuke on 2019-8-30 0030.
 * 新版入口Activity，仿手机QQ聊天界面
 */
class ChatActivity : BaseActivity() {
    val binding by lazy { DataBindingUtil.setContentView<ChatActivityBinding>(this, R.layout.chat_activity) }

    val model by lazy { ViewModelProviders.of(this, ChatFactory(ChatRepo())).get(ChatModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        start()


    }

    fun bind() {
        binding.toolbarIcon.setOnClickListener { finish() }
        binding.chatModel = model
        binding.inputEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                model.run { sendMsg(inputText.get()!!) }
                true
            } else {
                false
            }
        }
        binding.chatRv.apply {
            layoutManager = CustomLinearLayoutManager(this@ChatActivity)/*.apply { stackFromEnd = true }*///设置stackFromEnd表示从底部开始展示数据，这样在弹出键盘时不会挡住
            adapter = ChatAdapter(this@ChatActivity, model.dataList.value)

        }

        //刷新列表
        model.isFreshRv.observe(this, Observer {
            if (it){
                val endPos = model.dataList.value!!.size - 1
                binding.chatRv.adapter?.notifyItemChanged(endPos)
                Handler().postDelayed({
                    binding.chatRv.smoothScrollToPosition(endPos)
                }, 200)
            }
        })

        binding.inputEditText.setInputType(TYPE_TEXT_FLAG_MULTI_LINE)
        binding.inputEditText.setSingleLine(false)
    }

    fun start() {
        Handler().postDelayed({
            model.sendMsg("未来日記へようこそ！", false)
        }, 500)
    }
}