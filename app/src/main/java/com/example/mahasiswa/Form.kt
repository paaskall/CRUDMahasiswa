package com.example.mahasiswa

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Form : AppCompatActivity() {
    private var isEditMode = false
    private var studentToEdit: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get student data if in edit mode
        studentToEdit = intent.getParcelableExtra("student")
        isEditMode = studentToEdit != null

        val etNim = findViewById<EditText>(R.id.etNim)
        val etNama = findViewById<EditText>(R.id.etNama)
        val etJurusan = findViewById<EditText>(R.id.etJurusan)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        // Populate fields if editing
        if (isEditMode) {
            supportActionBar?.title = "Edit Data Mahasiswa"
            studentToEdit?.let { student ->
                etNim.setText(student.nim)
                etNim.isEnabled = false  // NIM shouldn't be editable
                etNama.setText(student.nama)
                etJurusan.setText(student.jurusan)
                btnSubmit.text = "Update"
            }
        } else {
            supportActionBar?.title = "Data Mahasiswa"
        }

        toolbar.setNavigationOnClickListener { finish() }

        btnSubmit.setOnClickListener {
            val nim = etNim.text.toString()
            val nama = etNama.text.toString()
            val jurusan = etJurusan.text.toString()

            if (nama.isNotEmpty() && nim.isNotEmpty() && jurusan.isNotEmpty()) {
                val student = Student(nim, nama, jurusan)

                if (isEditMode) {
                    updateStudent(student)
                } else {
                    addStudent(student)
                }
            } else {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addStudent(student: Student) {
        RetrofitClient.instance.addStudent(student).enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status == true) {
                        Toast.makeText(this@Form, "Success: ${apiResponse.result}", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@Form, "Failed: ${apiResponse?.result ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Form, "Server Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Toast.makeText(this@Form, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateStudent(student: Student) {
        RetrofitClient.instance.updateStudent(student).enqueue(object : Callback<StudentResponse> {
            override fun onResponse(call: Call<StudentResponse>, response: Response<StudentResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status) {
                        Toast.makeText(this@Form, "Update successful", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@Form, "Update failed: ${apiResponse?.result}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Form, "Server Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<StudentResponse>, t: Throwable) {
                Toast.makeText(this@Form, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}