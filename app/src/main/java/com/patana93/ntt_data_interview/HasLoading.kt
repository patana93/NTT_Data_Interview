package com.patana93.ntt_data_interview

interface HasLoading {
    /**
     * Add imageView and progressBar to indicate the loading of the data from network
     */
    fun addLoadingUI()
    /**
     * Remove imageView and progressBar
     */
    fun removeLoadingUI()
}