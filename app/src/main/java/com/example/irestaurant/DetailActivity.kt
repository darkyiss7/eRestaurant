package com.example.irestaurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.irestaurant.databinding.ActivityDetailBinding
import com.google.gson.Gson
import java.io.*


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    var count = 1
    private var itemIsPresent = "false"
    lateinit var item: Item
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        item = intent.getSerializableExtra(CategoryActivity.ITEM_KEY) as Item
        binding.detailTitle.text = item.name_fr
        binding.totalView.text=("Total : "+item.prices[0].price +"€")
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
            val cartitems = arrayListOf<CartItem>()
            cartitems.add(cartitem)
            val items = CartData(cartitems)
            try {
                if(isFilePresent(it.context,"panier.json")){
                    val jsonString: String
                    val fos: FileInputStream = openFileInput("panier.json")
                    val size: Int = fos.available()
                    val buffer = ByteArray(size)
                    val gson = Gson()
                    fos.read(buffer)
                    fos.close()
                    jsonString = String(buffer, charset("UTF-8"))
                    if (jsonString=="{}"){
                        val gson2 = Gson()
                        val jsonString = gson2.toJson(items)
                        create(it.context,"panier.json",jsonString)
                    }
                    val itemsPresent = Gson().fromJson(jsonString, CartData::class.java)

                    for (i in itemsPresent.data.indices){
                        if (itemsPresent.data[i].plat.name_fr==cartitem.plat.name_fr){
                            itemsPresent.data[i].quantité+=cartitem.quantité
                            val jsonString2 = gson.toJson(itemsPresent)
                            create(it.context,"panier.json",jsonString2)
                            itemIsPresent="true"
                        }
                    }
                    if (itemIsPresent=="false"){
                        itemsPresent.data.add(cartitem)
                        val jsonString3 = gson.toJson(itemsPresent)
                        create(it.context,"panier.json",jsonString3)
                    }

                }else{
                    val gson = Gson()
                    val jsonString = gson.toJson(items)
                    create(it.context,"panier.json",jsonString)
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
        if (count<=1) count =1
        else count--
        binding.compteurView.text=(""+count)
        binding.totalView.text=("Total : "+count*item.prices[0].price.toInt() + "€")
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
                create(this,"panier.json", "{}")
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

    fun isFilePresent(context: Context, fileName: String): Boolean {
        val path: String = context.getFilesDir().getAbsolutePath().toString() + "/" + fileName
        val file = File(path)
        return file.exists()
    }
}