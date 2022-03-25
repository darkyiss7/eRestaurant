package com.example.irestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.irestaurant.databinding.ActivityCategoryBinding
import org.json.JSONObject
import com.google.gson.Gson;

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_category)
        val queue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()
        jsonObject.put("id_shop", 1)
        val url = "http://test.api.catering.bluecodegames.com/menu"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response ->
                val stringResponse = response.toString()
                val item = Gson().fromJson(stringResponse, Data::class.java)
                val arrayOfItems = item.data.firstOrNull { it.name_fr == intent.getStringExtra("Category") }?.items ?: arrayListOf()
               // Log.d("CategoryActivity", arrayOfItems.toString())
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                binding.recyclerView.adapter = RecyclerAdapter(arrayOfItems) {
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra(ITEM_KEY, it)
                    startActivity(intent)
                }
            },
            { error ->
                Log.d("CategoryActivity", error.toString())
            }
        )
        queue.add(jsonObjectRequest)

        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var actionBar = supportActionBar
        actionBar!!.title = intent.getStringExtra("Category")
        actionBar.setDisplayHomeAsUpEnabled(true)

    }



    companion object {
        val ITEM_KEY ="item_key"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.panier -> {
                val intent = Intent(this, Cart::class.java)
                startActivity(intent)
                true
            }
            R.id.commande ->{
                return true
            }
            R.id.vider ->{
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
