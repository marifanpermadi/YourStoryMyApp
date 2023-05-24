package com.example.yourstorymyapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.bumptech.glide.Glide
import com.example.yourstorymyapp.data.model.Story
import com.example.yourstorymyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        val ivPhoto = binding.ivPhoto
        val tvName = binding.tvName
        val tvDescription = binding.tvDescription
        val tvLatitude = binding.tvLat
        val tvLongitude = binding.tvLon

        val detailStory = intent.getParcelableExtra<Story>(DETAIL_STORY) as Story
        Glide.with(this)
            .load(detailStory.photo)
            .into(ivPhoto)
        tvName.text = detailStory.name
        tvDescription.text = detailStory.description
        tvLatitude.text = detailStory.lat.toString()
        tvLongitude.text = detailStory.lon.toString()

    }

    companion object {
        const val DETAIL_STORY = "detail_story"
    }
}