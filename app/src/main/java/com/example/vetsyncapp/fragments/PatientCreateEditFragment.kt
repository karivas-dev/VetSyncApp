package com.example.vetsyncapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.vetsyncapp.R
import com.example.vetsyncapp.hooks.PatientHook


class PatientCreateEditFragment : Fragment(R.layout.fragment_patient_edit) {

    private lateinit var btnSavePatient : AppCompatButton
    private lateinit var btnDeletePatient : AppCompatButton
    private lateinit var lblTitle : TextView
    private lateinit var lblSubTitle : TextView

    private var action: String? = ""
    var id: String? = "" ; var name = "" ; var breed = "" ; var age = ""
    var weight = "" ;var owner = "" ; var ownerPhone = "" ; var diagnosis = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lblTitle = view.findViewById<TextView>(R.id.editPetTitle)
        lblSubTitle = view.findViewById<TextView>(R.id.editPetSubTitle)
        btnSavePatient = view.findViewById(R.id.btnSavePatient)
        btnDeletePatient = view.findViewById(R.id.btnDeletePatient)

        val args = this.arguments
        action = args?.getString("action")
        id = args?.getString("id")

        if (action == "edit") {
            btnDeletePatient.setText("Eliminar Paciente")
            lblTitle.setText("Editar Paciente")
            lblSubTitle.setText("Edita la informacion del paciente")
            view.findViewById<EditText>(R.id.inputNamePet).setText(args?.getString("name"))
            view.findViewById<EditText>(R.id.inputBreedPet).setText(args?.getString("breed"))
            view.findViewById<EditText>(R.id.inputAgePet).setText(args?.getInt("age").toString())
            view.findViewById<EditText>(R.id.inputWeightPet).setText(args?.getDouble("weight").toString())
            view.findViewById<EditText>(R.id.inputOwnerPet).setText(args?.getString("owner"))
            view.findViewById<EditText>(R.id.inputPhonePet).setText(args?.getString("ownerPhone"))
            view.findViewById<EditText>(R.id.inputDiagnosisPet).setText(args?.getString("diagnosis"))
        }

        btnSavePatient.setOnClickListener{
            name = view.findViewById<EditText>(R.id.inputNamePet).text.toString()
            breed = view.findViewById<EditText>(R.id.inputBreedPet).text.toString()
            age = view.findViewById<EditText>(R.id.inputAgePet).text.toString()
            weight = view.findViewById<EditText>(R.id.inputWeightPet).text.toString()
            owner = view.findViewById<EditText>(R.id.inputOwnerPet).text.toString()
            ownerPhone = view.findViewById<EditText>(R.id.inputPhonePet).text.toString()
            diagnosis = view.findViewById<EditText>(R.id.inputDiagnosisPet).text.toString()

            when(action){
                "create" -> {
                    if(!formValidations()){
                        return@setOnClickListener
                    }
                    PatientHook().store(name, breed, age.toInt(), weight.toDouble(), owner, ownerPhone, diagnosis)
                    Toast.makeText(context, "Paciente creado con exito !", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                "edit" -> {
                    if(!formValidations()){
                        return@setOnClickListener
                    }
                    PatientHook().update(id!!, name, breed, age.toInt(), weight.toDouble(), owner, ownerPhone, diagnosis)
                    Toast.makeText(context, "Paciente actualizado con exito !", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                else -> {
                    Toast.makeText(context, "Invalid action", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }

        btnDeletePatient.setOnClickListener{
           btnDeletePatientHandler(action!!)
        }
    }

    private fun btnDeletePatientHandler(action: String){
        when(action){
            "create" -> {
                view?.findViewById<EditText>(R.id.inputNamePet)?.setText("")
                view?.findViewById<EditText>(R.id.inputBreedPet)?.setText("")
                view?.findViewById<EditText>(R.id.inputAgePet)?.setText("")
                view?.findViewById<EditText>(R.id.inputWeightPet)?.setText("")
                view?.findViewById<EditText>(R.id.inputOwnerPet)?.setText("")
                view?.findViewById<EditText>(R.id.inputPhonePet)?.setText("")
                view?.findViewById<EditText>(R.id.inputDiagnosisPet)?.setText("")
            }
            "edit" -> {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Eliminar Paciente")
                builder.setMessage("¿Estas seguro de eliminar este paciente?")
                builder.setPositiveButton("Si") { dialog, which ->
                    PatientHook().delete(id!!)
                    Toast.makeText(context, "Paciente eliminado con exito !", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                builder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            else -> {
                Toast.makeText(context, "Invalid action", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun formValidations():Boolean{
        if(name.isNullOrEmpty() || breed.isNullOrEmpty() || age.isNullOrEmpty() || weight.isNullOrEmpty() || owner.isNullOrEmpty() || ownerPhone.isNullOrEmpty() || diagnosis.isNullOrEmpty()){
            Toast.makeText(context, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!ownerPhone.matches(Regex("\\d{4}\\s\\d{4}"))){
            Toast.makeText(context, "El numero debe cumplir con la expresion Ej: 2324 9099", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}


