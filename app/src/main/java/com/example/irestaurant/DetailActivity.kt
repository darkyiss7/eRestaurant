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
        binding.totalView.text=("Total : 0 €")
        binding.prixView.text = "Prix : "+item.prices[0].price + "€"


        val carousseladapter = CarousselAdapter(this,item.images)
        binding.detailSlider.adapter =  carousseladapter


        binding.ingredientsView.text ="Ingrédients : "+item.ingredients.joinToString {"${it.name_fr}"}
        var actionBar = supportActionBar
        actionBar!!.title = item.name_fr
        actionBar.setIcon(R.drawable.ic_shopping_cart_24)
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