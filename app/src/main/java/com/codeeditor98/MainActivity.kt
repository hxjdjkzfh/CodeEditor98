package com.codeeditor98

import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout   // Добавлен импорт LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.*

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: FileTabAdapter
    private val fileTabs = mutableListOf<FileTab>()

    private val openFileLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            val content = readTextFromUri(it)
            if (content != null) {
                val name = it.lastPathSegment ?: "opened.txt"
                fileTabs.add(FileTab(name, content, it.toString()))
                adapter.notifyItemInserted(fileTabs.size - 1)
                viewPager.currentItem = fileTabs.size - 1
                tabLayout.getTabAt(fileTabs.size - 1)?.select()
            } else showToast("Failed to open file")
        }
    }

    private val saveAsLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { uri ->
        uri?.let {
            val index = viewPager.currentItem
            val content = adapter.getContent(index)
            if (writeTextToUri(it, content)) {
                fileTabs[index].path = it.toString()
                fileTabs[index].title = it.lastPathSegment ?: "saved.txt"
                adapter.notifyItemChanged(index)
                showToast("Saved as \${fileTabs[index].title}")
            } else showToast("Save As failed")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(android.R.style.Theme_DeviceDefault)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        adapter = FileTabAdapter(this, fileTabs)
        viewPager.adapter = adapter

        applyEditorSettings()
        addNewTab()

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fileTabs[position].title
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_new -> { addNewTab(); true }
            R.id.menu_open -> { openFileLauncher.launch(arrayOf("*/*")); true }
            R.id.menu_save -> { handleSave(); true }
            R.id.menu_save_as -> { saveAsLauncher.launch("untitled.txt"); true }
            R.id.menu_settings -> {
                SettingsDialog(this) { applyEditorSettings() }.show(); true
            }
            R.id.menu_about -> { showToast("CodeEditor98 by Артур"); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun dispatchKeyShortcutEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            val ctrl = event.isCtrlPressed
            val shift = event.isShiftPressed
            when {
                ctrl && event.keyCode == KeyEvent.KEYCODE_S -> {
                    if (shift) saveAsLauncher.launch("untitled.txt") else handleSave()
                    return true
                }
                ctrl && event.keyCode == KeyEvent.KEYCODE_O -> {
                    openFileLauncher.launch(arrayOf("*/*"))
                    return true
                }
                ctrl && event.keyCode == KeyEvent.KEYCODE_N -> {
                    addNewTab()
                    return true
                }
            }
        }
        return super.dispatchKeyShortcutEvent(event)
    }

    private fun applyEditorSettings() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val fontSize = prefs.getInt("fontSize", 16)
        adapter.setFontSize(fontSize)
        findViewById<View>(R.id.tailHandle)?.isVisible = prefs.getBoolean("showTail", true)
        val layout = findViewById<LinearLayout>(R.id.editorContainer)
        layout.orientation = when (prefs.getString("panelSide", "bottom")) {
            "left", "right" -> LinearLayout.HORIZONTAL
            else -> LinearLayout.VERTICAL
        }
    }

    private fun addNewTab() {
        fileTabs.add(FileTab("untitled.txt", ""))
        adapter.notifyItemInserted(fileTabs.size - 1)
        viewPager.currentItem = fileTabs.size - 1
        tabLayout.getTabAt(fileTabs.size - 1)?.select()
    }

    private fun handleSave() {
        val index = viewPager.currentItem
        val tab = fileTabs[index]
        val uriStr = tab.path
        val content = adapter.getContent(index)
        if (uriStr != null) {
            val uri = Uri.parse(uriStr)
            if (writeTextToUri(uri, content))
                showToast("Saved \${tab.title}")
            else showToast("Save failed")
        } else {
            saveAsLauncher.launch(tab.title)
        }
    }

    private fun readTextFromUri(uri: Uri): String? {
        return try {
            contentResolver.openInputStream(uri)?.use { input ->
                BufferedReader(InputStreamReader(input)).readText()
            }
        } catch (e: Exception) { null }
    }

    private fun writeTextToUri(uri: Uri, content: String): Boolean {
        return try {
            contentResolver.openOutputStream(uri)?.use { output ->
                BufferedWriter(OutputStreamWriter(output)).use { it.write(content) }
            }
            true
        } catch (e: Exception) { false }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
