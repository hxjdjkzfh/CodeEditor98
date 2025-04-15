package com.codeeditor98

import android.content.Context
import android.view.*
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class FileTabAdapter(
    private val context: Context,
    private val tabs: MutableList<FileTab>
) : RecyclerView.Adapter<FileTabAdapter.TabViewHolder>() {

    inner class TabViewHolder(val editText: EditText) : RecyclerView.ViewHolder(editText)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val editor = EditText(context)
        editor.setText(tabs[viewType].content)
        editor.setTextColor(0xFFFF0000.toInt())
        editor.setBackgroundColor(0xFF000000.toInt())
        editor.setTextSize(16f)
        editor.setPadding(16, 16, 16, 16)
        editor.setHorizontallyScrolling(true)
        return TabViewHolder(editor)
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        holder.editText.setText(tabs[position].content)
    }

    override fun getItemCount(): Int = tabs.size
}

data class FileTab(
    var title: String,
    var content: String,
    var path: String? = null
)
