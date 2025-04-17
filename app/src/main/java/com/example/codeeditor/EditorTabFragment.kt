package com.example.codeeditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.tiagohm.codeview.CodeView
import br.tiagohm.codeview.Language
import br.tiagohm.codeview.Theme
import androidx.fragment.app.Fragment

class EditorTabFragment(private val code: String = "", private val unsaved: Boolean = false) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val codeView = CodeView(requireContext())
        codeView.setCode(code)
            .setLanguage(Language.KOTLIN)
            .setTheme(Theme.MONOKAI)
            .setZoomEnabled(true)
            .setShowLineNumber(true)
            .apply()
        return codeView
    }
}
