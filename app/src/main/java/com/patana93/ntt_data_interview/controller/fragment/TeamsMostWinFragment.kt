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
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.patana93.ntt_data_interview.HasLoading
import com.patana93.ntt_data_interview.R
import com.patana93.ntt_data_interview.Utils
import com.patana93.ntt_data_interview.controller.CONN_ERR
import com.patana93.ntt_data_interview.controller.CONN_INFO
import com.patana93.ntt_data_interview.controller.adapter.TeamsMostWinRecyclerViewAdapter
import com.patana93.ntt_data_interview.data.api.FootballDataEndpoints
import com.patana93.ntt_data_interview.data.api.ServiceBuilder
import com.patana93.ntt_data_interview.data.model.Matches
import com.patana93.ntt_data_interview.data.model.Team
import com.patana93.ntt_data_interview.data.model.Teams
import com.patana93.ntt_data_interview.data.model.repo.TeamRepo
import com.patana93.ntt_data_interview.getDateFormatted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class TeamsMostWinFragment : Fragment(), HasLoading {
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

        addLoadingUI()

        request = ServiceBuilder.buildService(FootballDataEndpoints::class.java)

        resultRecycler = view.findViewById(R.id.teamsRecyclerView)
        titleTextView = view.findViewById(R.id.titleMostWinFragTextView)
        shadowImageView = view.findViewById(R.id.shadowImageView)
        loadDataProgressBar = view.findViewById(R.id.loadDataProgressBar)

        resultRecycler.layoutManager = GridLayoutManager(context, 1)
        resultAdapter = TeamsMostWinRecyclerViewAdapter(requireContext(), result)
        resultRecycler.adapter = resultAdapter

        GlobalScope.launch(Dispatchers.IO) {
            if(Utils.isNetworkConnected(requireContext())){
                fetchTeams()
                fetchMostWinnerInDataRange()
            } else {
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        context,
                        getString(R.string.conn_error_check_conn),
                        Toast.LENGTH_LONG
                    ).show()
                    updateUI(-1, "", "")
                }
            }
        }
        return view
    }

    private fun getMaxNumberOfWin(): Int? {
        return TeamRepo.teamRepo.maxByOrNull { it.numbersOfWinInRangeDate }?.numbersOfWinInRangeDate
    }

    private fun updateUI(maxWinner: Int?, startDate: String, endDate: String) {
        result.clear()
        result.addAll(TeamRepo.teamRepo.filter { it.numbersOfWinInRangeDate == maxWinner })
        resultAdapter.notifyDataSetChanged()
        removeLoadingUI()
        titleTextView.text = "Team/s with most wins last 30 days in Serie A\nfrom $startDate to $endDate"
    }

    private suspend fun fetchMostWinnerInDataRange(){
        val response: Response<Matches>? = request.getMatches(getString(R.string.api_key))
        if (response?.isSuccessful == true) {
            //GET All match FINISHED in the competition
            val matchList = response.body()!!.matches

            //Check if from the last match in the competition have passed more than 30 days
            val formatter: DateTimeFormatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ssz")
                .withLocale(Locale.getDefault())
            val lastDateMatch = LocalDate.parse(matchList.last().utcDate, formatter)
            val endDate = if(Utils.checkMatchExistLast30Days(lastDateMatch)){
                LocalDate.now()
            } else {
                lastDateMatch
            }
            val startDate = endDate.minusDays(29)

            //Filter matches
            val filteredMatches = arrayListOf<Match>()
            for(match in matchList){
                val curr = LocalDate.parse(match.utcDate, formatter)
                if(curr.isBefore(endDate) && curr.isAfter(startDate)){
                    filteredMatches.add(match)
                }
            }

            //Set win counter for teams
            for (match in filteredMatches) {
                val score = match.score
                val home = match.homeTeam
                val away = match.awayTeam

                when (score.winner) {
                    "HOME_TEAM" -> TeamRepo.teamRepo.find { it.name == home.name }?.addWin()
                    "AWAY_TEAM" -> TeamRepo.teamRepo.find { it.name == away.name }?.addWin()
                }

                //Update UI
                withContext(Dispatchers.Main){
                    val maxWinner = getMaxNumberOfWin()
                    maxWinner?.let {
                        updateUI(maxWinner, startDate.getDateFormatted(), endDate.getDateFormatted())
                    }
                }
            }
        } else {
            //No Connection
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

    /**
     * Get Teams in a competition
     */
    private suspend fun fetchTeams() {
        val responseTeam: Response<Teams>? = request.getTeams(getString(R.string.api_key))
        if (responseTeam?.isSuccessful == true) {
            TeamRepo.teamRepo.clear()
            TeamRepo.teamRepo = responseTeam.body()!!.teams
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

    override fun addLoadingUI() {
        view?.findViewById<ImageView>(R.id.shadowImageView)?.visibility = View.VISIBLE
        view?.findViewById<ProgressBar>(R.id.loadDataProgressBar)?.visibility = View.VISIBLE
    }

    override fun removeLoadingUI() {
        view?.findViewById<ImageView>(R.id.shadowImageView)?.visibility = View.GONE
        view?.findViewById<ProgressBar>(R.id.loadDataProgressBar)?.visibility = View.GONE
    }
}