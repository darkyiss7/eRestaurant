package com.isen.irestaurant.activities

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.isen.irestaurant.BluetoothLeService
import com.isen.irestaurant.R
import com.isen.irestaurant.activities.BLEScanActivity.Companion.ITEM_KEY
import com.isen.irestaurant.databinding.ActivityBledeviceBinding


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
                val bluetoothDevice = intent.extras!!.getParcelable<BluetoothDevice>(ITEM_KEY)
                if (bluetoothDevice != null) {
                    bluetooth.connect(bluetoothDevice.address)
                }
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            bluetoothService = null
        }
    }
    private val gattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothLeService.ACTION_GATT_CONNECTED -> {
                    //connected = true
                    updateConnectionState(getString(R.string.connected))
                }
                BluetoothLeService.ACTION_GATT_DISCONNECTED -> {
                    //connected = false
                    updateConnectionState(getString(R.string.disconnected))
                }
            }
        }
    }

    private fun updateConnectionState(string: String) {
        binding.statusTextView.text=string
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())
        if (bluetoothService != null) {
            val bluetoothDevice = intent.extras!!.getParcelable<BluetoothDevice>(ITEM_KEY)
            val result = bluetoothDevice?.address?.let { bluetoothService!!.connect(it) }
            Log.d("BLEDeviceActivity", "Connect request result=$result")
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(gattUpdateReceiver)
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter? {
        return IntentFilter().apply {
            addAction(BluetoothLeService.ACTION_GATT_CONNECTED)
            addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBledeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bluetoothDevice = intent.extras!!.getParcelable<BluetoothDevice>(ITEM_KEY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        )
            if (bluetoothDevice != null) {
                binding.nemeTextView.text = bluetoothDevice.name
            }
        val gattServiceIntent = Intent(this, BluetoothLeService::class.java)
        bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
}