package com.example.saferide

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class EmergenciaActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emergencia)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigationEmergencia)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.navigation_home -> startActivity(Intent(this, InicioActivity::class.java))
                R.id.navigation_configuration -> startActivity(Intent(this, ConfiguracoesActivity::class.java))
                R.id.navigation_emergencia -> return@setOnItemSelectedListener true
            }
            true
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val voltar = findViewById<ImageButton>(R.id.voltarEmergencia)
        voltar.setOnClickListener {
            finish()
        }

    }
}
