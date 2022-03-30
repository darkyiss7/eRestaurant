package com.isen.irestaurant.activities

import android.bluetooth.le.ScanResult
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.isen.irestaurant.BluetoothLeService
import com.isen.irestaurant.R
import com.isen.irestaurant.activities.BLEScanActivity.Companion.ITEM_KEY
import com.isen.irestaurant.databinding.ActivityBledeviceBinding
import com.isen.irestaurant.databinding.ActivityBlescanBinding
import com.isen.irestaurant.databinding.ActivityCategoryBinding
import com.isen.irestaurant.objects.Item

class BLEDeviceActivity : AppCompatActivity() {
    private var bluetoothService : BluetoothLeService? = null
    private lateinit var binding : ActivityBledeviceBinding
    // Code to manage Service lifecycle.
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            componentName: ComponentName,
            service: IBinder
        ) {
            bluetoothService = (service as BluetoothLeService.LocalBinder).getService()
            bluetoothService?.let { bluetooth ->
                if (!bluetooth.initialize()) {
                    Log.e(TAG, "Unable to initialize Bluetooth")
                    finish()
                }
                var addresse = intent.getStringExtra(ITEM_KEY)
                if (addresse != null) {
                    bluetooth.connect(addresse)
                }
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            bluetoothService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBledeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gattServiceIntent = Intent(this, BluetoothLeService::class.java)
        bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
}