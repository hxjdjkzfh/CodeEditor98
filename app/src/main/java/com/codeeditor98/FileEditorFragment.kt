package com.codeeditor98

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment

class FileEditorFragment : Fragment() {
    private lateinit var tab: FileTab
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tab = MainActivity.getTab(arguments?.getInt("index") ?: 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        textView = TextView(requireContext()).apply {
            text = tab.content
            setTextColor(0xFFFF0000.toInt())
            setBackgroundColor(0xFF000000.toInt())
            textSize = tab.fontSize
            setPadding(20, 20, 20, 20)
            isVerticalScrollBarEnabled = true
            isHorizontalScrollBarEnabled = true
        }

        val gestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                tab.fontSize *= detector.scaleFactor
                tab.fontSize = tab.fontSize.coerceIn(8f, 40f)
                textView.textSize = tab.fontSize
                return true
            }
        })

        textView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }

        if (tab.showLineNumbers) {
            val lines = tab.content.lines()
            val numbered = lines.mapIndexed { i, line -> "${i + 1}: $line" }.joinToString("\n")
            textView.text = numbered
        }

        return textView
    }
}
