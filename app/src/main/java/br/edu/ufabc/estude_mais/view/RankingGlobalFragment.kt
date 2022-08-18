package br.edu.ufabc.estude_mais.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.estude_mais.databinding.FragmentRankingGlobalBinding
import br.edu.ufabc.estude_mais.databinding.ItemRankingBinding
import br.edu.ufabc.estude_mais.model.Course
import br.edu.ufabc.estude_mais.model.Student
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import br.edu.ufabc.estude_mais.viewmodel.SessionViewModel
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class RankingGlobalFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private lateinit var binding: FragmentRankingGlobalBinding
    var studentList: MutableList<Student> = mutableListOf()
    private lateinit var studentActual: Student

    data class StudentRanking(
        val name: String,
        val grade: Long,
    )
    var studentsRanking: MutableList<StudentRanking> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRankingGlobalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        binding.root.visibility = View.INVISIBLE
        super.onStart()
        if(!sessionViewModel.isTeacher) getActualStudent()
        getStudents()
    }

    private fun getActualStudent(){
        mainViewModel.getStudentById(sessionViewModel.getSessionId()).observe(viewLifecycleOwner){status ->
            if (status is MainViewModel.Status.Success) try {
                studentActual = ((status.result as MainViewModel.Result.SingleStudent).value)
            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to obtain student", e)
                notifyError("Failed to obtain student")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to student", status.e)
                notifyError("Failed to fetch student")
            }
        }
    }

    private fun getStudents(){
        mainViewModel.getAllStudent().observe(viewLifecycleOwner){ status ->
            if (status is MainViewModel.Status.Success) try {
                val students = ((status.result as MainViewModel.Result.StudentList).value)
                if(students.isNotEmpty()) {
                    studentList = students as MutableList<Student>
                    getGrades()
                }else{
                    binding.root.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to obtain students", e)
                notifyError("Failed to obtain students")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to students", status.e)
                notifyError("Failed to fetch students")
            }
        }
    }

    private fun getGrades(){
        binding.recyclerviewStudentsList.apply {
            studentList.forEach { student ->
                mainViewModel.getStudentExperience(student.id).observe(viewLifecycleOwner) { status ->
                    if (status is MainViewModel.Status.Success) try {
                        val grade = (status.result as MainViewModel.Result.Grade).value
                        val studentRanking = StudentRanking(
                            name = student.username,
                            grade = grade
                        )

                        studentsRanking.add(studentRanking)

                        if (studentsRanking.size == studentList.size) {
                            val sortedStudents = studentsRanking.sortedByDescending {
                                it.grade
                            }
                            adapter = StudentRankingAdapter(sortedStudents)
                        }
                    } catch (e: Exception) {
                        Log.e("FRAGMENT", "Failed to get grade", e)
                        notifyError("Failed to get course grade")
                    }
                    else if (status is MainViewModel.Status.Failure) {
                        Log.e(
                            "VIEW",
                            "Failed to get course grade using student id: ${student.id}",
                            status.e
                        )
                        notifyError("Failed to fetch course grade using student id")
                    }
                }
            }
        }
    }

    private inner class StudentRankingAdapter(val studentsRankingList: List<StudentRanking>) :
        RecyclerView.Adapter<StudentRankingAdapter.StudentRankingHolder>() {

        private inner class StudentRankingHolder(itemBinding: ItemRankingBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
            val position = itemBinding.rankingStudentPosition
            val name = itemBinding.rankingStudentName
            val grade = itemBinding.rankingStudentPoints

        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): StudentRankingHolder =
            StudentRankingHolder(
                ItemRankingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: StudentRankingHolder, position: Int) {

            val studentRanking = studentsRankingList[position]
            val pos: Long = position.toLong() + 1

            holder.position.text = pos.toString()
            holder.name.text = studentRanking.name
            holder.grade.text = studentRanking.grade.toString()
        }

        override fun onViewRecycled(holder: StudentRankingHolder) {
            super.onViewRecycled(holder)
            Log.d("APP", "Recycled holder at position ${holder.bindingAdapterPosition}")
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            if(!sessionViewModel.isTeacher){
                studentsRankingList.forEachIndexed { index, student ->
                    if(student.name == studentActual.username){
                        val position: Int = index + 1

                        val rankingStudentText = "Usuário: " + student.name
                        val rankingStudentActualExperience = "Experiência: " + student.grade
                        val rankingStudentActualPosition = "Posição: $position"

                        binding.rankingStudentActual.text = rankingStudentText
                        binding.rankingStudentActualExperience.text = rankingStudentActualExperience
                        binding.rankingStudentActualPosition.text = rankingStudentActualPosition
                    }
                }
            }
            super.onAttachedToRecyclerView(recyclerView)
            binding.root.visibility = View.VISIBLE
        }

        override fun getItemId(position: Int): Long = studentsRankingList[position].grade

        override fun getItemCount(): Int = studentsRankingList.size
    }




    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}