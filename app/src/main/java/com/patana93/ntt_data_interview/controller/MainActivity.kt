package com.patana93.ntt_data_interview.controller

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

import com.patana93.ntt_data_interview.R


const val CONN_ERR = "CONN_ERR"
const val CONN_INFO = "CONN_INFO"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController)


        /*request = ServiceBuilder.buildService(FootballDataEndpoints::class.java)

        resultRecycler = findViewById(R.id.teamsRecyclerView)
        titleTextView = findViewById(R.id.titleTextView)
        shadowImageView = findViewById(R.id.shadowImageView)
        loadDataProgressBar = findViewById(R.id.loadDataProgressBar)

        resultRecycler.layoutManager = GridLayoutManager(this@MainActivity, 1)
        resultAdapter = TeamsMostWinAdapter(this@MainActivity, result)
        resultRecycler.adapter = resultAdapter

        GlobalScope.launch(Dispatchers.IO) {
            if(isNetworkConnected()){
                fetchTeams()
                val currentDate = LocalDate.now()

                var (startDate, endDate) = Pair(currentDate.minusDays(9), currentDate)
                fetchMostWinnerInDataRange(startDate.getDateFormatted(), endDate.getDateFormatted())

                endDate = startDate.minusDays(1)
                startDate = endDate.minusDays(9)
                fetchMostWinnerInDataRange(startDate.getDateFormatted(), endDate.getDateFormatted())

                endDate = startDate.minusDays(1)
                startDate = endDate.minusDays(9)
                fetchMostWinnerInDataRange(startDate.getDateFormatted(), endDate.getDateFormatted())

                withContext(Dispatchers.Main){
                    val maxWinner =
                            TeamRepo.teamRepo.maxByOrNull { it.numbersOfWinInRangeDate }?.numbersOfWinInRangeDate
                    maxWinner?.let {
                        updateUI(maxWinner)
                    }
                }
            } else {
                withContext(Dispatchers.Main){
                    Toast.makeText(this@MainActivity, getString(R.string.conn_error_check_conn), Toast.LENGTH_LONG).show()
                    updateUI(-1)
                }
            }
        }*/
    }
/*
    private fun updateUI(maxWinner: Int?) {
        result.clear()
        result.addAll(TeamRepo.teamRepo.filter { it.numbersOfWinInRangeDate == maxWinner })
        resultAdapter.notifyDataSetChanged()
        shadowImageView.visibility = View.GONE
        loadDataProgressBar.visibility = View.GONE
        titleTextView.text = "Team/s with most wins last 30 days in Serie A\nfrom ${LocalDate.now().minusDays(29)} to ${LocalDate.now()}"
    }

    private suspend fun fetchMostWinnerInDataRange(dateFrom: String, dateTo: String){
        val response = request.getMatches(
            getString(R.string.api_key),
            "SA",
            "FINISHED",
            dateFrom = dateFrom,
            dateTo = dateTo
        )
        if (response?.isSuccessful == true) {
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
        } else {
            Toast.makeText(this@MainActivity, getString(R.string.conn_error_check_conn), Toast.LENGTH_LONG).show()
            Log.e(CONN_ERR, "${response?.errorBody()}")
        }
    }

    private suspend fun fetchTeams() {
        var responseTeam: Response<TeamApi>? = null
        responseTeam = request.getTeams(getString(R.string.api_key))
        if (responseTeam?.isSuccessful == true) {
            val teamsList = responseTeam.body()!!.teams
            for (team in teamsList) {
                TeamRepo.teamRepo.add(Team(team["name"] as String, team["crestUrl"] as String))
            }
        } else {
            Toast.makeText(this@MainActivity, getString(R.string.conn_error_check_conn), Toast.LENGTH_LONG).show()
            Log.e(CONN_ERR, "${responseTeam?.errorBody()}")
        }
        Log.i(CONN_INFO, TeamRepo.teamRepo.joinToString())
    }

*/
    private fun isNetworkConnected(): Boolean {
        //TODO Check https://www.raywenderlich.com/6994782-android-networking-with-kotlin-tutorial-getting-started
        //1
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //2
        return if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetworkInfo?.isConnected
            activeNetwork != null && activeNetwork
        } else {
            val activeNetwork = connectivityManager.activeNetwork
            //3
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            //4
            networkCapabilities != null &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}