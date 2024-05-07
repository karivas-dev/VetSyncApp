package com.example.vetsyncapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetsyncapp.R
import com.example.vetsyncapp.models.Staff

class StaffAdapter(private val staffList: ArrayList<Staff>) : RecyclerView.Adapter<StaffAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_staff, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentStaff = staffList[position]
        holder.staffName.text = currentStaff.name
        holder.staffPhoneNumber.text = currentStaff.phoneNumber
        holder.staffRole.text = currentStaff.role
    }

    override fun getItemCount(): Int {
        return staffList.size
    }

    class ViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val staffName: TextView = itemView.findViewById(R.id.staffName)
        val staffPhoneNumber: TextView = itemView.findViewById(R.id.staffPhone)
        val staffRole: TextView = itemView.findViewById(R.id.staffRole)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }
}