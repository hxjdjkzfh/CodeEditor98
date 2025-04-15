package com.codeeditor98

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabAdapter(activity: FragmentActivity, private val tabs: List<FileTab>) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = tabs.size
    override fun createFragment(position: Int): Fragment {
        return FileEditorFragment.newInstance(tabs[position])
    }
}
