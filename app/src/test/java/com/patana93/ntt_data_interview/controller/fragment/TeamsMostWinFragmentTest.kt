package com.patana93.ntt_data_interview.controller.fragment

import com.patana93.ntt_data_interview.Utils
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class TeamsMostWinFragmentTest{

    @Test
    fun check_match_exist_last_30_days_return_true() {
        val lastMatch = LocalDate.now().minusDays(10)

        val result = Utils.checkMatchExistLast30Days(lastDateMatch = lastMatch)
        assertTrue(result)
    }

    @Test
    fun check_match_exist_last_30_days_return_false() {
        val lastMatch = LocalDate.now().minusDays(60)

        val result = Utils.checkMatchExistLast30Days(lastDateMatch = lastMatch)
        assertFalse(result)
    }

}