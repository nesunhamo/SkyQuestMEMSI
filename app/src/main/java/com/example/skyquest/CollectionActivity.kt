package com.example.skyquest

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class CollectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        confirmPika()
    }

    private fun confirmPika() {
        val newDialog = DialogFragmentPika()
        newDialog.show(supportFragmentManager, null)
    }
}