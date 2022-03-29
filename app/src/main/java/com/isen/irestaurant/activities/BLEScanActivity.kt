package com.isen.irestaurant.activities

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.isen.irestaurant.R
import com.isen.irestaurant.adapter.BleAdapter
import com.isen.irestaurant.databinding.ActivityBlescanBinding


class BLEScanActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBlescanBinding
    private var isScanning = false
    private val listeBle = ArrayList<ScanResult>()

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var i = 0
        val handler = Handler()
        val bleAdapter : BleAdapter
        super.onCreate(savedInstanceState)
        binding = ActivityBlescanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bleScanList.layoutManager = LinearLayoutManager(this)
        binding.bleScanList.adapter = BleAdapter(listeBle)
        title = "Bluetooth";
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        when{
            bluetoothAdapter?.isEnabled == true ->{
                startLeScanBLEWithPermission(true)

            }

            bluetoothAdapter != null ->
                askBluetoothPermission()
            else -> {
                displayBLEUnAvailable()
            }
        }
        binding.bleScanImg.setOnClickListener {
            startLeScanBLEWithPermission(!isScanning)
        }
        binding.bleScanText.setOnClickListener {
            startLeScanBLEWithPermission(!isScanning)
        }
    }

    override fun onStop() {
        super.onStop()
        startLeScanBLEWithPermission(false)
    }
    private fun startLeScanBLEWithPermission(enable : Boolean){
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        )==PackageManager.PERMISSION_GRANTED
        ){
            startLeScanBLE(enable)
        }else{
            ActivityCompat.requestPermissions(this, getAllPermissions(), ALL_PERMISSION_REQUEST_CODE)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLeScanBLE(enable : Boolean) {
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            if (enable){
                isScanning = true
                startScan(scanCallback)
            }else{
                isScanning = false
                stopScan(scanCallback)
            }
            handlePlayPause()
        }
    }
    private fun checkAllPermissionGranted():Boolean{
        return getAllPermissions().all{ permission ->
            ActivityCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getAllPermissions(): Array<String> {
        return if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.BLUETOOTH_SCAN,
                android.Manifest.permission.BLUETOOTH_CONNECT
            )
        }else{
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun displayBLEUnAvailable() {
        binding.bleScanImg.isVisible = false
        binding.bleScanText.text=getString(R.string.ble_scan_error)
        binding.bleScanProgression.isVisible = false
    }
    private fun askBluetoothPermission(){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ){
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        }
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Log.d("BLEScanActivity","result : ${result.device.address}, rssi : ${result.rssi}")
            (binding.bleScanList.adapter as BleAdapter).apply {
                addToList(result)
                notifyDataSetChanged()
            }
        }
    }


    private fun handlePlayPause(){

            if (isScanning){

                binding.bleScanImg.setImageResource(R.drawable.ic_baseline_pause_24)
                binding.bleScanText.text=getString(R.string.ble_scan_pause)
                binding.bleScanProgression.isIndeterminate = true
            }else{
                binding.bleScanImg.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                binding.bleScanText.text=getString(R.string.ble_scan_play)
                binding.bleScanProgression.isIndeterminate = false
            }

    }
    companion object {
        private const val ALL_PERMISSION_REQUEST_CODE = 1
        private const val ENABLE_BLUETOOTH_REQUEST_CODE = 1

    }
}