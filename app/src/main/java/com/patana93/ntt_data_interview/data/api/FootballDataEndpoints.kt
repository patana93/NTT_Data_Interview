package com.patana93.ntt_data_interview.data.api

import Competition
import com.patana93.ntt_data_interview.data.model.Matches
import com.patana93.ntt_data_interview.data.model.Teams
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Endpoints of API
 */
interface FootballDataEndpoints {

    /**
     * GET list of matches for serie A that are flagged as FINISHED
     */
    @GET("competitions/2019/matches?status=FINISHED")
    suspend fun getMatches(@Header("X-Auth-Token") key: String): Response<Matches>?

    /**
     * GET all teams of Serie A
     */
    @GET("competitions/2019/teams")
    suspend fun getTeams(@Header("X-Auth-Token") key: String): Response<Teams>?

    /**
     * GET Serie A detail
     */
    @GET("competitions/2019")
    suspend fun getCompetition(@Header("X-Auth-Token") key: String): Response<Competition>?
}