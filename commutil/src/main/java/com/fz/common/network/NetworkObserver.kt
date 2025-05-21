package com.fz.common.network


import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import android.util.Log
import androidx.annotation.MainThread
import com.fz.common.network.NetworkObserver.Listener

private const val TAG = "NetworkObserver"

/** Create a new [NetworkObserver]. */
fun NetworkObserver(
    context: Context,
    listener: Listener,
): NetworkObserver {
    val connectivityManager: ConnectivityManager? =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (connectivityManager == null) {
        Log.e(TAG, "Unable to register network observer.")
        return EmptyNetworkObserver()
    }

    return try {
        RealNetworkObserver(connectivityManager, listener)
    } catch (e: Exception) {
        Log.e(TAG, "Failed to register network observer")
        EmptyNetworkObserver()
    }
}

/**
 * Observes the device's network state and calls [Listener] if any state changes occur.
 *
 * This class provides a raw stream of updates from the network APIs. The [Listener] can be
 * called multiple times for the same network state.
 */
interface NetworkObserver {

    /** Synchronously checks if the device is online. */
    val isOnline: Boolean

    /** Stop observing network changes. */
    fun shutdown()

    /** Start observing network changes. */
    fun start()

    /** Calls [onConnectivityChange] when a connectivity change event occurs. */
    fun interface Listener {

        @MainThread
        fun onConnectivityChange(isOnline: Boolean)
    }
}

internal class EmptyNetworkObserver : NetworkObserver {

    override val isOnline get() = true

    override fun shutdown() {}
    override fun start() {

    }
}

@SuppressLint("MissingPermission")
@Suppress("DEPRECATION") // TODO: Remove uses of 'allNetworks'.
private class RealNetworkObserver(
    private val connectivityManager: ConnectivityManager,
    private val listener: Listener,
) : NetworkObserver {

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) = onConnectivityChange(network, true)
        override fun onLost(network: Network) = onConnectivityChange(network, false)
    }

    override val isOnline: Boolean
        get() = connectivityManager.allNetworks.any { it.isOnline() }

    override fun shutdown() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    override fun start() {
        val request = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    private fun onConnectivityChange(network: Network, isOnline: Boolean) {
        val isAnyOnline = connectivityManager.allNetworks.any {
            if (it == network) {
                // Don't trust the network capabilities for the network that just changed.
                isOnline
            } else {
                it.isOnline()
            }
        }
        listener.onConnectivityChange(isAnyOnline)
    }

    private fun Network.isOnline(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(this)
        return capabilities != null && capabilities.hasCapability(NET_CAPABILITY_INTERNET)
    }
}