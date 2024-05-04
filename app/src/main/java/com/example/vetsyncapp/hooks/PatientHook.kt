package com.example.vetsyncapp.hooks

import android.util.Log
import android.widget.Toast
import com.example.vetsyncapp.models.Patient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PatientHook {

    companion object {
        var db: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refPatients: DatabaseReference = db.getReference("patients")
    }

    private var patients: MutableList<Patient>? = null

    fun store(
        name: String, breed: String, age: Int, weight: Double,
        owner: String, ownerPhone: String, diagnostic: String
    ) {
        val id = refPatients.push().key
        val patient = Patient(id!!, name, breed, age, weight, owner, ownerPhone, diagnostic)
        refPatients.child(id).setValue(patient)
    }

    fun update(
        id: String, name: String, breed: String, age: Int, weight: Double,
        owner: String, ownerPhone: String, diagnosis: String
    ) {
        val patient = Patient(id, name, breed, age, weight, owner, ownerPhone, diagnosis)
        refPatients.child(id).setValue(patient)
    }

    fun delete(id: String) {
        refPatients.child(id).removeValue()
    }
}

