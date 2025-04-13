package com.arthur.editor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arthur.editor.ui.CodeEditorView
import com.arthur.editor.ui.EditorSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EditorTabsApp()
        }
    }
}

@Composable
fun EditorTabsApp() {
    var openTabs by remember { mutableStateOf(listOf("New File")) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    var showLineNumbers by remember { mutableStateOf(true) }
    var showBreakpoints by remember { mutableStateOf(true) }
    var selectedLanguage by remember { mutableStateOf("JavaScript") }

    Column {
        Row(
            Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            openTabs.forEachIndexed { index, title ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, color = Color.Red) }
                )
            }
        }

        Divider(color = Color.Red)

        Row(modifier = Modifier.fillMaxSize()) {
            EditorSettings(
                showLineNumbers = showLineNumbers,
                onToggleLineNumbers = { showLineNumbers = it },
                showBreakpoints = showBreakpoints,
                onToggleBreakpoints = { showBreakpoints = it },
                selectedLanguage = selectedLanguage,
                onLanguageChange = { selectedLanguage = it }
            )

            CodeEditorView(
                fileName = openTabs[selectedTabIndex],
                showLineNumbers = showLineNumbers,
                showBreakpoints = showBreakpoints,
                language = selectedLanguage
            )
        }
    }
}
