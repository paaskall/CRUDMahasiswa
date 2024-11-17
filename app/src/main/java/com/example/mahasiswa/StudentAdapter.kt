package com.example.mahasiswa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(private val onItemClick: (Student) -> Unit,
                     private val onDeleteClick: (Student) -> Unit)
    : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private var students: List<Student> = emptyList()

    fun setStudents(newStudents: List<Student>?) {
        this.students = newStudents ?: emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view, onItemClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student)
    }

    override fun getItemCount() = students.size

    class StudentViewHolder(
        view: View,
        private val onItemClick: (Student) -> Unit,
        private val onDeleteClick: (Student) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val tvNim: TextView = view.findViewById(R.id.tvNim)
        private val tvNama: TextView = view.findViewById(R.id.tvNama)
        private val tvJurusan: TextView = view.findViewById(R.id.tvJurusan)
        private val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)

        fun bind(student: Student) {
            tvNim.text = "NIM: ${student.nim}"
            tvNama.text = "Nama: ${student.nama}"
            tvJurusan.text = "Jurusan: ${student.jurusan}"

            itemView.setOnClickListener { onItemClick(student) }
            btnDelete.setOnClickListener { onDeleteClick(student) }
        }
    }
}