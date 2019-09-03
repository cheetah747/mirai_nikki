package com.sibyl.mirainikki.activity.chatActivity.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sibyl.mirainikki.R
import com.sibyl.mirainikki.databinding.ChatItemBinding

/**
 * @author Sasuke on 2019-9-2 0002.
 */
class ChatAdapter(val context: Context, var dataList: MutableList<ChatDataItem>?) : RecyclerView.Adapter<ViewHolderX>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderX {
        val binding = DataBindingUtil.inflate<ChatItemBinding>(
                LayoutInflater.from(context),
                R.layout.chat_item,
                parent,
                false)
        return ViewHolderX(binding.root)
    }

    override fun getItemCount(): Int {
        return dataList?.size ?:0
    }

    override fun onBindViewHolder(holder: ViewHolderX, position: Int) {
        val binding = DataBindingUtil.getBinding<ChatItemBinding>(holder.view)
    }
}