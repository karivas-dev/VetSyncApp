package com.example.vetsyncapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.vetsyncapp.LoginActivity
import com.example.vetsyncapp.R
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment: Fragment(R.layout.fragment_profile) {

    private lateinit var btnSaveProfile : AppCompatButton
    private lateinit var btnLogout: AppCompatButton
    private lateinit var auth: FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        view.findViewById<TextView>(R.id.labelCurrentUserEmail).setText(auth.currentUser?.email)

        btnSaveProfile = view?.findViewById(R.id.btnSaveProfile) ?: AppCompatButton(requireContext())
        btnSaveProfile.setOnClickListener {
            saveProfile()
        }

        btnLogout = view?.findViewById(R.id.btnLogout) ?: AppCompatButton(requireContext())
        btnLogout.setOnClickListener {
            logout()
        }
    }

     private fun saveProfile() {
         val currentPassword = view?.findViewById<EditText>(R.id.inputCurrentPassword)?.text.toString()
         val password = view?.findViewById<EditText>(R.id.inputPasswordUpdate)?.text.toString()
         val confirmPassword = view?.findViewById<EditText>(R.id.inputConfirmPasswordUpdate)?.text.toString()

         if(password.isNotEmpty() && confirmPassword.isNotEmpty() && currentPassword.isNotEmpty()) {
             if (password == confirmPassword) {
                 auth.signInWithEmailAndPassword(
                     auth.currentUser?.email.toString(),
                     currentPassword
                 ).addOnCompleteListener { task ->
                     if (task.isSuccessful) {
                         auth.currentUser?.updatePassword(password)?.addOnCompleteListener {
                             Toast.makeText(requireContext(), "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                         }
                     }
                 }.addOnFailureListener { exception ->
                     Toast.makeText(requireContext(), "Su contraseña actual no es la correcta", Toast.LENGTH_LONG).show()
                 }
             }else{
                    Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
             }
         }else{
             Toast.makeText(requireContext(), "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
         }
    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut().also{
            Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

    }

}