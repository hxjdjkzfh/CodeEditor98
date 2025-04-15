package com.codeeditor98

import android.app.Activity
import java.io.File

class FileManager(private val activity: Activity) {
    fun openFile(path: String): String? {
        return try {
            File(path).readText()
        } catch (e: Exception) {
            null
        }
    }
    fun saveFile(path: String, content: String): Boolean {
        return try {
            File(path).writeText(content)
            true
        } catch (e: Exception) { false }
    }
}
