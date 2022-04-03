package com.isen.irestaurant.objects

import android.bluetooth.BluetoothProfile
import androidx.annotation.StringRes
import com.isen.irestaurant.R

enum class BLEConnexionState (val state: Int,@StringRes val text: Int) {
    STATE_CONNECTING(BluetoothProfile.STATE_CONNECTING, R.string.connexion),
    STATE_CONNECTED(BluetoothProfile.STATE_CONNECTED, R.string.connected),
    STATE_DISCONNECTED(BluetoothProfile.STATE_DISCONNECTED, R.string.disconnected),
    STATE_DISCONNECTING(BluetoothProfile.STATE_DISCONNECTING, R.string.disconnected);

    companion object {
        fun getBLEConnexionStateFromState(state: Int) = values().firstOrNull {
            it.state == state
        }
    }
}