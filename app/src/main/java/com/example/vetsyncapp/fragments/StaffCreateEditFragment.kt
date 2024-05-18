package com.example.vetsyncapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.vetsyncapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.vetsyncapp.controllers.StaffController

class StaffCreateEditFragment: Fragment(R.layout.fragment_staff_edit) {
    private lateinit var spinnerMemberType: Spinner
    private lateinit var btnSaveStaff: AppCompatButton
    private lateinit var btnDeleteStaff: AppCompatButton
    private lateinit var lblTitle: TextView
    private lateinit var lblSubTitle: TextView

    private var action: String? = ""
    var id: String? = null
    var name = ""
    var phoneNumber = ""
    var position = ""

    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_staff_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lblTitle = view.findViewById<TextView>(R.id.editStaffTitle)
        lblSubTitle = view.findViewById<TextView>(R.id.editStaffSubTitle)
        btnSaveStaff = view.findViewById(R.id.btnSaveStaff)
        btnDeleteStaff = view.findViewById(R.id.btnDeleteStaff)
        spinnerMemberType = view.findViewById(R.id.spinner_memberType)
        databaseReference = FirebaseDatabase.getInstance().reference.child("roles")

        val rolesList = ArrayList<String>()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val role = ds.getValue(String::class.java)
                    role?.let { rolesList.add(it) }
                }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    rolesList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerMemberType.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        }

        val args = this.arguments
        action = args?.getString("action")
        id = args?.getString("id")

        if (action == "edit") {
            btnDeleteStaff.text = "Eliminar Personal"
            lblTitle.text = "Editar Personal"
            lblSubTitle.text = "Edita la información del personal"
            view.findViewById<EditText>(R.id.inputNameStaff).setText(args?.getString("name"))
            view.findViewById<EditText>(R.id.inputPhoneStaff).setText(args?.getString("phoneNumber"))
            spinnerMemberType.setSelection(rolesList.indexOf(args?.getString("position")))
        }

        databaseReference.addListenerForSingleValueEvent(valueEventListener)

        btnSaveStaff.setOnClickListener {
            name = view.findViewById<EditText>(R.id.inputNameStaff).text.toString()
            phoneNumber = view.findViewById<EditText>(R.id.inputPhoneStaff).text.toString()
            position = spinnerMemberType.selectedItem.toString()

            when (action) {
                "create" -> {
                    if (!formValidations()) {
                        return@setOnClickListener
                    }
                    StaffController().store(id, name, phoneNumber, position)
                    Toast.makeText(context, "Personal creado con éxito !", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                "edit" -> {
                    if (!formValidations()) {
                        return@setOnClickListener
                    }
                    StaffController().update(id!!, name, phoneNumber, position)
                    Toast.makeText(context, "Personal actualizado con éxito !", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                else -> {
                    Toast.makeText(context, "Acción inválida", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }

        btnDeleteStaff.setOnClickListener {
            btnDeleteStaffHandler(action!!)
        }
    }

    private fun btnDeleteStaffHandler(action: String) {
        when (action) {
            "create" -> {
                view?.findViewById<EditText>(R.id.inputNameStaff)?.setText("")
                view?.findViewById<EditText>(R.id.inputPhoneStaff)?.setText("")
            }
            "edit" -> {
                if (id!= null) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Eliminar Personal")
                    builder.setMessage("¿Estás seguro de eliminar este personal?")
                    builder.setPositiveButton("Sí") { dialog, which ->
                        StaffController().delete(id!!)
                        Toast.makeText(context, "Personal eliminado con éxito!", Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                    builder.setNegativeButton("No") { dialog, which ->
                        dialog.dismiss()
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                } else {
                    Toast.makeText(context, "ID inválido", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(context, "Acción inválida", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun formValidations(): Boolean {
        if (name.isNullOrEmpty() || phoneNumber.isNullOrEmpty()) {
            Toast.makeText(context, "Por favor llena todos los campos", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (!phoneNumber.matches(Regex("\\d{4}\\s\\d{4}"))) {
            Toast.makeText(
                context,
                "El número debe cumplir con el formato Ej: 2324 9099",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }
}