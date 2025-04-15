package com.codeeditor98

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import androidx.core.widget.addTextChangedListener

class SettingsDialog(
    private val context: Context,
    private val onApply: () -> Unit
) {
    private val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun show() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_settings, null)

        val fontSizeInput = view.findViewById<EditText>(R.id.fontSizeInput)
        val panelPositionGroup = view.findViewById<RadioGroup>(R.id.panelPositionGroup)
        val tailCheckbox = view.findViewById<CheckBox>(R.id.tailCheckbox)
        val langSpinner = view.findViewById<Spinner>(R.id.langSpinner)

        fontSizeInput.setText(prefs.getInt("fontSize", 16).toString())
        tailCheckbox.isChecked = prefs.getBoolean("showTail", true)

        when (prefs.getString("panelSide", "bottom")) {
            "left" -> panelPositionGroup.check(R.id.radioLeft)
            "right" -> panelPositionGroup.check(R.id.radioRight)
            else -> panelPositionGroup.check(R.id.radioBottom)
        }

        val languages = listOf("text", "java", "kotlin", "python", "cpp", "js")
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, languages)
        langSpinner.adapter = adapter
        langSpinner.setSelection(languages.indexOf(prefs.getString("language", "text")))

        AlertDialog.Builder(context)
            .setTitle("Settings")
            .setView(view)
            .setPositiveButton("Apply") { _, _ ->
                val fontSize = fontSizeInput.text.toString().toIntOrNull() ?: 16
                prefs.edit()
                    .putInt("fontSize", fontSize)
                    .putBoolean("showTail", tailCheckbox.isChecked)
                    .putString("panelSide", when (panelPositionGroup.checkedRadioButtonId) {
                        R.id.radioLeft -> "left"
                        R.id.radioRight -> "right"
                        else -> "bottom"
                    })
                    .putString("language", langSpinner.selectedItem.toString())
                    .apply()
                onApply()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
