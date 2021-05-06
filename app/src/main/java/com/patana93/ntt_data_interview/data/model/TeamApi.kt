package com.patana93.ntt_data_interview.data.model

import com.google.gson.annotations.SerializedName

class TeamApi (
    @SerializedName("teams")
    val teams: List<Map<String,Any>>
)