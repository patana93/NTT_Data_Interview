package com.patana93.ntt_data_interview.data.model

import com.google.gson.annotations.SerializedName

/**
 * Team data class
 * @param name Name of the team
 * @param crestURL Crest of the team
 * @param numbersOfWinInRangeDate Number of winner for a team in the last 30 days
 */
data class Team (
    @SerializedName("name") val name: String,
    @SerializedName("crestUrl") val crestURL: String,
    var numbersOfWinInRangeDate: Int = 0) {

    /**
     * Increase the number of win for this team
     */
    fun addWin() = numbersOfWinInRangeDate++

}