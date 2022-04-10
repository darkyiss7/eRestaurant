package com.isen.irestaurant.activities

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.*
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.isen.irestaurant.BLEService
import com.isen.irestaurant.BluetoothLeService
import com.isen.irestaurant.R
import com.isen.irestaurant.activities.BLEScanActivity.Companion.ITEM_KEY
import com.isen.irestaurant.adapter.BleServiceAdapter
import com.isen.irestaurant.databinding.ActivityBledeviceBinding
import com.isen.irestaurant.objects.BLEConnexionState
import java.util.*

@SuppressLint("MissingPermission")
class BLEDeviceActivity : AppCompatActivity() {
    private var isScanning = false
    private lateinit var binding: ActivityBledeviceBinding
    private var bluetoothGatt : BluetoothGatt? = null
    private var timer: Timer? = null
    private lateinit var adapter: BleServiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBledeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val device = intent.getParcelableExtra<BluetoothDevice?>(ITEM_KEY)
        binding.nemeTextView.text = device?.name ?: getString(R.string.ble_scan_default_name)
        binding.statusTextView.text= getString(R.string.connexion)

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
                onServicesDiscovered(gatt)

            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                super.onCharacteristicRead(gatt, characteristic, status)
                runOnUiThread {
                    adapter.updateFromChangedCharacteristic(characteristic)
                    adapter.notifyDataSetChanged()
                }
            }
        })
        isScanning = true
        bluetoothGatt?.connect()

    }
    private fun onServicesDiscovered(gatt: BluetoothGatt?) {
        val bleService= gatt?.services?.map { BLEService(it.uuid.toString(), it.characteristics) } ?: arrayListOf()
         adapter= BleServiceAdapter(this,bleService as MutableList<BLEService>,{ characteristic ->
            if (gatt != null) {
                gatt.readCharacteristic(characteristic)
            }
        },
            { characteristic -> gatt?.let { writeIntoCharacteristic(it, characteristic) } },
            { characteristic, enable ->
                gatt?.let {
                    toggleNotificationOnCharacteristic(
                        it,
                        characteristic,
                        enable
                    )
                }
            })
        runOnUiThread {
            isScanning = false
            binding.characteristicView.layoutManager = LinearLayoutManager(this@BLEDeviceActivity)
            binding.characteristicView.adapter = adapter
            handlePlayPause()
        }
    }
    private fun toggleNotificationOnCharacteristic(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        enable: Boolean
    ) {
        characteristic.descriptors.forEach {
            it.value =
                if (enable) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(it)
        }
        gatt.setCharacteristicNotification(characteristic, enable)
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                gatt.readCharacteristic(characteristic)
            }
        }, 0, 1000)
    }
    private fun writeIntoCharacteristic(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        runOnUiThread {
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(16, 0, 16, 0)
            input.layoutParams = params

            AlertDialog.Builder(this@BLEDeviceActivity)
                .setTitle(R.string.ble_device_write_characteristic_title)
                .setView(input)
                .setPositiveButton(R.string.ble_device_write_characteristic_confirm) { _, _ ->
                    characteristic.value = input.text.toString().toByteArray()
                    gatt.writeCharacteristic(characteristic)
                    gatt.readCharacteristic(characteristic)
                }
                .setNegativeButton(R.string.ble_device_write_characteristic_cancel) { dialog, _ -> dialog.cancel() }
                .create()
                .show()
        }
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
        handlePlayPause()
    }

    override fun onStop() {
        super.onStop()
        closeBluetoothGatt()
    }

    private fun closeBluetoothGatt() {
        bluetoothGatt?.close()
        bluetoothGatt = null
    }
    private fun handlePlayPause(){
        if (isScanning){
            binding.progressBar2.isIndeterminate = true
        }else{
            binding.progressBar2.isIndeterminate = false
            binding.progressBar2.isVisible = false
        }
    }

}