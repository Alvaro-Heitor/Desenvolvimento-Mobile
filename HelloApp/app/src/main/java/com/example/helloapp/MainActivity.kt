package com.example.helloapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var count = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var addButton= findViewById<FloatingActionButton>(R.id.addButton)
        var countText = findViewById<TextView>(R.id.count)
        var minusButton = findViewById<FloatingActionButton>(R.id.minusButton)
        addButton.setOnClickListener {
            count++
            countText.text = count.toString()
        }
        minusButton.setOnClickListener {
            count --
            countText.text = count.toString()
        }

    }
}