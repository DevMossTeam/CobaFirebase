package com.devmoss.cobafirebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.WindowCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enable edge-to-edge content display
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Initialize the FloatingActionButton
        floatingActionButton = findViewById(R.id.floatingActionButton)

        // Set up FloatingActionButton click listener
        floatingActionButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menu.add("Add Quote")
        popupMenu.menu.add("Add Image")

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.title) {
                "Add Quote" -> {
                    // Start activity for adding a quote
                    startActivity(Intent(this, AddQuoteActivity::class.java))
                    true
                }
                "Add Image" -> {
                    // Start activity for adding an image
                    startActivity(Intent(this, AddImageActivity::class.java))
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}
