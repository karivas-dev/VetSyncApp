package com.example.vetsyncapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetsyncapp.R
import com.example.vetsyncapp.models.Appointment

class AppointmentAdapter(private val appointmentList: ArrayList<Appointment>) : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>()  {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_appointment, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAppointment = appointmentList[position]
        holder.petId.text = (position + 1).toString()
        holder.petName.text = currentAppointment.patient?.name
        holder.petBreed.text = currentAppointment.patient?.breed
        holder.status.text = currentAppointment.status
        holder.date.text = currentAppointment.date
        holder.hour.text = currentAppointment.hour
        holder.owner.text = currentAppointment.patient?.owner
        holder.ownerPhone.text = currentAppointment.patient?.ownerPhone
        holder.diagnosis.text = currentAppointment.diagnosis
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }

    class ViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val petId = itemView.findViewById<TextView>(R.id.petId)
        val petName = itemView.findViewById<TextView>(R.id.petName)
        val petBreed = itemView.findViewById<TextView>(R.id.petBreed)
        val status = itemView.findViewById<TextView>(R.id.appointmentStatus)
        val date = itemView.findViewById<TextView>(R.id.appointmentDate)
        val hour = itemView.findViewById<TextView>(R.id.appointmentHour)
        val owner = itemView.findViewById<TextView>(R.id.petOwner)
        val ownerPhone = itemView.findViewById<TextView>(R.id.petPhone)
        val diagnosis = itemView.findViewById<TextView>(R.id.petDiagnosis)

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