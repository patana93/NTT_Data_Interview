package com.patana93.ntt_data_interview.data.model

import com.google.gson.annotations.SerializedName

data class Matches (
    @SerializedName("matches") val matches : List<Match>
)