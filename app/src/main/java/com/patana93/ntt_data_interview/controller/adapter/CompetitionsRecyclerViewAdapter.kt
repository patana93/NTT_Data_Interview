package com.patana93.ntt_data_interview.controller.adapter

import Competition
import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.navigation.findNavController
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.patana93.ntt_data_interview.R


class CompetitionsRecyclerViewAdapter(
    private val context: Context,
    private val competitionsList: List<Competition>
)
    : RecyclerView.Adapter<CompetitionsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_competitions_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = competitionsList[position]
        holder.competitionName.text = item.name
        GlideToVectorYou.justLoadImage(context as Activity, item.emblemUrl.toUri(), holder.competitionImage)
        holder.competitionCardView.setOnClickListener{
            holder.competitionCardView.findNavController().navigate(R.id.action_competitionsFragment_to_teamsMostWinFragment)
        }
    }

    override fun getItemCount(): Int = competitionsList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val competitionCardView: CardView = view.findViewById(R.id.competitionCardView)
        val competitionImage: ImageView = view.findViewById(R.id.imageCompetitionItem)
        val competitionName: TextView = view.findViewById(R.id.nameCompetitionItem)
    }
}