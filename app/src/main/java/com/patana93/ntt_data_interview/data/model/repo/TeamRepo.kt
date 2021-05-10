package com.patana93.ntt_data_interview.data.model.repo

import com.patana93.ntt_data_interview.data.model.Team

/**
 * Singleton that store all teams
 */
object TeamRepo {
    var teamRepo: ArrayList<Team> = arrayListOf()
}