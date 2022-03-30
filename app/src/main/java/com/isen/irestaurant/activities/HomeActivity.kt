package com.isen.irestaurant.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.isen.irestaurant.R
import com.isen.irestaurant.databinding.ActivityHomeBinding
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.entreeBoutton.setOnClickListener{
            goToCategory(getString(R.string.entree_name))
        }
        binding.platBouton.setOnClickListener{
            goToCategory(getString(R.string.plats_name))
        }
        binding.dessertBouton.setOnClickListener{
            goToCategory(getString(R.string.desserts_name))
        }
        binding.boutonBluetooth.setOnClickListener{
            startActivity(Intent(this, BLEScanActivity::class.java))
        }
    }

    private fun goToCategory(category : String){
        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra("Category",category)
        startActivity(intent)
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