package com.patana93.ntt_data_interview.data.model

import com.google.gson.annotations.SerializedName

/**
 * Home team details
 * @param id
 * @param name
 */
data class HomeTeam (
	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String
)