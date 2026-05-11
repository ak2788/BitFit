package com.example.bitfit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddEntryActivity : AppCompatActivity() {

    private var currentPhotoPath: String? = null
    private lateinit var ivPhotoPreview: ImageView

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && currentPhotoPath != null) {
                ivPhotoPreview.visibility = android.view.View.VISIBLE
                Glide.with(this).load(currentPhotoPath).into(ivPhotoPreview)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        val etFoodName = findViewById<TextInputEditText>(R.id.etFoodName)
        val etCalories = findViewById<TextInputEditText>(R.id.etCalories)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnAddPhoto = findViewById<Button>(R.id.btnAddPhoto)
        ivPhotoPreview = findViewById(R.id.ivPhotoPreview)

        btnAddPhoto.setOnClickListener {
            val photoFile = createImageFile()
            currentPhotoPath = photoFile.absolutePath
            val photoUri: Uri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                photoFile
            )
            takePictureLauncher.launch(photoUri)
        }

        btnSave.setOnClickListener {
            val name = etFoodName.text.toString().trim()
            val caloriesText = etCalories.text.toString().trim()

            if (name.isEmpty() || caloriesText.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val entry = FoodEntry(
                foodName = name,
                calories = caloriesText.toInt(),
                photoPath = currentPhotoPath
            )

            lifecycleScope.launch(IO) {
                FoodDatabase.getInstance(applicationContext)
                    .foodEntryDao()
                    .insert(entry)
                finish()

            }

        }
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
    }
}