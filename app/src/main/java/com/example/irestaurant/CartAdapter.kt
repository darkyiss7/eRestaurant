package com.example.irestaurant

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.squareup.picasso.Picasso

class CartAdapter(private val platListe: java.util.ArrayList<CartItem>, val clickListener: (Item) -> Unit) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView : TextView = itemView.findViewById(R.id.text)
        val image : ImageView = itemView.findViewById(R.id.imagePreview)
        val prix : TextView = itemView.findViewById(R.id.description)
        val prixTotal : TextView = itemView.findViewById(R.id.prixTotal)
        // val ingredients : TextView = itemView.findViewById(R.id.ingredientsView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val View = LayoutInflater.from(parent.context).inflate(R.layout.list_items_cart, parent, false)
        return ViewHolder(View)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = platListe[position]
        holder.textView.text = item.plat.name_fr
        holder.prix.text = item.plat.prices[0].price + "€" + " x   "+item.quantité
        holder.prixTotal.text = "Total : "+(item.plat.prices[0].price.toInt() * item.quantité ).toString()+"€"
        Picasso.get().load(item.plat.images[0].ifEmpty { null })
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.image)
        holder.itemView.setOnClickListener{
            clickListener(item.plat)
        }
    }

    override fun getItemCount(): Int {
        return platListe.size
    }
}