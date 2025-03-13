package com.example.maxres_new

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ResultScreenActivity : AppCompatActivity() {

    private lateinit var originalImageView: ImageView
    private lateinit var processedImageView: ImageView
    private lateinit var slider: View
    private lateinit var tryAgainButton: Button
    private lateinit var downloadButton: Button

    private var sliderY = 0f
    private var maxSliderY = 0f
    private var minSliderY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_screen)

        originalImageView = findViewById(R.id.originalImageView)
        processedImageView = findViewById(R.id.processedImageView)
        slider = findViewById(R.id.slider)
        tryAgainButton = findViewById(R.id.tryAgainButton)
        downloadButton = findViewById(R.id.downloadButton)

        // Get the original image from the intent
        val originalImageBitmap = intent.getParcelableExtra<Bitmap>("originalImage")

        // Load the original image into the ImageView
        originalImageBitmap?.let {
            originalImageView.setImageBitmap(it)
        }

        // Simulate receiving the processed image from the backend
        val processedImageBitmap = simulateBackendResponse()

        // Load the processed image into the ImageView
        processedImageBitmap?.let {
            processedImageView.setImageBitmap(it)
        }

        // Set up slider touch listener
        slider.setOnTouchListener { _, event ->
            handleSliderTouch(event)
            true
        }

        // Set up button click listeners
        tryAgainButton.setOnClickListener {
            // Handle try again button click
        }

        downloadButton.setOnClickListener {
            // Handle download button click
        }
    }

    private fun handleSliderTouch(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // Calculate slider position
                sliderY = event.y

                // Limit slider movement
                sliderY = sliderY.coerceIn(minSliderY, maxSliderY)

                // Update slider position
                slider.y = sliderY

                // Update processed image clipping
                updateProcessedImageClip()
            }
        }
    }

    private fun updateProcessedImageClip() {
        // Calculate clip height
        val clipHeight = slider.y.toInt()

        // Create clip rect
        val clipRect = Rect(0, 0, processedImageView.width, clipHeight)

        // Create bitmap for clipping
        val bitmap = Bitmap.createBitmap(processedImageView.width, processedImageView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.clipRect(clipRect)

        // Set bitmap as processed image drawable
        processedImageView.setImageBitmap(bitmap)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            // Calculate slider limits
            minSliderY = 0f
            maxSliderY = processedImageView.height.toFloat() - slider.height
            sliderY = maxSliderY / 2
            slider.y = sliderY
            updateProcessedImageClip()
        }
    }

    // Simulate receiving the processed image from the backend
    private fun simulateBackendResponse(): Bitmap? {
        // Replace this with actual backend communication later
        return BitmapFactory.decodeResource(resources, R.drawable.processed_image)
    }
}