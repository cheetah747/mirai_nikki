package com.sibyl.mirainikki.activity.chatActivity.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE
import android.view.inputmethod.EditorInfo
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sibyl.mirainikki.MyApplication.MyApplication
import com.sibyl.mirainikki.R
import com.sibyl.mirainikki.activity.chatActivity.model.ChatFactory
import com.sibyl.mirainikki.activity.chatActivity.model.ChatModel
import com.sibyl.mirainikki.activity.chatActivity.repo.ChatRepo
import com.sibyl.mirainikki.activity.chatActivity.ui.ChatAdapter
import com.sibyl.mirainikki.activity.chatActivity.ui.CustomLinearLayoutManager
import com.sibyl.mirainikki.activity.chatActivity.util.fingerCheck
import com.sibyl.mirainikki.base.BaseActivity
import com.sibyl.mirainikki.databinding.ChatActivityBinding
import com.sibyl.mirainikki.reposity.FileData
import java.io.File

/**
 * @author Sasuke on 2019-8-30 0030.
 * 新版入口Activity，仿手机QQ聊天界面
 */
class ChatActivity : BaseActivity() {
    val binding by lazy { DataBindingUtil.setContentView<ChatActivityBinding>(this, R.layout.chat_activity) }

    val model by lazy { ViewModelProviders.of(this, ChatFactory(ChatRepo(), MyApplication.app)).get(ChatModel::class.java) }

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
                fingerCheck(this,
                        { model.sendMsg("認証キャンセル", false) },
                        { model.sendMsg("非対応です", false) },
                        { model.sendMsg("その他のエラーです", false) },
                        {
                            model.sendMsg("認証成功です、今から未来をご覧ください", false)
                            Handler().postDelayed({
                                showNikkiList()
                            }, 1_000)
                        },
                        { model.sendMsg("認証失敗です", false) }
                )
//                cancelSignal = CancellationSignal()
//                val builder = BiometricPrompt.Builder(this)
//                builder.setTitle("生体認証します")
//                builder.setNegativeButton("キャンセル", mainExecutor, DialogInterface.OnClickListener { dialogInterface, i ->
//                    model.sendMsg("認証キャンセル",false)
//                    cancelSignal.cancel()
//                })
//                builder.build().authenticate(cancelSignal, mainExecutor, object : BiometricPrompt.AuthenticationCallback() {
//                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                        when (errorCode) {
//                            BiometricPrompt.BIOMETRIC_ERROR_NO_BIOMETRICS ->
//                                model.sendMsg("非対応です",false)
//                            else ->
//                                model.sendMsg("その他のエラーです",false)
//                        }
//                    }
//
//                    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
//                        throw RuntimeException("Stub!")
//                    }
//
//                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                        model.sendMsg("認証成功です",false)
//                    }
//
//                    override fun onAuthenticationFailed() /                        model.sendMsg("認証失敗です",false)
//                    }
//                })
            }
        })


    }


    /**にっきリストを表示*/
    fun showNikkiList() {
        val fileList = model.listNikkiFiles()
        //如果没有日记
        if (fileList.size == 0) {
            model.sendMsg("あれ？空っぽですよ", false)
        }
        //如果有一条日记
        if (fileList.size == 1) {
            openNikkiFile(FileData.nikkiFile)//如果只有一个文件，那就直接打开就完事了
        }
        //如果大于一条，那就显示列表，供选择哪一年
        if (fileList.size > 1) {
//            showYearListDialog(fileList);
        }
    }

    /**直接打开文件*/
    fun openNikkiFile(file: File) {
        startActivity(Intent("android.intent.action.VIEW").apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //        Uri uri = Uri.fromFile(FileData.getNikkiFile());
            setDataAndType(FileProvider.getUriForFile(MyApplication.app, FileData.fileProviderAuth, file), "text/plain")
        })
    }


    fun start() {
        Handler().postDelayed({
            model.sendMsg("未来日記へようこそ！", false)
        }, 500)
    }
}