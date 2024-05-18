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
import com.example.vetsyncapp.adapters.StaffAdapter
import com.example.vetsyncapp.models.Staff
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StaffFragment: Fragment(R.layout.fragment_staff_home) {

    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var refStaff: DatabaseReference = db.getReference("staff")

    private lateinit var btnAddStaff : AppCompatButton

    private lateinit var staffRecyclerview: RecyclerView
    private lateinit var staffArrayList: ArrayList<Staff>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        staffRecyclerview = view.findViewById(R.id.staff_members)
        staffRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        staffRecyclerview.setHasFixedSize(true)
        fetchStaff()

        btnAddStaff = view.findViewById(R.id.btnAddStaff)
        btnAddStaff.setOnClickListener {
            goToCreateEditStaffFragment("create", null)
        }
    }

    private fun fetchStaff(){
        refStaff.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    staffArrayList = ArrayList()
                    for (data in snapshot.children) {
                        val staff = data.getValue(Staff::class.java)
                        staffArrayList.add(staff!!)
                    }

                    val adapter = StaffAdapter(staffArrayList)
                    staffRecyclerview.adapter = adapter

                    adapter.setOnItemClickListener(object : StaffAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val staff = staffArrayList[position]
                            goToCreateEditStaffFragment("edit", staff)
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

    private fun goToCreateEditStaffFragment(action: String , staff: Staff? = null){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        val fragment = StaffCreateEditFragment()
        fragment.arguments = Bundle().apply {
            putString("action", action)
            staff?.let {
                putString("id", it.id)
                putString("name", it.name)
                putString("phoneNumber", it.phoneNumber)
                putString("role", it.role)
            }
        }
        transaction.replace(R.id.mainHostFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}