package com.devmoss.cobafirebase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class AddActivity : AppCompatActivity() {

    private lateinit var editTextQuote: EditText
    private lateinit var editTextAuthor: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        editTextQuote = findViewById(R.id.editTextQuote)
        editTextAuthor = findViewById(R.id.editTextAuthor)
        addButton = findViewById(R.id.addButton)

        addButton.setOnClickListener {
            val quote = editTextQuote.text.toString().trim()
            val author = editTextAuthor.text.toString().trim()

            if (quote.isEmpty()) {
                editTextQuote.error = "Cannot be empty"
                return@setOnClickListener
            }
            if (author.isEmpty()) {
                editTextAuthor.error = "Cannot be empty"
                return@setOnClickListener
            }
            addQuoteToDB(quote, author)
        }
    }

    private fun addQuoteToDB(quote: String, author: String) {
        val quoteHashmap = hashMapOf(
            "quote" to quote,
            "author" to author
        )

        val database = FirebaseDatabase.getInstance()
        val quotesRef = database.getReference("quotes")
        val key = quotesRef.push().key ?: return
        quoteHashmap["key"] = key

        quotesRef.child(key).setValue(quoteHashmap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Quote added successfully!", Toast.LENGTH_SHORT).show()
                editTextQuote.text.clear()
                editTextAuthor.text.clear()
                editTextQuote.clearFocus()
                editTextAuthor.clearFocus()
            } else {
                Toast.makeText(this, "Failed to add quote. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
