package com.example.codeeditor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.tiagohm.codeview.CodeView
import br.tiagohm.codeview.Language

class EditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        val codeView = findViewById<CodeView>(R.id.code_view)
        val kotlinCode = """
            fun main() {
                println("Hello, CodeEditor98!")
            }
        """.trimIndent()

        codeView.setCode(kotlinCode)
                .setLanguage(Language.KOTLIN)
                .setTheme(br.tiagohm.codeview.Theme.MONOKAI)
                .setZoomEnabled(true)
                .setShowLineNumber(true)
                .apply()
    }
}
