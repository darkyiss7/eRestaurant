package com.example.irestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.irestaurant.databinding.ActivityHomeBinding

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
    }

    private fun goToCategory(category : String){
        val intent = Intent(this, CategoryActivity::class.java)

        intent.putExtra("Category",category)
        startActivity(intent)
    }
}