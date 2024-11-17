package com.example.mahasiswa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: StudentAdapter

    companion object {
        private const val EDIT_STUDENT_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)

        // Initialize Views
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        // Initialize adapter with click listeners
        adapter = StudentAdapter(
            onItemClick = { student ->
                // Handle edit
                val intent = Intent(this, Form::class.java).apply {
                    putExtra("student", student)
                }
                startActivityForResult(intent, EDIT_STUDENT_REQUEST)
            },
            onDeleteClick = { student ->
                // Show confirmation dialog
                showDeleteConfirmationDialog(student)
            }
        )

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Setup Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Daftar Mahasiswa"

        toolbar.setNavigationOnClickListener { finish() }

        // Load initial data
        loadStudents()
    }

    private fun loadStudents() {
        progressBar.visibility = View.VISIBLE

        RetrofitClient.instance.getStudents().enqueue(object : Callback<StudentResponse> {
            override fun onResponse(call: Call<StudentResponse>, response: Response<StudentResponse>) {
                progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        val studentResponse = response.body()
                        Log.d("API_RESPONSE", "Response: $studentResponse")

                        if (studentResponse?.status == true) {
                            // Convert result to List<Student> using Gson
                            val gson = Gson()
                            val jsonArray = gson.toJsonTree(studentResponse.result).asJsonArray
                            val students = jsonArray.map { jsonElement ->
                                gson.fromJson(jsonElement, Student::class.java)
                            }

                            adapter.setStudents(students)

                            if (students.isEmpty()) {
                                Toast.makeText(this@StudentListActivity,
                                    "No students found",
                                    Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val message = if (studentResponse?.result is String) {
                                studentResponse.result.toString()
                            } else "No data found"

                            Toast.makeText(this@StudentListActivity,
                                message,
                                Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@StudentListActivity,
                            "Error: ${response.code()}",
                            Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("API_ERROR", "Error: ${e.message}", e)
                    Toast.makeText(this@StudentListActivity,
                        "Error processing data: ${e.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<StudentResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("API_ERROR", "Error: ${t.message}", t)
                Toast.makeText(this@StudentListActivity,
                    "Network error: ${t.message}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDeleteConfirmationDialog(student: Student) {
        AlertDialog.Builder(this)
            .setTitle("Delete Student")
            .setMessage("Are you sure you want to delete ${student.nama}?")
            .setPositiveButton("Delete") { _, _ ->
                deleteStudent(student)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteStudent(student: Student) {
        progressBar.visibility = View.VISIBLE

        RetrofitClient.instance.deleteStudent(student.nim).enqueue(object : Callback<StudentResponse> {
            override fun onResponse(call: Call<StudentResponse>, response: Response<StudentResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status) {
                        Toast.makeText(this@StudentListActivity,
                            "Student deleted successfully",
                            Toast.LENGTH_SHORT).show()
                        loadStudents()  // Refresh the list
                    } else {
                        Toast.makeText(this@StudentListActivity,
                            "Delete failed: ${apiResponse?.result}",
                            Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@StudentListActivity,
                        "Server Error: ${response.code()}",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<StudentResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@StudentListActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_STUDENT_REQUEST && resultCode == RESULT_OK) {
            loadStudents()  // Refresh the list after edit
        }
    }
}