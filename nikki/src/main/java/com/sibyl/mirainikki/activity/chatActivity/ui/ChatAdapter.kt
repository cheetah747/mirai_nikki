package com.sibyl.mirainikki.activity.chatActivity.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sibyl.mirainikki.R
import com.sibyl.mirainikki.activity.chatActivity.model.ChatModel
import com.sibyl.mirainikki.databinding.ChatItemBinding
import com.sibyl.mirainikki.util.copy2Clipboard

/**
 * @author Sasuke on 2019-9-2 0002.
 */
class ChatAdapter(val context: Context, val chatModel: ChatModel) : RecyclerView.Adapter<ViewHolderX>() {
    var timeCache: String = ""//当前时间
    var dataList: MutableList<ChatDataItem>? = mutableListOf()


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
            model = chatModel
            item = dataList!![pos]
            containerLayout.setOnClickListener { hideKeyboard(context, holder.view) }
            //在view为空的时候，如果不手动清除掉，就会因为容器重用而导致本不应该显示view的item显示了view
            if (dataList!![pos].view == null && chatYouLayout.childCount > 1){
                chatYouLayout.removeViewAt(1)
            }
            //在view不为空的时候，添加view显示出来
            dataList!![pos].view?.let { view ->
                view.parent?.let {
                    (it as ViewGroup).removeView(view)
                }
                chatYouLayout.addView(view,1)
            }
            //长按撤回消息
            chatMeLayout.setOnLongClickListener {
                chatModel.longClickedPos = pos
                chatModel.sendMsg("“${msg}” ${context.resources.getString(R.string.delete_msg)}", false)
                copy2Clipboard(msg)//清除前先拷贝一份

                true
            }
        }

    }

    //收起软键盘
    fun hideKeyboard(context: Context, view: View) {
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