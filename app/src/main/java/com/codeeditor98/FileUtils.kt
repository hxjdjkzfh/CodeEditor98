package com.codeeditor98

import android.os.Environment
import java.io.File

object FileUtils {
    fun getDownloadDir(): File {
        return File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ce98")
    }

    fun saveToFile(tab: FileTab): Boolean {
        try {
            val file = tab.path?.let { File(it) } ?: return false
            file.writeText(tab.content)
            tab.saved = true
            tab.lastSaved = System.currentTimeMillis()
            return true
        } catch (_: Exception) {}
        return false
    }

    fun saveAs(tab: FileTab, name: String): Boolean {
        try {
            val dir = getDownloadDir()
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, name)
            file.writeText(tab.content)
            tab.path = file.absolutePath
            tab.title = file.name
            tab.saved = true
            tab.lastSaved = System.currentTimeMillis()
            return true
        } catch (_: Exception) {}
        return false
    }

    fun rename(tab: FileTab, newName: String): Boolean {
        try {
            val old = tab.path?.let { File(it) } ?: return false
            val new = File(old.parent, newName)
            if (old.renameTo(new)) {
                tab.path = new.absolutePath
                tab.title = new.name
                return true
            }
        } catch (_: Exception) {}
        return false
    }

    fun openFrom(path: String): FileTab? {
        return try {
            val file = File(path)
            val content = file.readText()
            FileTab(title = file.name, content = content, path = file.absolutePath, saved = true)
        } catch (_: Exception) {
            null
        }
    }
}
