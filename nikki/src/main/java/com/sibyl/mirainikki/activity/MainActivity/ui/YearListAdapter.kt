package com.sibyl.mirainikki.activity.MainActivity.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.sibyl.mirainikki.R
import org.jetbrains.anko.find
import java.io.File

/**
 * @author Sasuke on 2019-04-15 0015.
 */
class YearListAdapter(val context: Context,val fileList: Array<File>,val clickCallBack: (File) -> Unit): RecyclerView.Adapter<YearListAdapter.YearListHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): YearListHolder {
        return YearListHolder(LayoutInflater.from(context).inflate(R.layout.year_list_item,parent,false))
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    override fun onBindViewHolder(holder: YearListHolder?, pos: Int) {
        holder?.let {
            it.yearBtn.text = fileList[pos].name.replace(".","\n.")
            it.yearBtn.setOnClickListener {
                clickCallBack.invoke(fileList[pos])
            }
        }
    }

    class YearListHolder(view: View): RecyclerView.ViewHolder(view){
        var yearBtn = view.find<Button>(R.id.yearBtn)
    }
}