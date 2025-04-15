package com.codeeditor98

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment

class FileEditorFragment : Fragment() {

    companion object {
        private const val ARG_CONTENT = "content"
        private const val ARG_INDEX = "index"

        fun newInstance(tab: FileTab): FileEditorFragment {
            val frag = FileEditorFragment()
            val bundle = Bundle()
            bundle.putString(ARG_CONTENT, tab.content)
            bundle.putInt(ARG_INDEX, -1) // можно использовать для обратной связи
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val editor = EditText(requireContext()).apply {
            setText(arguments?.getString(ARG_CONTENT))
            setTextColor(0xFFFF0000.toInt())
            setBackgroundColor(0xFF000000.toInt())
            textSize = 16f
        }
        return editor
    }
}
