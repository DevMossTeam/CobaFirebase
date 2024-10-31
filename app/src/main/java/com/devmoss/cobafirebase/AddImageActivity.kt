package com.devmoss.cobafirebase

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devmoss.cobafirebase.R
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AddImageActivity : AppCompatActivity() {

    private lateinit var imagePreview: ImageView
    private lateinit var editTextDescription: EditText
    private lateinit var loadingPercentageText: TextView
    private lateinit var addImageButton: Button
    private var imageUri: Uri? = null

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_images)

        imagePreview = findViewById(R.id.imagePreview)
        loadingPercentageText = findViewById(R.id.loadingPercentageText)
        addImageButton = findViewById(R.id.addImageButton)

        // Image picker setup for imagePreview
        imagePreview.setOnClickListener {
            pickImageFromGallery()
        }

        // Upload button click listener
        addImageButton.setOnClickListener {
            if (imageUri != null) {
                uploadImageToFirebase()
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = data?.data
            imagePreview.setImageURI(imageUri)
        }
    }

    private fun uploadImageToFirebase() {
        loadingPercentageText.visibility = View.VISIBLE

        val storageReference = FirebaseStorage.getInstance().getReference("images/${UUID.randomUUID()}")
        val uploadTask = storageReference.putFile(imageUri!!)

        uploadTask.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
            loadingPercentageText.text = "Loading: $progress%"
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loadingPercentageText.text = "Upload Complete"
                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Upload failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
            loadingPercentageText.visibility = View.GONE
        }
    }
}