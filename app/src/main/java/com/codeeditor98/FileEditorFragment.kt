package com.codeeditor98

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment

class FileEditorFragment : Fragment() {

    private lateinit var tab: FileTab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tab = MainActivity.getTab(arguments?.getInt("index") ?: 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val editor = EditText(requireContext()).apply {
            setText(tab.content)
            setTextColor(0xFFFF0000.toInt())
            setBackgroundColor(0xFF000000.toInt())
            textSize = 16f
        }

        editor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val newText = s.toString()
                if (newText != tab.content) {
                    tab.content = newText
                    tab.saved = false
                    MainActivity.updateTabColor()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return editor
    }
}
