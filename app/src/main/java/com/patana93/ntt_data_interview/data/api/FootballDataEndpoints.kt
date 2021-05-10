package com.patana93.ntt_data_interview.data.api

import Competition
import com.patana93.ntt_data_interview.data.model.Matches
import com.patana93.ntt_data_interview.data.model.TeamApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface FootballDataEndpoints {

    @GET("competitions/2019/matches?status=FINISHED")
    suspend fun getMatches(@Header("X-Auth-Token") key: String): Response<Matches>?

    @GET("competitions/2019/teams")
    suspend fun getTeams(@Header("X-Auth-Token") key: String): Response<TeamApi>?

    @GET("competitions/2019")
    suspend fun getCompetition(@Header("X-Auth-Token") key: String): Response<Competition>?
}