package com.example.vetsyncapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.vetsyncapp.R
import com.example.vetsyncapp.controllers.AppointmentController
import com.example.vetsyncapp.controllers.PatientController
import com.example.vetsyncapp.controllers.StaffController
import com.example.vetsyncapp.models.Patient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AppointmentCreateEditFragment : Fragment(R.layout.fragment_appointment_edit) {

    private lateinit var spinnerStatusType: Spinner
    private lateinit var spinnerPatientType: Spinner
    private lateinit var btnSaveAppointment: AppCompatButton
    private lateinit var btnDeleteAppointment: AppCompatButton
    private lateinit var lblTitle: TextView
    private lateinit var lblSubTitle: TextView

    private var action: String? = ""
    var id: String? = null
    var status = "" ; var date = "" ; var hour = "" ; var diagnosis = ""
    var patientName = ""
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lblTitle = view.findViewById<TextView>(R.id.editAppointmentTitle)
        lblSubTitle = view.findViewById<TextView>(R.id.editAppointmentSubTitle)
        btnSaveAppointment = view.findViewById(R.id.btnSaveAppointment)
        btnDeleteAppointment = view.findViewById(R.id.btnDeleteAppointment)
        spinnerStatusType = view.findViewById(R.id.spinner_status)
        spinnerPatientType = view.findViewById(R.id.spinner_patients)
        databaseReference = FirebaseDatabase.getInstance().reference.child("patients")

        val patientNameList = ArrayList<String>()
        val patientsList = ArrayList<Patient>()
        val statuses = resources.getStringArray(R.array.statuses)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val patient = ds.getValue(Patient::class.java)
                    patient?.let { it.name?.let { it1 -> patientNameList.add(it1) } }
                    patient?.let { patientsList.add(it) }
                }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    patientNameList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerPatientType.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        }
        loadStatusesSpinner()

        val args = this.arguments
        action = args?.getString("action")
        id = args?.getString("id")

        if (action == "edit") {
            btnDeleteAppointment.text = "Eliminar Cita"
            lblTitle.text = "Editar Cita"
            lblSubTitle.text = "Edita la información de la cita"

            spinnerStatusType.setSelection(statuses.indexOf(args?.getString("status")))
            spinnerPatientType.setSelection(patientNameList.indexOf(args?.getString("patientName")))
            view.findViewById<EditText>(R.id.inputAppointmentDate).setText(args?.getString("date"))
            view.findViewById<EditText>(R.id.inputAppointmentHour).setText(args?.getString("hour"))
            view.findViewById<EditText>(R.id.inputDiagnosisPet).setText(args?.getString("diagnosis"))
        }

        databaseReference.addListenerForSingleValueEvent(valueEventListener)

        btnSaveAppointment.setOnClickListener {
            status = spinnerStatusType.selectedItem.toString()
            date = view.findViewById<EditText>(R.id.inputAppointmentDate).text.toString()
            hour = view.findViewById<EditText>(R.id.inputAppointmentHour).text.toString()
            diagnosis = view.findViewById<EditText>(R.id.inputDiagnosisPet).text.toString()
            patientName = spinnerPatientType.selectedItem.toString()

            when (action) {
                "create" -> {
                    if (!formValidations()) {
                        return@setOnClickListener
                    }
                    //search patient by name
                    val patient = patientsList.find { it.name?.lowercase() == patientName.lowercase() }
                    patient?.let {
                        AppointmentController().store(status, date, hour, it, diagnosis)
                        Toast.makeText(context, "Cita creada con éxito !", Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }
                "edit" -> {
                    if (!formValidations()) {
                        return@setOnClickListener
                    }
                    //search patient by name
                    val patient = patientsList.find { it.name?.lowercase() == patientName.lowercase() }
                    patient?.let {
                        AppointmentController().update(id!!, status, date, hour , patient, diagnosis )
                        Toast.makeText(context, "Cita actualizada con éxito !", Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }
                else -> {
                    Toast.makeText(context, "Acción inválida", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }

        btnDeleteAppointment.setOnClickListener {
            btnDeleteAppointmentHandler(action!!)
        }
    }

    private fun btnDeleteAppointmentHandler(action: String) {
        when (action) {
            "create" -> {
                view?.findViewById<EditText>(R.id.inputAppointmentDate)?.setText("")
                view?.findViewById<EditText>(R.id.inputAppointmentHour)?.setText("")
                view?.findViewById<EditText>(R.id.inputDiagnosisPet)?.setText("")
            }
            "edit" -> {
                if (id != null) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Eliminar Cita")
                    builder.setMessage("¿Estás seguro de eliminar esta Cita?")
                    builder.setPositiveButton("Sí") { dialog, which ->
                        AppointmentController().delete(id!!)
                        Toast.makeText(context, "Cita eliminada con éxito!", Toast.LENGTH_SHORT).show()
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

    private fun loadStatusesSpinner(){
        val statuses = resources.getStringArray(R.array.statuses)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatusType.adapter = adapter
    }

    private fun formValidations(): Boolean {
        if (status.isNullOrEmpty() || date.isNullOrEmpty() || hour.isNullOrEmpty() || diagnosis.isNullOrEmpty() || patientName.isNullOrEmpty()) {
            Toast.makeText(context, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!date.matches(Regex("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))\$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$"))){
            Toast.makeText(context, "Por favor, ingresa una fecha válida Ej: 12/12/2024 , 12.12.2024 , 12-12-2024", Toast.LENGTH_SHORT).show()
            return false
        }
        if(hour.length != 5 || !hour.matches(Regex("^([01]?[0-9]|2[0-3]).[0-5][0-9]\$"))){
            Toast.makeText(context, "Por favor, ingresa una hora válida Ej: 12.00", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}