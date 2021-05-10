package com.patana93.ntt_data_interview.data.model

import com.google.gson.annotations.SerializedName

/**
 * Score details
 * @param winner
 */
data class Score (
	@SerializedName("winner") val winner : String,
)