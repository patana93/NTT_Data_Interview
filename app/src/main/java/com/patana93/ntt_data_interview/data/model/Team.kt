package com.patana93.ntt_data_interview.data.model

import android.widget.ImageView

data class Team (val name: String, val crestURL: String, var numbersOfWinInRangeDate: Int = 0){
    fun addWin() = numbersOfWinInRangeDate++
}