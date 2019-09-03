package com.sibyl.mirainikki.activity.chatActivity.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sibyl.mirainikki.R
import com.sibyl.mirainikki.activity.chatActivity.model.ChatFactory
import com.sibyl.mirainikki.activity.chatActivity.model.ChatModel
import com.sibyl.mirainikki.activity.chatActivity.repo.ChatRepo
import com.sibyl.mirainikki.activity.chatActivity.ui.ChatAdapter
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
        init()


    }

    fun bind() {
        binding.toolbarIcon.setOnClickListener { finish() }
        binding.chatModel = model
        binding.chatRv.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = ChatAdapter(this@ChatActivity,model.dataList.value)
        }
    }

    fun init() {
        binding


    }
}