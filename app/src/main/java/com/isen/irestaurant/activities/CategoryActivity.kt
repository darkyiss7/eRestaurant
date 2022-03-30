package com.isen.irestaurant.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.isen.irestaurant.R
import com.isen.irestaurant.adapter.RecyclerAdapter
import com.isen.irestaurant.databinding.ActivityCategoryBinding
import com.isen.irestaurant.objects.Data
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class CategoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                binding.recyclerView.adapter = RecyclerAdapter(arrayOfItems) {
                    val intent2 = Intent(this, DetailActivity::class.java)
                    intent2.putExtra(ITEM_KEY, it)
                    intent2.putExtra("Categorie",intent.getStringExtra("Category"))
                    startActivity(intent2)
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
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.commande ->{
                return true
            }
            R.id.vider ->{
                create(this,"panier.json", "null")
                Toast.makeText(this, "Pannier vidé", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun create(context: Context, fileName: String, jsonString: String?): Boolean {
        val FILENAME = "panier.json"
        return try {
            val fos: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            if (jsonString != null) {
                fos.write(jsonString.toByteArray())
            }
            fos.close()
            true
        } catch (fileNotFound: FileNotFoundException) {
            false
        } catch (ioException: IOException) {
            false
        }
    }
}
