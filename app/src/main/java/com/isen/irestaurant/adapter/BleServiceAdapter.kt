package com.isen.irestaurant.adapter

import android.bluetooth.BluetoothGattCharacteristic
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.isen.irestaurant.BLEService
import com.isen.irestaurant.BluetoothLeService
import com.isen.irestaurant.R
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class BleServiceAdapter(private val serviceList: MutableList<BLEService>) :
    ExpandableRecyclerViewAdapter<BleServiceAdapter.ServiceViewHolder,BleServiceAdapter.CharacteristicViewHolder>(serviceList){

    class ServiceViewHolder(itemView : View) : GroupViewHolder(itemView) {
        var serviceName : TextView = itemView.findViewById(R.id.characteristicName)
        var serviceUUID : TextView = itemView.findViewById(R.id.characteristicUUID)
        var serviceArrows : ImageView = itemView.findViewById(R.id.serviceArrow)
    }

    class CharacteristicViewHolder(itemView: View): ChildViewHolder(itemView) {
        var characteristicName : TextView = itemView.findViewById(R.id.characteristicName)
        var characteristicUUID : TextView = itemView.findViewById(R.id.characteristicUUID)
        var characteristicValeur : TextView = itemView.findViewById(R.id.characteristicValeur)
        var characteristicPropriete : TextView = itemView.findViewById(R.id.characteristicPropriete)
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder =
        ServiceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_ble, parent, false)
        )

    override fun onCreateChildViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacteristicViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_ble_characteristic,parent,false)
        return CharacteristicViewHolder(itemView)
    }

    override fun onBindChildViewHolder(
        holder: CharacteristicViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>,
        childIndex: Int
    ) {
        val characteristics = group.items[childIndex] as BluetoothGattCharacteristic
        holder.characteristicName.text = characteristics.uuid.toString()
    }

    override fun onBindGroupViewHolder(
        holder: ServiceViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>
    ) {
        holder.serviceName.text = group.title
    }
}