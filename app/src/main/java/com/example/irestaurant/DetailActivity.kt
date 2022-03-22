package com.example.irestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.irestaurant.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var item: Item = intent.getSerializableExtra(CategoryActivity.ITEM_KEY) as Item
        binding.detailTitle.text = item.name_fr
        Picasso.get().load(item.images[0].ifEmpty { null })
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.imageFinal)
        binding.prixView.text = item.prices[0].price
        var actionBar = supportActionBar
        actionBar!!.title = intent.getStringExtra(CategoryActivity.ITEM_KEY)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }
}