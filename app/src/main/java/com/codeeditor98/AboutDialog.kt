package com.codeeditor98

import android.app.AlertDialog
import android.content.Context

object AboutDialog {
    fun show(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("About CodeEditor98")
            .setMessage("Version 1.0\n\nMinimal offline code editor\nMade with love by Arthur.")
            .setPositiveButton("OK", null)
            .show()
    }
}
