package com.patana93.ntt_data_interview.controller.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.patana93.ntt_data_interview.R
import com.patana93.ntt_data_interview.data.model.Team

class TeamsMostWinRecyclerViewAdapter(private val context: Context, private val teamsList: List<Team>): RecyclerView.Adapter<TeamsMostWinRecyclerViewAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_teams_most_win_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from dataset at this position and replace the
        // contents of the view with that element
        viewHolder.name.text = teamsList[position].name
        viewHolder.nWin.text = teamsList[position].numbersOfWinInRangeDate.toString()
        GlideToVectorYou.justLoadImage(context as Activity, teamsList[position].crestURL.toUri(), viewHolder.image)
    }

    // Return the size of your teamList (invoked by the layout manager)
    override fun getItemCount() = teamsList.size

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.nameTeamItem)
        val nWin: TextView = view.findViewById(R.id.nWinTeamItem)
        val image: ImageView = view.findViewById(R.id.imageTeamItem)
    }
}