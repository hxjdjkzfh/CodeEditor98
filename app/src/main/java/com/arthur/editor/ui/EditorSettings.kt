package com.arthur.editor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EditorSettings(
    showLineNumbers: Boolean,
    onToggleLineNumbers: (Boolean) -> Unit,
    showBreakpoints: Boolean,
    onToggleBreakpoints: (Boolean) -> Unit,
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .fillMaxHeight()
            .background(Color.Black)
            .padding(8.dp)
    ) {
        Text("Settings", color = Color.Red)
        Divider(color = Color.Red)

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(
                checked = showLineNumbers,
                onCheckedChange = onToggleLineNumbers,
                colors = CheckboxDefaults.colors(checkedColor = Color.Red)
            )
            Text("Line numbers", color = Color.Red)
        }

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(
                checked = showBreakpoints,
                onCheckedChange = onToggleBreakpoints,
                colors = CheckboxDefaults.colors(checkedColor = Color.Red)
            )
            Text("Breakpoints", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Syntax:", color = Color.Red)
        val languages = listOf("JavaScript", "Python", "Java", "Kotlin", "C++")
        DropdownMenuToggle(languages, selectedLanguage, onLanguageChange)
    }
}

@Composable
fun DropdownMenuToggle(
    items: List<String>,
    selectedItem: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
        ) {
            Text(selectedItem, color = Color.Red)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach {
                DropdownMenuItem(onClick = {
                    onSelect(it)
                    expanded = false
                }) {
                    Text(it, color = Color.Red)
                }
            }
        }
    }
}
