package com.example.vetsyncapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetsyncapp.R
import com.example.vetsyncapp.adapters.AppointmentAdapter
import com.example.vetsyncapp.adapters.StaffAdapter
import com.example.vetsyncapp.models.Appointment
import com.example.vetsyncapp.models.Staff
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AppointmentFragment: Fragment(R.layout.fragment_appointment_home) {
    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var refAppointment: DatabaseReference = db.getReference("appointments")

    private lateinit var btnAddAppointment : AppCompatButton

    private lateinit var appointmentRecyclerview: RecyclerView
    private lateinit var appointmentArrayList: ArrayList<Appointment>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appointmentRecyclerview = view.findViewById(R.id.appointment_members)
        appointmentRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        appointmentRecyclerview.setHasFixedSize(true)
        fetchAppointments()

        btnAddAppointment = view.findViewById(R.id.btnAddAppointment)
        btnAddAppointment.setOnClickListener {
            goToCreateEditAppointmentFragment("create", null)
        }
    }

    private fun fetchAppointments(){
        refAppointment.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    appointmentArrayList = ArrayList()
                    for (data in snapshot.children) {
                        val appointment = data.getValue(Appointment::class.java)
                        appointmentArrayList.add(appointment!!)
                    }

                    val adapter = AppointmentAdapter(appointmentArrayList)
                    appointmentRecyclerview.adapter = adapter

                    adapter.setOnItemClickListener(object : AppointmentAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val appointment = appointmentArrayList[position]
                            goToCreateEditAppointmentFragment("edit", appointment)
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Staff", "Error fetching Staff: ${error.message}")
                Toast.makeText(context, "Error fetching Staff: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToCreateEditAppointmentFragment(action: String , appointment: Appointment? = null){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        val fragment = AppointmentCreateEditFragment()
        fragment.arguments = Bundle().apply {
            putString("action", action)
            appointment?.let {
                putString("id", it.id)
                //passing a patient object to the fragment
                putString("patientName", it.patient?.name)
                putString("status", it.status)
                putString("date", it.date)
                putString("hour", it.hour)
                putString("diagnosis", it.diagnosis)

            }
        }
        transaction.replace(R.id.mainHostFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}