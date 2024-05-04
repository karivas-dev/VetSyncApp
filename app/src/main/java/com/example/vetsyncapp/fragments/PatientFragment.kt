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
import com.example.vetsyncapp.adapters.PatientAdapter
import com.example.vetsyncapp.models.Patient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class PatientFragment: Fragment(R.layout.fragment_patient_history) {

    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var refPatients: DatabaseReference = db.getReference("patients")

    private lateinit var btnAddPatient : AppCompatButton

    private lateinit var patientRecyclerview: RecyclerView
    private lateinit var patientArrayList: ArrayList<Patient>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the recycler view
        patientRecyclerview = view.findViewById(R.id.patients_members)
        patientRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        patientRecyclerview.setHasFixedSize(true)
        fetchPatients()

        // Add Patient Button
        btnAddPatient = view?.findViewById(R.id.btnAddPatient) ?: AppCompatButton(requireContext())
        btnAddPatient.setOnClickListener {
            goToCreateEditPatientFragment("create", null)
        }
    }

    private fun fetchPatients(){
        refPatients.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    patientArrayList = ArrayList()
                    for (data in snapshot.children) {
                        val patient = data.getValue(Patient::class.java)
                        patientArrayList.add(patient!!)
                    }

                    // Set the adapter
                    var adapter = PatientAdapter(patientArrayList)
                    patientRecyclerview.adapter = adapter

                    // Set the OnClickListener
                    adapter.setOnItemClickListener(object : PatientAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val patient = patientArrayList[position]
                            goToCreateEditPatientFragment("edit", patient)
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Patient", "Error fetching Patients: ${error.message}")
                Toast.makeText(context, "Error fetching Patients: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun goToCreateEditPatientFragment(action: String , patient: Patient? = null){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        val fragment = PatientCreateEditFragment()
        fragment.arguments = Bundle().apply {
            putString("action", action)
            patient?.let {
                putString("id", it.id)
                putString("name", it.name)
                putString("breed", it.breed)
                putInt("age", it.age!!)
                putDouble("weight", it.weight!!)
                putString("owner", it.owner)
                putString("ownerPhone", it.ownerPhone)
                putString("diagnosis", it.diagnosis)
            }
        }
        transaction.replace(R.id.mainHostFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}