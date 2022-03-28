package com.example.irestaurant

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.io.FileInputStream

class BleAdapter(private val bleliste: ArrayList<ScanResult>) : RecyclerView.Adapter<BleAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textNom : TextView = itemView.findViewById(R.id.nomTextView)
        val textAddresse : TextView = itemView.findViewById(R.id.addresseTextView)

    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = bleliste[position]
        holder.textNom.text = item.device.name

    }

    fun addToList(res:ScanResult){
        val index:Int = bleliste.indexOfFirst{ it.device.address==res.device.address }
        if(index == -1){
            bleliste.add(res)
        }else{
            bleliste[index]=res
        }
        bleliste.sortBy { kotlin.math.abs(it.rssi) }
    }

    override fun getItemCount(): Int {
        return bleliste.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BleAdapter.ViewHolder {
        val View = LayoutInflater.from(parent.context).inflate(R.layout.list_items_ble, parent, false)
        return BleAdapter.ViewHolder(View)
    }
}