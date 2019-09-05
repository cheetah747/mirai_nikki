package com.sibyl.mirainikki.activity.chatActivity.util

import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal

/**
 * @author Sasuke on 2019/9/5.
 */
//class FingerDominator {
//
//}

fun fingerCheck(context: Context, cancelCallback: () -> Unit, noBiometricCallback: () -> Unit, otherErrorCallback: () -> Unit, successCallback: () -> Unit, failedCallback: () -> Unit) {
    val cancelSignal = CancellationSignal()
    val builder = BiometricPrompt.Builder(context)
    builder.setTitle("生体認証します")
    builder.setNegativeButton("キャンセル", context.mainExecutor, DialogInterface.OnClickListener { dialogInterface, i ->
        cancelCallback.invoke()
        cancelSignal.cancel()
    })
    builder.build().authenticate(cancelSignal, context.mainExecutor, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            when (errorCode) {
                BiometricPrompt.BIOMETRIC_ERROR_NO_BIOMETRICS ->
                    noBiometricCallback.invoke()
                else ->
                    otherErrorCallback.invoke()
            }
        }

        override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
            throw RuntimeException("Stub!")
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            successCallback.invoke()
        }

        override fun onAuthenticationFailed() {
            failedCallback.invoke()
        }
    })
}