package com.patana93.ntt_data_interview.controller.fragment

import Competition
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.patana93.ntt_data_interview.R
import com.patana93.ntt_data_interview.controller.CONN_ERR
import com.patana93.ntt_data_interview.controller.CONN_INFO
import com.patana93.ntt_data_interview.controller.adapter.CompetitionsRecyclerViewAdapter
import com.patana93.ntt_data_interview.data.api.FootballDataEndpoints
import com.patana93.ntt_data_interview.data.api.ServiceBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class CompetitionsFragment : Fragment() {
    private lateinit var request: FootballDataEndpoints
    private lateinit var competitionsRecycler: RecyclerView
    private var competitionsArrayList: ArrayList<String> = arrayListOf()
    private lateinit var competitionsAdapter: CompetitionsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_competitions_list, container, false)

        competitionsRecycler= view.findViewById(R.id.competitionsRecyclerView)
        competitionsRecycler.layoutManager = GridLayoutManager(context, 2)
        competitionsAdapter= CompetitionsRecyclerViewAdapter(competitionsArrayList)
        competitionsRecycler.adapter = competitionsAdapter

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        request = ServiceBuilder.buildService(FootballDataEndpoints::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val responseTeam: Response<Competition>? = request.getCompetition(getString(R.string.api_key))
            if (responseTeam?.isSuccessful == true) {
                val competitionsList = responseTeam.body()!!
                withContext(Dispatchers.Main){
                    //TODO ADD LEAGUE IMAGE
                    competitionsArrayList.clear()
                    competitionsArrayList.add(competitionsList.name)
                    competitionsAdapter.notifyDataSetChanged()
                    Log.i(CONN_INFO, competitionsArrayList.joinToString())
                }
            } else {
                withContext(Dispatchers.Main){
                    Toast.makeText(context, getString(R.string.conn_error_check_conn), Toast.LENGTH_LONG).show()
                    Log.e(CONN_ERR, "${responseTeam?.errorBody()}")
                }

            }
        }
    }
}