package com.patana93.ntt_data_interview

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Convert LocalDate into a String Date with yyyy-MM-dd format
 * @return String date
 */
fun LocalDate.getDateFormatted(): String{
    //Date format ex: 2021-12-30
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return this.format(formatter)
}