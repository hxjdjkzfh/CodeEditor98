package com.codeeditor98

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: FileTabAdapter
    private val fileTabs = mutableListOf<FileTab>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        adapter = FileTabAdapter(this, fileTabs)
        viewPager.adapter = adapter

        // Первая вкладка по умолчанию
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
            R.id.menu_new -> {
                addNewTab()
                true
            }
            R.id.menu_about -> {
                Toast.makeText(this, "CodeEditor98 by Артур", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addNewTab() {
        fileTabs.add(FileTab("untitled.txt", ""))
        adapter.notifyItemInserted(fileTabs.size - 1)
        viewPager.currentItem = fileTabs.size - 1
        tabLayout.getTabAt(fileTabs.size - 1)?.select()
    }
}
