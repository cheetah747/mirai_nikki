package com.sibyl.mirainikki.activity.chatActivity.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sibyl.mirainikki.R
import com.sibyl.mirainikki.base.BaseActivity
import com.sibyl.mirainikki.databinding.ChatActivityBinding

/**
 * @author Sasuke on 2019-8-30 0030.
 * 新版入口Activity，仿手机QQ聊天界面
 */
class ChatActivity: BaseActivity() {
    val binding by lazy {  DataBindingUtil.setContentView<ChatActivityBinding>(this,R.layout.chat_activity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

    }

    fun init(){
        binding

    }
}