package com.codeeditor98

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.sora.editor.CodeEditorView
import dev.sora.editor.component.lang.*
import java.io.OutputStreamWriter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val isDarkTheme = remember { mutableStateOf(loadPref(context, "darkTheme", false)) }

            CompositionLocalProvider(LocalDarkTheme provides isDarkTheme.value) {
                MaterialTheme(colorScheme = if (isDarkTheme.value) darkColorScheme() else lightColorScheme()) {
                    CodeEditorScreen(isDarkTheme)
                }
            }
        }
    }

    @Composable
    fun CodeEditorScreen(isDarkTheme: MutableState<Boolean>) {
        val context = LocalContext.current

        var codeText by remember { mutableStateOf("") }
        var fileUri by remember { mutableStateOf<Uri?>(null) }
        var fileName by remember { mutableStateOf("No file") }
        var editorView: CodeEditorView? by remember { mutableStateOf(null) }

        var lineNumbers by rememberSaveable {
            mutableStateOf(loadPref(context, "lineNumbers", true))
        }
        var wordWrap by rememberSaveable {
            mutableStateOf(loadPref(context, "wordWrap", false))
        }

        val openLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                context.contentResolver.openInputStream(it)?.bufferedReader()?.use {
                    codeText = it.readText()
                }
                fileUri = uri
                fileName = uri.lastPathSegment ?: "file.txt"
                editorView?.apply {
                    setEditorLanguage(autoDetectLanguage(fileName))
                    setText(codeText)
                }
            }
        }

        val saveLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { uri: Uri? ->
            uri?.let {
                context.contentResolver.openOutputStream(it)?.let { out ->
                    OutputStreamWriter(out).use { writer -> writer.write(codeText) }
                    fileUri = uri
                    fileName = uri.lastPathSegment ?: "file.txt"
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("CodeEditor98 — ") },
                    actions = {
                        Row {
                            Text("Lines")
                            Switch(checked = lineNumbers, onCheckedChange = {
                                lineNumbers = it
                                savePref(context, "lineNumbers", it)
                                editorView?.isLineNumberEnabled = it
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Wrap")
                            Switch(checked = wordWrap, onCheckedChange = {
                                wordWrap = it
                                savePref(context, "wordWrap", it)
                                editorView?.isWordwrap = it
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (isDarkTheme.value) "Dark" else "Light")
                            Switch(checked = isDarkTheme.value, onCheckedChange = {
                                isDarkTheme.value = it
                                savePref(context, "darkTheme", it)
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { openLauncher.launch(arrayOf("*/*")) }) {
                                Text("Open")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                codeText = editorView?.text.toString()
                                if (fileUri != null) {
                                    context.contentResolver.openOutputStream(fileUri!!)?.let { out ->
                                        OutputStreamWriter(out).use { writer -> writer.write(codeText) }
                                    }
                                } else {
                                    saveLauncher.launch("untitled.txt")
                                }
                            }) {
                                Text("Save")
                            }
                        }
                    }
                )
            }
        ) { padding ->
            AndroidView(
                factory = { ctx ->
                    CodeEditorView(ctx).apply {
                        setEditorLanguage(LanguageText())
                        setText(codeText)
                        isLineNumberEnabled = lineNumbers
                        isWordwrap = wordWrap
                        editorView = this
                    }
                },
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            )
        }
    }

    private fun autoDetectLanguage(name: String): IEditorLanguage {
        return when {
            name.endsWith(".java") -> LanguageJava()
            name.endsWith(".kt") -> LanguageKotlin()
            name.endsWith(".py") -> LanguagePython()
            name.endsWith(".cpp") || name.endsWith(".cc") || name.endsWith(".cxx") -> LanguageCpp()
            name.endsWith(".js") -> LanguageJavascript()
            else -> LanguageText()
        }
    }

    private fun loadPref(context: Context, key: String, def: Boolean): Boolean {
        return context.getSharedPreferences("editor_prefs", Context.MODE_PRIVATE).getBoolean(key, def)
    }

    private fun savePref(context: Context, key: String, value: Boolean) {
        context.getSharedPreferences("editor_prefs", Context.MODE_PRIVATE)
            .edit().putBoolean(key, value).apply()
    }

    private val LocalDarkTheme = compositionLocalOf { false }
}
