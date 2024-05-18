package com.example.vetsyncapp.controllers

import android.util.Log
import com.example.vetsyncapp.models.Appointment
import com.example.vetsyncapp.models.Patient
import com.example.vetsyncapp.models.Staff
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AppointmentController {

    companion object {
        private var db: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refAppointment: DatabaseReference = db.getReference("appointments")
    }


    fun store(status: String, date: String, hour: String, patient: Patient, diagnosis: String) {
        val id = refAppointment.push().key
        val appointment = Appointment(id!!, status, date, hour, patient, diagnosis)
        refAppointment.child(id).setValue(appointment)
    }

    fun update(id: String, status: String, date: String, hour: String, patient: Patient, diagnosis: String) {
        val appointment = Appointment(id, status, date, hour, patient, diagnosis)
        refAppointment.child(id).setValue(appointment)
    }

    fun delete(id: String) {
        refAppointment.child(id).removeValue()
    }
}