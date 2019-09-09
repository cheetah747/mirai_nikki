package com.sibyl.mirainikki.activity.chatActivity.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

/**
 * @author Sasuke on 2019/5/4.
 * 用于Databinding的自定义属性
 */
class DatabindingAdapterDominator {
    //试了一下，把imageUrl改个名也行，这个@BindingAdapter("sasukeUrl")写成@BindingAdapter("bind:sasukeUrl")也行
    //结果好像就是(View,String)两个参数和 sasukeUrl 名字在起作用。
    companion object {
        @JvmStatic
        @BindingAdapter("bind:sasukeUrl")
        fun loadImage(iv: ImageView, imageUrl: String?) {
            imageUrl?.takeIf { it.isNotBlank() }?.let {
                Glide.with(iv.context).load(imageUrl)
                        .centerCrop()
                        .apply(RequestOptions().apply {
                            diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                            skipMemoryCache(true)//禁用内存缓存
                        })
                        .into(iv)
            }
        }

    }
}

