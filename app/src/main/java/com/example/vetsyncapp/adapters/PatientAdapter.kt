package com.example.vetsyncapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetsyncapp.R
import com.example.vetsyncapp.models.Patient

class PatientAdapter(private val patients: ArrayList<Patient>) : RecyclerView.Adapter<PatientAdapter.ViewHolder>() {

    // Add an interface to handle the click event
    private lateinit var mListener: OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_patient, parent, false)
        return ViewHolder(itemView, mListener)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPatient = patients[position]
        holder.txtId.text = (position + 1).toString()
        holder.txtName.text = currentPatient.name
        holder.txtBreed.text = currentPatient.breed
        holder.txtAge.text = currentPatient.age.toString()
        holder.txtWeight.text = currentPatient.weight.toString()
        holder.txtOwner.text = currentPatient.owner
        holder.txtOwnerPhone.text = currentPatient.ownerPhone
        holder.txtDiagnosis.text = currentPatient.diagnosis

    }
    override fun getItemCount(): Int {
        return patients.size
    }

    class ViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val txtId : TextView = itemView.findViewById(R.id.petId)
        val txtName: TextView = itemView.findViewById(R.id.petName)
        val txtBreed: TextView = itemView.findViewById(R.id.petBreed)
        val txtAge: TextView = itemView.findViewById(R.id.petAge)
        val txtWeight: TextView = itemView.findViewById(R.id.petWeight)
        val txtOwner: TextView = itemView.findViewById(R.id.petOwner)
        val txtOwnerPhone: TextView = itemView.findViewById(R.id.petPhone)
        val txtDiagnosis: TextView = itemView.findViewById(R.id.petDiagnosis)

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