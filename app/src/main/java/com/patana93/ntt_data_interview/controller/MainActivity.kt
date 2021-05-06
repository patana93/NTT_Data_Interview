package com.patana93.ntt_data_interview.controller

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.internal.LinkedTreeMap
import com.patana93.ntt_data_interview.R
import com.patana93.ntt_data_interview.controller.adapter.TeamsMostWinAdapter
import com.patana93.ntt_data_interview.data.api.FootballDataEndpoints
import com.patana93.ntt_data_interview.data.api.ServiceBuilder
import com.patana93.ntt_data_interview.data.model.MatchApi
import com.patana93.ntt_data_interview.data.model.Team
import com.patana93.ntt_data_interview.data.model.TeamApi
import com.patana93.ntt_data_interview.data.model.TeamRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var call: Call<MatchApi>
    private lateinit var call2: Call<TeamApi>
    private lateinit var request: FootballDataEndpoints
    private lateinit var resultRecycler: RecyclerView
    private lateinit var titleTextView: TextView
    private lateinit var shadowImageView: ImageView
    private lateinit var loadDataProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        request = ServiceBuilder.buildService(FootballDataEndpoints::class.java)

        resultRecycler = findViewById(R.id.teamsRecyclerView)
        titleTextView = findViewById(R.id.titleTextView)
        shadowImageView = findViewById(R.id.shadowImageView)
        loadDataProgressBar = findViewById(R.id.loadDataProgressBar)

        fetchTeams()

        var currentDate = LocalDate.now()
        //Date format ex: 2021-12-30
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var currentDateString = currentDate.format(formatter)
        val dayToSubtract = 9
        var dateTenDayBefore = currentDate.minusDays(dayToSubtract.toLong())
        var currentDateTenDayBeforeString = dateTenDayBefore.format(formatter)
        fetchMostWinnerInDataRange(currentDateTenDayBeforeString, currentDateString){}

        currentDate = dateTenDayBefore.minusDays(1)
        currentDateString = currentDate.format(formatter)
        dateTenDayBefore = currentDate.minusDays(dayToSubtract.toLong())
        currentDateTenDayBeforeString = dateTenDayBefore.format(formatter)
        fetchMostWinnerInDataRange(currentDateTenDayBeforeString, currentDateString){}

        currentDate = dateTenDayBefore.minusDays(1)
        currentDateString = currentDate.format(formatter)
        dateTenDayBefore = currentDate.minusDays((dayToSubtract + 1).toLong())
        currentDateTenDayBeforeString = dateTenDayBefore.format(formatter)
        fetchMostWinnerInDataRange(currentDateTenDayBeforeString, currentDateString) {
            shadowImageView.visibility = View.GONE
            loadDataProgressBar.visibility = View.GONE
        }

        titleTextView.text = "Team/s with most wins last 30 days in Serie A\nfrom $currentDateTenDayBeforeString to ${LocalDate.now()}"
    }

    private fun fetchMostWinnerInDataRange(dateFrom: String, dateTo: String, callback: () -> Unit){
        call = request.getMatches(
            getString(R.string.api_key),
            "SA",
            "FINISHED",
            dateFrom = dateFrom,
            dateTo = dateTo
        )
        call.enqueue(object : Callback<MatchApi> {
            override fun onResponse(call: Call<MatchApi>, response: Response<MatchApi>) {
                if (response.isSuccessful) {
                    val matchList = response.body()!!.matchList
                    for (match in matchList) {
                        val score = match["score"] as LinkedTreeMap<String, Any>
                        val home = match["homeTeam"] as LinkedTreeMap<String, Any>
                        val away = match["awayTeam"] as LinkedTreeMap<String, Any>

                        when (score["winner"]) {
                            "HOME_TEAM" -> TeamRepo.teamRepo.find { it.name == home["name"] }?.addWin()
                            "AWAY_TEAM" -> TeamRepo.teamRepo.find { it.name == away["name"] }?.addWin()
                        }
                    }

                    val maxWinner =
                        TeamRepo.teamRepo.maxByOrNull { it.numbersOfWinInRangeDate }?.numbersOfWinInRangeDate
                    maxWinner?.let {
                        val result = TeamRepo.teamRepo.filter { it.numbersOfWinInRangeDate == maxWinner}
                        resultRecycler.layoutManager = GridLayoutManager(this@MainActivity, 1)
                        val resultAdapter = TeamsMostWinAdapter(this@MainActivity, result)
                        resultRecycler.adapter = resultAdapter

                        callback()
                    }
                } else {
                    println("ERROR: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<MatchApi>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchTeams(){
        call2 = request.getTeams(getString(R.string.api_key))
        call2.enqueue(object : Callback<TeamApi> {
            override fun onResponse(call: Call<TeamApi>, response: Response<TeamApi>) {
                if (response.isSuccessful) {
                    val teamsList = response.body()!!.teams
                    for (team in teamsList) {
                        TeamRepo.teamRepo.add(Team(team["name"] as String, team["crestUrl"] as String))
                    }
                } else {
                    println("ERROR: ${response.errorBody()}")
                }
                println(TeamRepo.teamRepo.joinToString())
            }
            override fun onFailure(call: Call<TeamApi>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}