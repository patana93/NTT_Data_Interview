package com.patana93.ntt_data_interview

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.time.LocalDate

object Utils {
    /**
     * Check if from the last match in the competition have passed more than 30 days
     * @param lastDateMatch last date of a match
     * @return Return true if there is a match in the last 30 days, false instead
     */
    fun checkMatchExistLast30Days(lastDateMatch: LocalDate): Boolean {
        return LocalDate.now().minusDays(29).isBefore(lastDateMatch)
    }

    /**
     * Check if the connection is ready
     */
    fun isNetworkConnected(context: Context): Boolean {
        // Retrieving an instance of ConnectivityManager from the current application context.
        // ConnectivityManager is used to query the state of network.
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Getting a reference to the active network the device is using based on API Version
        return if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetworkInfo?.isConnected
            activeNetwork != null && activeNetwork
        } else {
            val activeNetwork = connectivityManager.activeNetwork
            // Getting the capabilities of the active network
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            // Checking if the active network can reach the Internet
            networkCapabilities != null &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    }
}