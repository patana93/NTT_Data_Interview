package com.patana93.ntt_data_interview.data.model

import com.google.gson.annotations.SerializedName

/**
 * Teams List
 * @param teams
 */
data class Teams (
    @SerializedName("teams") val teams: ArrayList<Team>
)