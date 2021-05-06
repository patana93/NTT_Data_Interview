package com.patana93.ntt_data_interview.data.model

import com.google.gson.annotations.SerializedName

class MatchApi(
    @SerializedName("matches")
    val matchList: List<Map<String,Any>>
)