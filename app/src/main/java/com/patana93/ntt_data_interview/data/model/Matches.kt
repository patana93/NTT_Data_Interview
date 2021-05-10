package com.patana93.ntt_data_interview.data.model

import com.google.gson.annotations.SerializedName

/**
 * List of matches
 * @param matches
 */
data class Matches (
    @SerializedName("matches") val matches : ArrayList<Match>
)