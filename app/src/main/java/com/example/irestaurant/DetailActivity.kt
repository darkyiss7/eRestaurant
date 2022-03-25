package com.example.irestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.irestaurant.databinding.ActivityDetailBinding
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.io.FileWriter
import java.io.PrintWriter

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
        binding.totalView.setOnClickListener{
            val intent = Intent(this, Cart::class.java)
            val cartitem = CartItem(item,count)
            val path = "panier.json"
            try {
                PrintWriter(FileWriter(path)).use {
                    val gson = Gson()
                    val jsonString = gson.toJson(cartitem)
                    it.write(jsonString)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            startActivity(intent)
        }


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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("iditem",item.itemId.toString())
        if (item.itemId==2131296714){
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}