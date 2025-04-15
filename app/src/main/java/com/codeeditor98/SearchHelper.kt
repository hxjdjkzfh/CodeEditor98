package com.codeeditor98

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan

object SearchHelper {
    fun highlightAll(text: String, query: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder(text)
        if (query.isEmpty()) return builder
        var index = text.indexOf(query, 0, true)
        while (index >= 0) {
            builder.setSpan(
                BackgroundColorSpan(Color.YELLOW),
                index,
                index + query.length,
                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            index = text.indexOf(query, index + query.length, true)
        }
        return builder
    }
}
