package com.codeeditor98

import android.content.Context
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

data class FileTab(
    var title: String,
    var content: String,
    var path: String? = null,
    var breakpoints: MutableSet<Int> = mutableSetOf()
)

class FileTabAdapter(
    private val context: Context,
    private val tabs: MutableList<FileTab>
) : RecyclerView.Adapter<FileTabAdapter.FileViewHolder>() {

    private var fontSize = 16

    fun setFontSize(size: Int) {
        fontSize = size
        notifyDataSetChanged()
    }

    fun getContent(index: Int): String {
        return tabs[index].content
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        // TextView для номеров строк с поддержкой "брейкпоинтов"
        val lineNumbers = TextView(context).apply {
            setTextColor(0xFFFF0000.toInt())
            setPadding(8, 8, 8, 8)
            textSize = fontSize.toFloat()
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            // При нажатии на номер строки, как пример — показываем Toast.
            setOnClickListener {
                // Здесь можно реализовать логику переключения брейкпоинта для выбранной строки.
                // Пока просто покажем сообщение.
                Toast.makeText(context, "Toggle breakpoint (not implemented)", Toast.LENGTH_SHORT).show()
            }
        }

        // Редактор текста для файла
        val editor = EditText(context).apply {
            setBackgroundColor(0xFF000000.toInt())
            setTextColor(0xFFFF0000.toInt())
            isSingleLine = false
            setPadding(12, 12, 12, 12)
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1f
            )
        }

        layout.addView(lineNumbers)
        layout.addView(editor)

        return FileViewHolder(layout, editor, lineNumbers)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val tab = tabs[position]
        holder.editor.setText(tab.content)
        holder.editor.textSize = fontSize.toFloat()
        holder.lineNumbers.textSize = fontSize.toFloat()
        holder.updateLineNumbers(tab)
        holder.editor.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                tab.content = s.toString()
                holder.updateLineNumbers(tab)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun getItemCount(): Int = tabs.size

    class FileViewHolder(
        view: View,
        val editor: EditText,
        val lineNumbers: TextView
    ) : RecyclerView.ViewHolder(view) {
        fun updateLineNumbers(tab: FileTab) {
            // Определяем количество строк — минимально 1.
            val lines = editor.lineCount.coerceAtLeast(1)
            val builder = StringBuilder()
            for (i in 1..lines) {
                if (tab.breakpoints.contains(i)) {
                    builder.append("● ")  // Индикатор брейкпоинта
                } else {
                    builder.append("   ")
                }
                builder.append("$i\n")
            }
            lineNumbers.text = builder.toString()
        }
    }
}
