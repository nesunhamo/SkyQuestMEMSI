package com.example.skyquest

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DialogFragmentPika : DialogFragment() {

    val builder: AlertDialog.Builder? = activity?.let {
        AlertDialog.Builder(it)
    }

    val dialog: AlertDialog? = builder?.create()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.pikachu_dialog, null))
                // Add action buttons
                .setPositiveButton(R.string.continue_button
                ) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}