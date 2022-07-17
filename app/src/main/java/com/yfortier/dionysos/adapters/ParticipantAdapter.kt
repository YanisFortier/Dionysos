package com.example.application.recyclerviewproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yfortier.dionysos.R
import org.w3c.dom.Text

class ParticipantAdapter(listeParticipant: ArrayList<String>) : RecyclerView.Adapter<ParticipantAdapter.ExampleViewHolder>() {
	private val mExampleList: ArrayList<String>
	private var mListener: OnItemClickListener? = null

	interface OnItemClickListener {
		fun onItemClick(position: Int)
		fun onDeleteClick(position: Int)
	}

	fun setOnItemClickListener(listener: OnItemClickListener?) {
		mListener = listener
	}

	class ExampleViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {

		var mTextView1: TextView
		var mDeleteImage: ImageView

		init {
			mTextView1 = itemView.findViewById(R.id.textViewNomParticipant)
			mDeleteImage = itemView.findViewById(R.id.boutonSupprimerParticipant)
			itemView.setOnClickListener {
				if (listener != null) {
					val position = adapterPosition
					if (position != RecyclerView.NO_POSITION) {
						listener.onItemClick(position)
					}
				}
			}
			mDeleteImage.setOnClickListener {
				if (listener != null) {
					val position = adapterPosition
					if (position != RecyclerView.NO_POSITION) {
						listener.onDeleteClick(position)
					}
				}
			}
		}

	}

	override fun onCreateViewHolder(
			parent: ViewGroup,
			viewType: Int,
	): ExampleViewHolder {
		val v: View = LayoutInflater.from(parent.context).inflate(R.layout.participant_item, parent, false)
		return ExampleViewHolder(v, mListener)
	}

	override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
		val currentItem: String = mExampleList[position]
		holder.mTextView1.text = currentItem
	}

	override fun getItemCount(): Int {
		return mExampleList.size
	}

	init {
		mExampleList = listeParticipant
	}
}