package com.arthur.editor.ui
import io.github.rosemoe.sora.widget.CodeEditor

import io.github.rosemoe.sora.langs.java.JavaLanguage

import io.github.rosemoe.sora.langs.kotlin.KotlinLanguage

import io.github.rosemoe.sora.langs.python.PythonLanguage

import io.github.rosemoe.sora.langs.cpp.CppLanguage





import android.content.Context
import android.widget.FrameLayout
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CodeEditorView(
    fileName: String,
    showLineNumbers: Boolean,
    showBreakpoints: Boolean,
    language: String
) {
    val context = LocalContext.current
    var editor: CodeEditor? by remember { mutableStateOf(null) }

    AndroidView(factory = { ctx: Context ->
        FrameLayout(ctx).apply {
            editor = CodeEditor(ctx).apply {
                isLineNumberEnabled = showLineNumbers
                setEditorLanguage(JavaLanguage())
                setText("// Hello, $fileName")
            }
            addView(editor)
        }
    }, update = {
        editor?.isLineNumberEnabled = showLineNumbers
        editor?.invalidate()
    })
}
