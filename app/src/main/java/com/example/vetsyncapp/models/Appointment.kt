package com.example.vetsyncapp.models

data class Appointment(var id: String? = null, var status: String? = null, var date: String? = null, var hour: String? = null,
                       var patient: Patient? = null, var diagnosis: String? = null)
