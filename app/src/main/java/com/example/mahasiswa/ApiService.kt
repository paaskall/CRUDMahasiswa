package com.example.mahasiswa

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


interface ApiService {
    @POST("api/kelompok_1/insert_mahasiswa.php")
    fun addStudent(@Body student: Student): Call<Student>

    @GET("api/kelompok_1/select_mahasiswa.php")
    fun getStudents(): Call<StudentResponse>

    @PUT("api/kelompok_1/update_mahasiswa.php")
    fun updateStudent(@Body student: Student): Call<StudentResponse>

    @DELETE("api/kelompok_1/delete_mahasiswa.php")
    fun deleteStudent(@Query("nim") nim: String): Call<StudentResponse>
}
