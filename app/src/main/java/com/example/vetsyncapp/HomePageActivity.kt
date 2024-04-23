package com.example.vetsyncapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class HomePageActivity : AppCompatActivity() {
    private lateinit var btnLogout: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        btnLogout = findViewById<AppCompatButton>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            logout()
        }

    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut().also{
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}