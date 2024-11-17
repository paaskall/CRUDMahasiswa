package com.example.mahasiswa

data class StudentResponse(
    val status: Boolean = false,
    val result: Any // This can be either List<Student> or String depending on status
)