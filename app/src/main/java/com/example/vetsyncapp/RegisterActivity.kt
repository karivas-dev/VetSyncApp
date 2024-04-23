package com.example.vetsyncapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var buttonRegister: AppCompatButton
    private lateinit var textViewLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)

        auth = FirebaseAuth.getInstance()

        buttonRegister = findViewById<AppCompatButton>(R.id.btnRegister)
        buttonRegister.setOnClickListener {
            val email = findViewById<EditText>(R.id.inputEmailRegister).text.toString()
            val password = findViewById<EditText>(R.id.inputPasswordRegister).text.toString()
            val confirmPass = findViewById<EditText>(R.id.inputConfirmPasswordRegister).text.toString()
            this.register(email, password, confirmPass)
        }

        textViewLogin = findViewById<TextView>(R.id.textViewRegister)
        textViewLogin?.setOnClickListener {
            this.goToLogin()
        }
    }

    private fun register(email: String, password: String, confirmPass: String) {

        if(email.isNotEmpty() && password.isNotEmpty()){
            if(password.length < 6){
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return
            }
            if (password == confirmPass) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, HomePageActivity::class.java)
                        startActivity(intent)
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
        }

    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}