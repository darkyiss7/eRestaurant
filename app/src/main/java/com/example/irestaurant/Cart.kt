package com.example.irestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.irestaurant.databinding.ActivityCartBinding
import com.example.irestaurant.databinding.ActivityCategoryBinding
import com.google.gson.Gson
import org.json.JSONArray
import java.io.FileInputStream


class Cart : AppCompatActivity() {
    private lateinit var binding : ActivityCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val jsonString: String
        val fos: FileInputStream = openFileInput("panier.json")
        val size: Int = fos.available()
        val buffer = ByteArray(size)
        val gson = Gson()
        fos.read(buffer)
        fos.close()
        jsonString = String(buffer, charset("UTF-8"))
        val items = Gson().fromJson(jsonString, CartData::class.java)
        Log.e("Cart", "The json is: $jsonString")
        if(jsonString!="{}"){
            binding.recyclerViewCart.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewCart.adapter = CartAdapter(items.data){
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(CategoryActivity.ITEM_KEY, it)
                startActivity(intent)
            }
        }



    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home,menu)
        Log.d("Panier",getFilesDir().getAbsolutePath())
        return super.onCreateOptionsMenu(menu)


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

}