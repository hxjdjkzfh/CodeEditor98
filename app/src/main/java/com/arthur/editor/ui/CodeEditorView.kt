package com.arthur.editor.ui

import android.content.Context
import android.widget.FrameLayout
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.roseeditor.editor.CodeEditor
import com.roseeditor.langs.textmate.languages.*

@Composable
fun CodeEditorView(
    fileName: String,
    showLineNumbers: Boolean,
    showBreakpoints: Boolean,
    language: String
) {
    val context = LocalContext.current

    AndroidView(factory = { ctx: Context ->
        CodeEditor(ctx).apply {
            setShowLineNumbers(showLineNumbers)
            setLineNumberEnabled(showLineNumbers)
            setHighlightCurrentLine(true)
            setWordwrap(true)
            setText("fun main() = println(\"Hello from \$fileName\")")

            setEditorLanguage(
                when (language) {
                    "Java" -> JavaLanguage()
                    "Kotlin" -> KotlinLanguage()
                    "Python" -> PythonLanguage()
                    "C++" -> CppLanguage()
                    else -> JavaScriptLanguage()
                }
            )
        }
    }, update = {
        it.setShowLineNumbers(showLineNumbers)
        it.invalidate()
    })
}
