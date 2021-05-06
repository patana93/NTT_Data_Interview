package com.patana93.ntt_data_interview.data.model

data class Team (var name: String, var numbersOfWinInRangeDate: Int = 0){
    fun addWin() = numbersOfWinInRangeDate++
}