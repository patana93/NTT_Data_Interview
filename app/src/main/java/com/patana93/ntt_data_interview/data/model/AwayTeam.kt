package com.patana93.ntt_data_interview.data.model

import com.google.gson.annotations.SerializedName

data class AwayTeam (
	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String
)