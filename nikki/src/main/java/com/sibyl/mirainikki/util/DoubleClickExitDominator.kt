package com.sibyl.mirainikki.util

import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity

/**
 * @author Sasuke on 2019-9-6 0006.
 */
class DoubleClickExitDominator(val mActivity: AppCompatActivity, val clickedCallback: (() -> Unit)?, val doubledCallback: (() -> Unit)?) {

    private var isOnKeyBacking: Boolean = false
    private var mHandler: Handler? = null
    private var doubleClicked: Boolean = false//是否已双击，如果已双击，则不响应返回键，直到2秒倒计时结束

    init {
        mHandler = Handler(Looper.getMainLooper())
    }

    /**
     * Activity onKeyDown事件
     */
    fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode != KeyEvent.KEYCODE_BACK ) {
            return false
        }
        if (doubleClicked){
            return true
        }
        if (isOnKeyBacking) {
            doubleClicked = true
            mHandler?.removeCallbacksAndMessages(null)
            doubledCallback?.invoke()//双击后
            return true
        } else {
            isOnKeyBacking = true
            clickedCallback?.invoke()//第一次点击
            mHandler?.postDelayed({
                isOnKeyBacking = false
                doubleClicked = false
            }, 2000)
            return true
        }
    }

}