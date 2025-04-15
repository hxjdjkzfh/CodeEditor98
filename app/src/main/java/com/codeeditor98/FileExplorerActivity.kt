package com.codeeditor98

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class FileExplorerActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private var currentDir: File = Environment.getExternalStorageDirectory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_explorer)
        listView = findViewById(R.id.listView)
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selected = listView.adapter.getItem(position) as String
            if (selected == "..") {
                currentDir = currentDir.parentFile ?: currentDir
                updateList()
            } else {
                val file = File(currentDir, selected)
                if (file.isDirectory) {
                    currentDir = file
                    updateList()
                } else {
                    val resultIntent = Intent()
                    resultIntent.data = android.net.Uri.fromFile(file)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
        updateList()
    }

    private fun updateList() {
        val files = currentDir.listFiles()
        val list = mutableListOf<String>()
        if (currentDir.parentFile != null) {
            list.add("..")
        }
        files?.sortedBy { it.name }?.forEach { list.add(it.name) }
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
    }
}
