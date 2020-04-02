package com.sibyl.mirainikki.activity.chatActivity.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sibyl.mirainikki.MyApplication.MyApplication
import com.sibyl.mirainikki.R
import com.sibyl.mirainikki.activity.MainActivity.ui.YearListAdapter
import com.sibyl.mirainikki.activity.chatActivity.model.ChatFactory
import com.sibyl.mirainikki.activity.chatActivity.model.ChatModel
import com.sibyl.mirainikki.activity.chatActivity.repo.ChatRepo
import com.sibyl.mirainikki.activity.chatActivity.ui.ChatAdapter
import com.sibyl.mirainikki.activity.chatActivity.ui.CustomLinearLayoutManager
import com.sibyl.mirainikki.base.BaseActivity
import com.sibyl.mirainikki.databinding.ChatActivityBinding
import com.sibyl.mirainikki.reposity.FileData
import com.sibyl.mirainikki.util.*
import com.sibyl.mirainikki.util.PhotoPickDominator.*
import java.io.File

/**
 * @author Sasuke on 2019-8-30 0030.
 * 新版入口Activity，仿手机QQ聊天界面
 */
class ChatActivity : BaseActivity() {
    val binding by lazy { DataBindingUtil.setContentView<ChatActivityBinding>(this, R.layout.chat_activity) }

    val model by lazy { ViewModelProviders.of(this, ChatFactory(ChatRepo(), MyApplication.app)).get(ChatModel::class.java) }

    /**当前界面是否不在最前*/
    var isPaused = false

    var picker: PhotoPickDominator? = null


    val doubleClickExitDominator by lazy {
        DoubleClickExitDominator(this,
                //点击一次的时候
                {
                    model.sendMsg("未来数: ${model.dataList.value?.count { it.isMsg4Save() }}, もう一回押すと、セーブして閉じます", false)
                },
                //双击的时候
                {
                    model.sendMsg("セーブしています", false)
                    model.saveNikkiAndExit()
                })
    }

    /**展示所有nikki的View*/
    val filesView by lazy {
        LayoutInflater.from(this).inflate(R.layout.year_list_layout, null).apply {
            findViewById<RecyclerView>(R.id.yearListRv).run {
                layoutManager = GridLayoutManager(this@ChatActivity, 3)
                setAdapter(YearListAdapter(this@ChatActivity, model.nikkiFilesList) { file ->
                    openNikkiFile(this@ChatActivity, file)//点击跳转
                    null
                })
            }
        }
    }
//    private lateinit var cancelSignal: CancellationSignal

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
            adapter = ChatAdapter(this@ChatActivity, model)

        }

        //刷新列表
        model.isFreshRv.observe(this, Observer {
            if (it) {
                val endPos = model.dataList.value!!.size - 1
                binding.chatRv.adapter?.notifyItemChanged(endPos)
                Handler().postDelayed({
                    binding.chatRv.smoothScrollToPosition(endPos)
                }, 200)
            }
        })
        //刷新具体位置的元素
        model.refreshRvPos.observe(this, Observer {
            binding.chatRv.adapter?.notifyItemChanged(it)
        })

        //使同时支持多行输入+响应键盘回车发送键
        binding.inputEditText.apply {
            setInputType(TYPE_TEXT_FLAG_MULTI_LINE)
            setSingleLine(false)
            setOnClickListener {
                //点击后会弹出键盘，然后挪到底部，避免被键盘遮挡
                Handler().postDelayed({ binding.chatRv.scrollToPosition(model.dataList.value!!.size - 1) }, 200)
            }
        }

        /**校验指纹*/
        model.isCheckFinger.observe(this, Observer {
            if (it) {
                model.sendMsg("すぐ未来一覧を表示します", false)
                Handler().postDelayed({
                    showNikkiList()
                }, 500)
//                model.sendMsg("まず生体認証をしてください", false, ImageView(this).apply {
//                    setImageResource(R.drawable.finger_print_logo)
//                    minimumWidth = 0
//                    minimumHeight = 0
//                })
//                fingerCheck(this,
//                        { model.sendMsg("認証キャンセル", false) },
//                        { model.sendMsg("非対応です", false) },
//                        { model.sendMsg("その他のエラーです", false) },
//                        {
//                            model.sendMsg("認証成功です、すぐ未来一覧を表示します", false)
//                            Handler().postDelayed({
//                                showNikkiList()
//                            }, 500)
//                        },
//                        { model.sendMsg("認証失敗です", false) }
//                )
            }
        })

        model.isFinish.observe(this, Observer {
            if (it) {
                finish()
            }
        })

        //正在保存文件
        model.isSavingFile.observe(this, Observer {
            binding.inputEditText.run { setText(""); isFocusable = !it;isFocusableInTouchMode = !it }
        })

        //选择背景图片
        model.selectLocalPhoto.observe(this, Observer {
            picker?.selectByLocal(it)
        })
    }


    /**にっきリストを表示*/
    fun showNikkiList() {
        //如果没有日记
        if (model.nikkiFilesList.size == 0) {
            model.sendMsg(resources.getString(R.string.empty_alert), false)
        }
        //如果有一条日记
        if (model.nikkiFilesList.size == 1) {
            openNikkiFile(this, model.nikkiFilesList.first()/*FileData.getNikkiFile()*/)//如果只有一个文件，那就直接打开就完事了
        }
        //如果大于一条，那就显示列表，供选择哪一年
        if (model.nikkiFilesList.size > 1) {
            model.sendMsg(resources.getString(R.string.here_is_all_the_mirai), false, filesView)
        }
    }

    override fun onResume() {
        super.onResume()
        isPaused = false
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //你都没显示在最前，那就不需要执行后面的了
        if (isPaused) return true
        //如果正在保存，那就不执行自定义双击操作，免得存重复了。。。
        if (model.isSavingFile.value!!) {
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return doubleClickExitDominator.onKeyDown(keyCode, event)
        }
        return super.onKeyDown(keyCode, event)
    }

    fun start() {
        picker = PhotoPickDominator(this)
        window.setBackgroundDrawableResource(R.color.window_background_color)
        //当第一次获取权限时，会重新调用这里，需要利用它重新刷新界面，所以要clear()+notify()
//        model.dataList.value?.clear()
//        binding.chatRv.adapter?.notifyDataSetChanged()
        //一进来就初始化背景图
        Handler().postDelayed({
            model.sendMsg(resources.getString(R.string.welcome_to_miraimikki), false)
        }, 500)
//        binding.sasukeUrl = FileData.getRootFile().canonicalPath + "/background.jpg"
//        model.background.set( /*FileData.getRootFile().canonicalPath + "/background.jpg"*/"")
    }


    var photoPathTemp: String? = ""
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            //裁剪返回======================================
            CROP_REQUEST -> {
                if (data != null) {
                    var photo: Bitmap? = null
                    if (data.extras != null) {
                        photo = data.extras.get("data") as Bitmap?
                    }
                    if (photo == null && data.data != null) {
                        photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.data))
                    }
                    //图像提取出来了，可以开始设置了
                    when (model.selectLocalPhoto.value) {
                        PHOTO_REQUEST_GALLERY -> {//设置背景
                            photo?.let { FileCache.saveBitmap(it, FileData.getBackgroundImgCache()) }
                            model.backgroundPath.set(FileData.getBackgroundImgCache())
                        }
                        PHOTO_REQUEST_YOU_ICON -> {//设置你的头像
                            photo?.let { FileCache.saveBitmap(it, FileData.getYouIconCache()) }
                            model.youIcon.set(FileData.getYouIconCache())
                        }
                        PHOTO_REQUEST_ME_ICON -> {//设置我的头像
                            photo?.let { FileCache.saveBitmap(it, FileData.getMeIconCache()) }
                            model.meIcon.set(FileData.getMeIconCache())
                        }
                    }

                    //删掉中间缓存图片
                    photoPathTemp?.takeIf { File(it).exists() }?.let { File(it).delete() }
                    Handler().postDelayed({
                        model.sendMsg("${when (model.selectLocalPhoto.value) {
                            PHOTO_REQUEST_YOU_ICON -> "あたしのアイコン"
                            PHOTO_REQUEST_ME_ICON -> "あなたのアイコン"
                            else -> "背景"
                        }
                        }が更新しました", false)
                    }, 800)
                }
            }
            //选图返回======================================
            else -> {
                photoPathTemp = picker?.onPhotoPathResult(this, requestCode, resultCode, data)
                photoPathTemp?.takeIf { it.isNotBlank() }?.let {
                    picker?.cropPhoto(this,
                            FuckGoogleAdaptUtil.android7AdaptUri(
                                    this,
                                    FileData.fileProviderAuth, File(photoPathTemp)),
                            if (requestCode == PHOTO_REQUEST_GALLERY) binding.chatContainerLayout.measuredWidth else 1,
                            if (requestCode == PHOTO_REQUEST_GALLERY) binding.chatContainerLayout.measuredHeight else 1
                    )
                }
            }

        }

    }
}