package com.sibyl.mirainikki.activity.chatActivity.util

import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity

/**
 * @author Sasuke on 2019-9-6 0006.
 */
class DoubleClickExitDominator(val mActivity: AppCompatActivity,val clickedCallback: (()->Unit)?,val doubledCallback: (()->Unit)?) {

    private var isOnKeyBacking: Boolean = false
    private var mHandler: Handler? = null

    init {
        mHandler = Handler(Looper.getMainLooper())
    }

    /**
     * Activity onKeyDown事件
     */
    fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return false
        }
        if (isOnKeyBacking) {
            mHandler?.removeCallbacksAndMessages(null)
            doubledCallback?.invoke()//双击后
            return true
        } else {
            isOnKeyBacking = true
            clickedCallback?.invoke()//第一次点击
            mHandler?.postDelayed({ isOnKeyBacking = false }, 2000)
            return true
        }
    }

}