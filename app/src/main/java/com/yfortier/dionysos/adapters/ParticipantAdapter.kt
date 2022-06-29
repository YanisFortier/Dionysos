package com.yfortier.dionysos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yfortier.dionysos.R
import com.yfortier.dionysos.composants.ParticipantItem

class ParticipantAdapter(participantList: ArrayList<String>) : RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder>() {
	private val listeParticipants: ArrayList<String>

	class ParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		var textViewNomParticipant: TextView

		init {
			textViewNomParticipant = itemView.findViewById(R.id.textViewNomParticipant)
		}
	}

	override fun onCreateViewHolder(
			parent: ViewGroup,
			viewType: Int,
	): ParticipantViewHolder {
		val v: View = LayoutInflater.from(parent.context).inflate(R.layout.participant_item, parent, false)
		return ParticipantViewHolder(v)
	}

	override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
		val currentItem: String = listeParticipants[position]
		holder.textViewNomParticipant.text = currentItem
	}

	override fun getItemCount(): Int {
		return listeParticipants.size
	}


	init {
		listeParticipants = participantList
	}
}