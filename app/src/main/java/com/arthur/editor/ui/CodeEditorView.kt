import io.github.rosemoe.sora.widget.CodeEditor

import io.github.rosemoe.sora.langs.textmate.TextMateLanguage

import io.github.rosemoe.sora.langs.textmate.languages.JavaLanguage
package com.arthur.editor.ui

import android.content.Context
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
                    else -> PlainTextLanguage()
                }
            )
        }
    }, update = {
        it.setShowLineNumbers(showLineNumbers)
        it.invalidate()
    })
}
