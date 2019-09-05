package com.sibyl.mirainikki.activity.chatActivity.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sibyl.mirainikki.MyToast.MyToast
import com.sibyl.mirainikki.R
import com.sibyl.mirainikki.activity.chatActivity.model.ChatModel
import com.sibyl.mirainikki.databinding.ChatItemBinding

/**
 * @author Sasuke on 2019-9-2 0002.
 */
class ChatAdapter(val context: Context, val chatModel: ChatModel) : RecyclerView.Adapter<ViewHolderX>() {
    var timeCache: String = ""//当前时间
    var dataList: MutableList<ChatDataItem>? = mutableListOf()
    var longClickedPos = -1//记录长按位置

    init {
        dataList = chatModel.dataList.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderX {
        val binding = DataBindingUtil.inflate<ChatItemBinding>(
                LayoutInflater.from(context),
                R.layout.chat_item,
                parent,
                false)
        return ViewHolderX(binding.root)
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolderX, pos: Int) {
        val binding = DataBindingUtil.getBinding<ChatItemBinding>(holder.view)

        binding?.apply {
            isMe = dataList!![pos].isMe
            time = if (timeCache != dataList!![pos].time) dataList!![pos].time.apply { timeCache = this } else ""
            msg = dataList!![pos].msg
            containerLayout.setOnClickListener { hideKeyboard(context,holder.view)  }

            chatMeLayout.setOnLongClickListener {
                longClickedPos = pos
                MyToast.show("chatMeLayout.setOnLongClickListener $pos")
                true
            }
        }

    }

    //收起软键盘
    fun hideKeyboard(context: Context,view: View) {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    val balloon = createBalloon(context) {
//        setArrowSize(10)
//        setWidthRatio(1.0f)
//        setHeight(65)
//        setArrowPosition(0.7f)
//        setCornerRadius(4f)
//        setAlpha(0.9f)
//        setText("You can access your profile from on now.")
//        setTextColorResource(R.color.white)
////        setIconDrawable(ContextCompat.getDrawable(baseContext, R.drawable.ic_profile))
//        setBackgroundColorResource(R.color.colorAccent)
//        setOnBalloonClickListener{}
//        setBalloonAnimation(BalloonAnimation.FADE)
//        setLifecycleOwner(context as Activity)
//    }

}