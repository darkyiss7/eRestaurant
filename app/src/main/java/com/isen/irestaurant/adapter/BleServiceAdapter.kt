package com.isen.irestaurant.adapter

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.core.view.isVisible
import com.isen.irestaurant.BLEService
import com.isen.irestaurant.BluetoothLeService
import com.isen.irestaurant.R
import com.isen.irestaurant.objects.BLEUUIDAttributes
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import java.util.*
import kotlin.collections.ArrayList

class BleServiceAdapter(
    private val context: Context,
    private val serviceList: MutableList<BLEService>,
    private val readCharacteristicCallback: (BluetoothGattCharacteristic) -> Unit,
    private val writeCharacteristicCallback: (BluetoothGattCharacteristic) -> Unit,
    private val notifyCharacteristicCallback: (BluetoothGattCharacteristic, Boolean) -> Unit
    ) :
    ExpandableRecyclerViewAdapter<BleServiceAdapter.ServiceViewHolder,BleServiceAdapter.CharacteristicViewHolder>(serviceList){

    class ServiceViewHolder(itemView : View) : GroupViewHolder(itemView) {
        var serviceName : TextView = itemView.findViewById(R.id.characteristicName)
        var serviceUUID : TextView = itemView.findViewById(R.id.characteristicUUID)
        var serviceArrows : ImageView = itemView.findViewById(R.id.serviceArrow)
        override fun expand() {
            serviceArrows.animate().rotation(-90f).setDuration(400L).start()
        }

        override fun collapse() {
            serviceArrows.animate().rotation(0f).setDuration(400L).start()
        }
    }

    class CharacteristicViewHolder(itemView: View): ChildViewHolder(itemView) {
        var characteristicName : TextView = itemView.findViewById(R.id.characteristicName)
        var characteristicUUID : TextView = itemView.findViewById(R.id.characteristicUUID)
        var characteristicValeur : TextView = itemView.findViewById(R.id.characteristicValeur)
        var characteristicPropriete : TextView = itemView.findViewById(R.id.characteristicPropriete)
        val characteristicReadAction: Button = itemView.findViewById(R.id.boutonLecture)
        val characteristicWriteAction: Button = itemView.findViewById(R.id.boutonEcrire)
        val characteristicNotifyAction: Button = itemView.findViewById(R.id.bouttonNotifier)
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
        val uuid = "UUID : ${characteristics.uuid}"
        val titre = BLEUUIDAttributes.getBLEAttributeFromUUID(characteristics.uuid.toString()).title
        holder.characteristicName.text = titre
        holder.characteristicUUID.text = uuid
        val properties = arrayListOf<String>()

        addPropertyFromCharacteristic(
            characteristics,
            properties,
            "Lecture",
            BluetoothGattCharacteristic.PROPERTY_READ,
            holder.characteristicReadAction,
            readCharacteristicCallback
        )

        addPropertyFromCharacteristic(
            characteristics,
            properties,
            "Ecrire",
            (BluetoothGattCharacteristic.PROPERTY_WRITE or BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE),
            holder.characteristicWriteAction,
            writeCharacteristicCallback
        )

        addPropertyNotificationFromCharacteristic(
            characteristics,
            properties,
            holder.characteristicNotifyAction,
            notifyCharacteristicCallback
        )

        val proprietiesMessage = "Proprietés : ${properties.joinToString()}"
        holder.characteristicPropriete.text = proprietiesMessage
        characteristics.value?.let {
            val value = "Valeur : ${String(it)} Hex : 0x${(it.joinToString("") { byte -> "%02x".format(byte) }.toUpperCase(Locale.FRANCE))}"
            holder.characteristicValeur.isVisible = true
            holder.characteristicValeur.text = value
        }
    }

    private fun addPropertyFromCharacteristic(
        characteristic: BluetoothGattCharacteristic,
        properties: ArrayList<String>,
        propertyName: String,
        propertyType: Int,
        propertyAction: Button,
        propertyCallBack: (BluetoothGattCharacteristic) -> Unit
    ) {
        if ((characteristic.properties and propertyType) != 0) {
            properties.add(propertyName)
            propertyAction.isEnabled = true
            propertyAction.alpha = 1f
            propertyAction.setOnClickListener {
                propertyCallBack.invoke(characteristic)
            }
        }
    }

    private fun addPropertyNotificationFromCharacteristic(
        characteristic: BluetoothGattCharacteristic,
        properties: ArrayList<String>,
        propertyAction: Button,
        propertyCallBack: (BluetoothGattCharacteristic, Boolean) -> Unit
    ) {
        if ((characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
            properties.add("Notifier")
            propertyAction.isEnabled = true
            propertyAction.alpha = 1f
            val isNotificationEnable = characteristic.descriptors.any {
                it.value?.contentEquals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) ?: false
            }
            if (isNotificationEnable) {
                propertyAction.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                propertyAction.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            } else {
                propertyAction.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
                propertyAction.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }
            propertyAction.setOnClickListener {
                propertyCallBack.invoke(characteristic, !isNotificationEnable)
            }
        }
    }

    fun updateFromChangedCharacteristic(characteristic: BluetoothGattCharacteristic?) {
        serviceList.forEach {
            val index = it.items.indexOf(characteristic)
            if(index != -1) {
                it.items.removeAt(index)
                it.items.add(index, characteristic)
            }
        }
    }

    override fun onBindGroupViewHolder(
        holder: ServiceViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>
    ) {
        val title =
            BLEUUIDAttributes.getBLEAttributeFromUUID(group.title).title
        holder.serviceUUID.text = group.title
        holder.serviceName.text= title

    }

}