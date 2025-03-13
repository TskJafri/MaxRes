package com.example.maxres_new

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var inputImageView: ImageView
    private lateinit var browseButton: Button
    private lateinit var submitButton: Button
    private var selectedImageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        inputImageView = findViewById(R.id.input_image)
        browseButton = findViewById(R.id.browse_button)
        submitButton = findViewById(R.id.submit_button)

        // Handle "Browse Image" button click
        browseButton.setOnClickListener {
            openGallery()
        }

        // Handle "Submit" button click
        submitButton.setOnClickListener {
            if (selectedImageBitmap != null) {
                startResultScreenActivity(selectedImageBitmap!!)
            }
        }
    }

    // Open the gallery to select an image
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }// Handle the result of the gallery intent
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                selectedImageBitmap = uriToBitmap(selectedImageUri)
                inputImageView.setImageBitmap(selectedImageBitmap)
            }
        }
    }

    // Convert URI to Bitmap
    private fun uriToBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
    }

    // Start ResultScreenActivity and pass the Bitmap
    private fun startResultScreenActivity(bitmap: Bitmap) {
        val intent = Intent(this, ResultScreenActivity::class.java)
        intent.putExtra("originalImage", bitmap)
        startActivity(intent)
    }
}