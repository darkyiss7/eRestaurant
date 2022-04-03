package com.isen.irestaurant.activities

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.*
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.isen.irestaurant.BLEService
import com.isen.irestaurant.BluetoothLeService
import com.isen.irestaurant.R
import com.isen.irestaurant.activities.BLEScanActivity.Companion.ITEM_KEY
import com.isen.irestaurant.adapter.BleServiceAdapter
import com.isen.irestaurant.databinding.ActivityBledeviceBinding
import com.isen.irestaurant.objects.BLEConnexionState

@SuppressLint("MissingPermission")
class BLEDeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBledeviceBinding
    private var bluetoothGatt : BluetoothGatt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBledeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val device = intent.getParcelableExtra<BluetoothDevice?>(ITEM_KEY)
        binding.nemeTextView.text = device?.name ?: "Nom inconnu"
        binding.statusTextView.text= getString(R.string.disconnected)

        connectToDevice(device)
    }


    private fun connectToDevice(device: BluetoothDevice?) {
        bluetoothGatt = device?.connectGatt(this, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                onConnectionStateChange(gatt, newState)
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)
                val bleService= gatt?.services?.map { BLEService(it.uuid.toString(), it.characteristics) } ?: arrayListOf()
                val adapter= BleServiceAdapter(bleService as MutableList<BLEService>)
                runOnUiThread {
                    binding.characteristicView.layoutManager = LinearLayoutManager(this@BLEDeviceActivity)
                    binding.characteristicView.adapter = adapter
                }
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                super.onCharacteristicRead(gatt, characteristic, status)
            }
        })
        bluetoothGatt?.connect()
    }

    private fun onConnectionStateChange (gatt: BluetoothGatt?, newState: Int){
        val state = if(newState == BluetoothProfile.STATE_CONNECTED) {
            gatt?.discoverServices()
            getString(R.string.connected)
        }else{
            getString(R.string.disconnected)
        }
        runOnUiThread {
            binding.statusTextView.text = state
        }
    }

    override fun onStop() {
        super.onStop()
        closeBluetoothGatt()
    }

    private fun closeBluetoothGatt() {
        bluetoothGatt?.close()
        bluetoothGatt = null
    }
}