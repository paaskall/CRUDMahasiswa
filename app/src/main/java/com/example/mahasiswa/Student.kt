package com.example.mahasiswa

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    val nim: String = "",
    val nama: String = "",
    val jurusan: String = "",
    val status: Boolean? = null,
    val result: String? = null
) : Parcelable