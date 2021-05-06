package com.patana93.ntt_data_interview.data.api

import com.patana93.ntt_data_interview.data.model.MatchApi
import com.patana93.ntt_data_interview.data.model.TeamApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FootballDataEndpoints {

    @GET("matches")
    fun getMatches(@Header("X-Auth-Token") key: String,
                   @Query("competitions") competitions: String,
                   @Query("status") status: String,
                   @Query("dateFrom") dateFrom: String,
                   @Query("dateTo") dateTo: String): Call<MatchApi>

    @GET("competitions/2019/teams")
    fun getTeams(@Header("X-Auth-Token") key: String): Call<TeamApi>

}