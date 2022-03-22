package com.example.irestaurant

import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatDrawableManager.get
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class RecyclerAdapter(private val platListe: java.util.ArrayList<Item>, val clickListener: (Item) -> Unit) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView : TextView = itemView.findViewById(R.id.text)
        val image : ImageView = itemView.findViewById(R.id.imagePreview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val View = LayoutInflater.from(parent.context).inflate(R.layout.list_items, parent, false)
        return ViewHolder(View)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = platListe[position]
        holder.textView.text = item.name_fr
        Picasso.get().load(item.images[0].ifEmpty { null })
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.image)
        holder.itemView.setOnClickListener{
            clickListener(item)
        }
    }

    override fun getItemCount(): Int {
        return platListe.size
    }
}