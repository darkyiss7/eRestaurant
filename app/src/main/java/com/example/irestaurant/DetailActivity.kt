package com.example.irestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.irestaurant.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    var count = 0
    lateinit var item: Item
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        item = intent.getSerializableExtra(CategoryActivity.ITEM_KEY) as Item
        binding.detailTitle.text = item.name_fr
        Picasso.get().load(item.images[0].ifEmpty { null })
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.imageFinal)
        binding.totalView.text=("Total : 0 €")
        binding.prixView.text = "Prix : "+item.prices[0].price + "€"

        binding.ingredientsView.text =item.ingredients.joinToString {  it -> "${it.name_fr}"  }
        var actionBar = supportActionBar
        actionBar!!.title = item.name_fr
        actionBar.setDisplayHomeAsUpEnabled(true)

    }
    fun decrement(view : View){
        count++
        binding.compteurView.text=(""+count)
        binding.totalView.text=("Total : "+count*item.prices[0].price.toInt() + "€")

    }
    fun increment(view : View){
        if (count<=0) count =0
        else count--
        binding.compteurView.text=(""+count)
        binding.totalView.text=("Total : "+count*item.prices[0].price.toInt() + "€")
    }
}