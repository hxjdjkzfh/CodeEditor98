package com.example.codeeditor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class EditorPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    val tabs = mutableListOf<Pair<String, Boolean>>() // title, isUnsaved

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment {
        return EditorTabFragment("fun main() { println(\"Tab $position\") }", tabs[position].second)
    }

    fun addTab(title: String, unsaved: Boolean) {
        tabs.add(Pair(title, unsaved))
        notifyItemInserted(tabs.size - 1)
    }

    fun getTitle(position: Int): String {
        val (name, unsaved) = tabs[position]
        return if (unsaved) "★ " else name
    }
}
