package com.example.irestaurant

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        var actionBar = supportActionBar
        actionBar!!.title = intent.getStringExtra("Category")
        actionBar.setDisplayHomeAsUpEnabled(true)

        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adaptor = RecyclerAdapter(resources.getStringArray(R.array.entrees))

        recyclerView.adapter=adaptor
    }

}