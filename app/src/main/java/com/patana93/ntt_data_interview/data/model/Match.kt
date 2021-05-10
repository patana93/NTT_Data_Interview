package com.patana93.ntt_data_interview.data.model

import com.google.gson.annotations.SerializedName

/**
 * Match details
 * @param id
 * @param utcDate
 * @param score
 * @param homeTeam
 * @param awayTeam
 */
data class Match (
	@SerializedName("id") val id : Int,
	@SerializedName("utcDate") val utcDate : String,
	@SerializedName("score") val score : Score,
	@SerializedName("homeTeam") val homeTeam : HomeTeam,
	@SerializedName("awayTeam") val awayTeam : AwayTeam
)