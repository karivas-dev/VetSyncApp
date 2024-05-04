package com.example.vetsyncapp.models

data class Patient(var id: String?=null, var name: String?=null, var breed: String?=null, var age: Int?=null,
                   var weight: Double?=null, var owner: String?=null, var ownerPhone: String?=null,
                   var diagnosis: String?=null)
