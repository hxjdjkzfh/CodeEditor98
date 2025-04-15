package com.codeeditor98

data class FileTab(
    var title: String,
    var content: String,
    var path: String? = null,
    var saved: Boolean = false,
    var fontSize: Float = 16f,
    var showLineNumbers: Boolean = true,
    var lastSaved: Long = System.currentTimeMillis()
)
