package com.example.vetsyncapp.controllers

import com.example.vetsyncapp.models.Staff
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StaffController {

    companion object {
        private var db: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refStaff: DatabaseReference = db.getReference("staff")
    }

    private var staff: MutableList<Staff>? = null

    fun store(id: String?, name: String, phoneNumber: String, role: String) {
        val staffId = id ?: refStaff.push().key!!
        val staff = Staff(staffId, name, phoneNumber, role)
        refStaff.child(staffId).setValue(staff)
    }

    fun update(id: String, name: String, phoneNumber: String, role: String) {
        val staff = Staff(id, name, phoneNumber, role)
        refStaff.child(id).setValue(staff)
    }

    fun delete(id: String) {
        refStaff.child(id).removeValue()
    }
}