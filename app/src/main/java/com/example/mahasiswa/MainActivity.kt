package com.example.mahasiswa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Referensi tombol
        val btnViewStudents = findViewById<Button>(R.id.btnViewStudents)
        val btnAddStudent = findViewById<Button>(R.id.btnAddStudent)

        // Tambahkan listener untuk setiap tombol
        btnViewStudents.setOnClickListener {
            val intent = Intent(this, StudentListActivity::class.java)
            startActivity(intent)
        }

        btnAddStudent.setOnClickListener {
            val intent = Intent(this, Form::class.java)
            startActivity(intent)
        }
    }
}