package com.patana93.ntt_data_interview.controller.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.patana93.ntt_data_interview.R


class CompetitionsRecyclerViewAdapter(
        private val values: List<String>,
)
    : RecyclerView.Adapter<CompetitionsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_competitions_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.competitionName.text = item
        holder.competitionCardView.setOnClickListener{
            holder.competitionCardView.findNavController().navigate(R.id.action_competitionsFragment_to_teamsMostWinFragment)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val competitionCardView: CardView = view.findViewById(R.id.competitionCardView)
        val competitionName: TextView = view.findViewById(R.id.nameCompetitionItem)
    }
}