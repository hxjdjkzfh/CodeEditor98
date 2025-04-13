package com.codeeditor98

import android.content.Context import android.net.Uri import android.os.Bundle import androidx.activity.ComponentActivity import androidx.activity.compose.setContent import androidx.activity.compose.rememberLauncherForActivityResult import androidx.activity.result.contract.ActivityResultContracts import androidx.compose.foundation.background import androidx.compose.foundation.clickable import androidx.compose.foundation.layout.* import androidx.compose.material3.* import androidx.compose.runtime.* import androidx.compose.ui.Alignment import androidx.compose.ui.Modifier import androidx.compose.ui.graphics.Color import androidx.compose.ui.platform.LocalContext import androidx.compose.ui.text.input.TextFieldValue import androidx.compose.ui.unit.dp import androidx.compose.ui.unit.sp import androidx.compose.ui.window.Dialog import dev.sora.editor.CodeEditorView import dev.sora.editor.component.lang.* import java.io.OutputStreamWriter

class MainActivity : ComponentActivity() { override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState) setContent { val context = LocalContext.current val darkTheme = remember { mutableStateOf(loadPref(context, "darkTheme", false)) }

CompositionLocalProvider(LocalDarkTheme provides darkTheme.value) {
            MaterialTheme(
                colorScheme = if (darkTheme.value) darkColorScheme() else lightColorScheme()
            ) {
                CodeEditorWithSettings(darkTheme)
            }
        }
    }
}

@Composable
fun CodeEditorWithSettings(darkTheme: MutableState<Boolean>) {
    val context = LocalContext.current

    var codeText by remember { mutableStateOf("") }
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf("No file") }
    var fontSize by remember { mutableStateOf(loadPref(context, "fontSize", 14)) }
    var language by remember { mutableStateOf(loadPref(context, "language", "text")) }
    var autoSave by remember { mutableStateOf(loadPref(context, "autoSave", false)) }
    var showTail by remember { mutableStateOf(loadPref(context, "showTail", true)) }
    var panelSide by remember { mutableStateOf(loadPref(context, "panelSide", "bottom")) }

    var showSettings by remember { mutableStateOf(false) }
    var editorView: CodeEditorView? by remember { mutableStateOf(null) }

    val openLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.bufferedReader()?.use {
                codeText = it.readText()
            }
            fileUri = uri
            fileName = uri.lastPathSegment ?: "file.txt"
            editorView?.apply {
                setEditorLanguage(resolveLang(language))
                setText(codeText)
                textSize = fontSize.toFloat()
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
                title = { Text("CodeEditor98 — $fileName") },
                actions = {
                    Button(onClick = { showSettings = true }) { Text("Settings") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { openLauncher.launch(arrayOf("*/*")) }) { Text("Open") }
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
                    }) { Text("Save") }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            if (showTail && panelSide == "bottom") {
                Box(Modifier.fillMaxWidth().clickable { showSettings = true }.padding(6.dp)) {
                    Divider(thickness = 4.dp, color = Color.Gray, modifier = Modifier.align(Alignment.Center))
                }
            }
            AndroidView(factory = { ctx ->
                CodeEditorView(ctx).apply {
                    setEditorLanguage(resolveLang(language))
                    setText(codeText)
                    textSize = fontSize.toFloat()
                    editorView = this
                }
            }, modifier = Modifier.weight(1f))
        }

        if (showSettings) {
            Dialog(onDismissRequest = { showSettings = false }) {
                Surface(shape = MaterialTheme.shapes.medium, tonalElevation = 6.dp) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Settings", style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = fontSize.toString(),
                            onValueChange = {
                                it.toIntOrNull()?.let {
                                    fontSize = it
                                    savePref(context, "fontSize", it)
                                    editorView?.textSize = it.toFloat()
                                }
                            },
                            label = { Text("Font Size") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))

                        Text("Language")
                        DropdownMenu(expanded = true, onDismissRequest = {}) {
                            listOf("text", "java", "kotlin", "python", "cpp", "js").forEach {
                                DropdownMenuItem(text = { Text(it) }, onClick = {
                                    language = it
                                    savePref(context, "language", it)
                                    editorView?.setEditorLanguage(resolveLang(it))
                                    showSettings = false
                                })
                            }
                        }
                        Spacer(Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = autoSave, onCheckedChange = {
                                autoSave = it
                                savePref(context, "autoSave", it)
                            })
                            Spacer(Modifier.width(8.dp))
                            Text("Autosave")
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = showTail, onCheckedChange = {
                                showTail = it
                                savePref(context, "showTail", it)
                            })
                            Spacer(Modifier.width(8.dp))
                            Text("Show Handle")
                        }

                        Text("Panel Position")
                        DropdownMenu(expanded = true, onDismissRequest = {}) {
                            listOf("bottom", "left", "right").forEach {
                                DropdownMenuItem(text = { Text(it) }, onClick = {
                                    panelSide = it
                                    savePref(context, "panelSide", it)
                                    showSettings = false
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun resolveLang(type: String): IEditorLanguage {
    return when (type) {
        "java" -> LanguageJava()
        "kotlin" -> LanguageKotlin()
        "python" -> LanguagePython()
        "cpp" -> LanguageCpp()
        "js" -> LanguageJavascript()
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

private fun loadPref(context: Context, key: String, def: Int): Int {
    return context.getSharedPreferences("editor_prefs", Context.MODE_PRIVATE).getInt(key, def)
}

private fun savePref(context: Context, key: String, value: Int) {
    context.getSharedPreferences("editor_prefs", Context.MODE_PRIVATE)
        .edit().putInt(key, value).apply()
}

private fun loadPref(context: Context, key: String, def: String): String {
    return context.getSharedPreferences("editor_prefs", Context.MODE_PRIVATE).getString(key, def) ?: def
}

private fun savePref(context: Context, key: String, value: String) {
    context.getSharedPreferences("editor_prefs", Context.MODE_PRIVATE)
        .edit().putString(key, value).apply()
}

private val LocalDarkTheme = compositionLocalOf { false }

}

