package com.arthur.editor.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.roseeditor.editor.CodeEditor
import com.roseeditor.langs.textmate.languages.*
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

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
                setShowLineNumbers(showLineNumbers)
                setLineNumberEnabled(showLineNumbers)
                setHighlightCurrentLine(true)
                setWordwrap(true)

                setEditorLanguage(
                    when (language) {
                        "Java" -> JavaLanguage()
                        "Kotlin" -> KotlinLanguage()
                        "Python" -> PythonLanguage()
                        "C++" -> CppLanguage()
                        else -> JavaScriptLanguage()
                    }
                )

                setText("// Use menu to open or save files...")
            }
            addView(editor)
        }
    }, update = {
        editor?.setShowLineNumbers(showLineNumbers)
        editor?.invalidate()
    })
}
