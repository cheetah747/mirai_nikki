package com.sibyl.mirainikki.activity.chatActivity.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sibyl.mirainikki.MyApplication.MyApplication
import com.sibyl.mirainikki.activity.chatActivity.repo.ChatRepo

/**
 * @author Sasuke on 2019-9-2 0002.
 */
class ChatFactory(val repo: ChatRepo,val app: MyApplication): ViewModelProvider.AndroidViewModelFactory(app) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return super.create(modelClass)
        return ChatModel(repo,app) as T
    }
}