package com.isen.irestaurant

import android.app.Service
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Intent
import android.os.IBinder
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class BLEService(title: String, characteristics: MutableList<BluetoothGattCharacteristic>) :
    ExpandableGroup<BluetoothGattCharacteristic>(title, characteristics)