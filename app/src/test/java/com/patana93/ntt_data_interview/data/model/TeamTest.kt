package com.patana93.ntt_data_interview.data.model

import org.junit.Assert.*
import org.junit.Test

class TeamTest{

    @Test
    fun check_increase_win_counter_of_a_team_return_1() {
        val team = Team("FakeTeam", "")

        team.addWin()
        assertEquals(team.numbersOfWinInRangeDate, 1)
    }

}