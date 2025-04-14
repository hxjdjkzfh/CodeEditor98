package com.codeeditor98

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    onOpenFile: () -> Unit,
    onNewFile: () -> Unit,
    onHelp: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to CodeEditor98",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Digital Freedom",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onOpenFile,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open File")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNewFile,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("New File")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onHelp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Help / About")
        }
    }
}
