package com.patana93.ntt_data_interview.controller.fragment

import com.patana93.ntt_data_interview.data.model.Match
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.patana93.ntt_data_interview.R
import com.patana93.ntt_data_interview.controller.CONN_ERR
import com.patana93.ntt_data_interview.controller.CONN_INFO
import com.patana93.ntt_data_interview.controller.adapter.TeamsMostWinRecyclerViewAdapter
import com.patana93.ntt_data_interview.data.api.FootballDataEndpoints
import com.patana93.ntt_data_interview.data.api.ServiceBuilder
import com.patana93.ntt_data_interview.data.model.Matches
import com.patana93.ntt_data_interview.data.model.Team
import com.patana93.ntt_data_interview.data.model.TeamApi
import com.patana93.ntt_data_interview.data.model.TeamRepo
import com.patana93.ntt_data_interview.getDateFormatted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class TeamsMostWinFragment : Fragment() {
    private lateinit var request: FootballDataEndpoints
    private lateinit var resultRecycler: RecyclerView
    private lateinit var titleTextView: TextView
    private lateinit var shadowImageView: ImageView
    private lateinit var loadDataProgressBar: ProgressBar
    private var result = arrayListOf<Team>()
    private lateinit var resultAdapter: TeamsMostWinRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_teams_most_win_list, container, false)
        request = ServiceBuilder.buildService(FootballDataEndpoints::class.java)

        resultRecycler = view.findViewById(R.id.teamsRecyclerView)
        titleTextView = view.findViewById(R.id.titleTextView)
        shadowImageView = view.findViewById(R.id.shadowImageView)
        loadDataProgressBar = view.findViewById(R.id.loadDataProgressBar)

        resultRecycler.layoutManager = GridLayoutManager(context, 1)
        resultAdapter = TeamsMostWinRecyclerViewAdapter(requireContext(), result)
        resultRecycler.adapter = resultAdapter

        GlobalScope.launch(Dispatchers.IO) {
            if(isNetworkConnected()){
                fetchTeams()
                val currentDate = LocalDate.now()

                var (startDate, endDate) = Pair(currentDate.minusDays(9), currentDate)
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
                    Toast.makeText(
                        context,
                        getString(R.string.conn_error_check_conn),
                        Toast.LENGTH_LONG
                    ).show()
                    updateUI(-1)
                }
            }
        }
        return view
    }

    private fun updateUI(maxWinner: Int?) {
        result.clear()
        result.addAll(TeamRepo.teamRepo.filter { it.numbersOfWinInRangeDate == maxWinner })
        resultAdapter.notifyDataSetChanged()
        shadowImageView.visibility = View.GONE
        loadDataProgressBar.visibility = View.GONE
        titleTextView.text = "Team/s with most wins last 30 days in Serie A\nfrom ${LocalDate.now().minusDays(
            29
        )} to ${LocalDate.now()}"
    }

    private suspend fun fetchMostWinnerInDataRange(dateFrom: String, dateTo: String){
        val response: Response<Matches>? = request.getMatches(getString(R.string.api_key))
        if (response?.isSuccessful == true) {
            val matchList = response.body()!!.matches

            var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz")
            formatter = formatter.withLocale(Locale.getDefault())
            val lastDateMatch = LocalDate.parse(matchList.last().utcDate, formatter)

            //TODO ADD TEST HERE. TRANSFORM THIS CHECK IN A FUNCTION
            val today = LocalDate.now()
            val endDate = if(today.minusDays(29).isBefore(lastDateMatch)){
                LocalDate.now()
            } else {
                lastDateMatch
            }
            val startDate = endDate.minusDays(29)

            val filteredMatches = arrayListOf<Match>()
            for(match in matchList){
                val curr = LocalDate.parse(match.utcDate, formatter)
                if(curr.isBefore(endDate) && curr.isAfter(startDate)){
                    filteredMatches.add(match)
                }
            }

            for (match in filteredMatches) {
                val score = match.score
                val home = match.homeTeam
                val away = match.awayTeam

                when (score.winner) {
                    "HOME_TEAM" -> TeamRepo.teamRepo.find { it.name == home.name }?.addWin()
                    "AWAY_TEAM" -> TeamRepo.teamRepo.find { it.name == away.name }?.addWin()
                }
            }
        } else {
            withContext(Dispatchers.Main){
                Toast.makeText(
                    context,
                    getString(R.string.conn_error_check_conn),
                    Toast.LENGTH_LONG
                ).show()
                Log.e(CONN_ERR, "${response?.errorBody()}")
            }
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
            withContext(Dispatchers.Main){
                Toast.makeText(
                    context,
                    getString(R.string.conn_error_check_conn),
                    Toast.LENGTH_LONG
                ).show()
                Log.e(CONN_ERR, "${responseTeam?.errorBody()}")
            }
        }
        Log.i(CONN_INFO, TeamRepo.teamRepo.joinToString())
    }


    private fun isNetworkConnected(): Boolean {
        //TODO Check https://www.raywenderlich.com/6994782-android-networking-with-kotlin-tutorial-getting-started
        //1
        val connectivityManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
}