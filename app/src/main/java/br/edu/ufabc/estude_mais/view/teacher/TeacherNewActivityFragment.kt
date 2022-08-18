package br.edu.ufabc.estude_mais.view.teacher

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import br.edu.ufabc.estude_mais.R
import br.edu.ufabc.estude_mais.databinding.TeacherFragmentNewActivityBinding
import br.edu.ufabc.estude_mais.model.Activity
import br.edu.ufabc.estude_mais.model.Student
import br.edu.ufabc.estude_mais.view.DatePickerFragment
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class TeacherNewActivityFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: TeacherFragmentNewActivityBinding
    private val args: TeacherNewActivityFragmentArgs by navArgs()
    var studentList: MutableList<Student> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TeacherFragmentNewActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getStudents()
        bindingEvents()
    }

    fun bindingEvents(){
        binding.deadline.text = Activity.formatDate(Date())

        binding.deadline.setOnClickListener {
            DatePickerFragment(binding.deadline).show(
                requireActivity().supportFragmentManager,
                null
            )
        }

        binding.newactivityCreatebutton.setOnClickListener {
            verifyActivity()
        }
    }

    fun getStudents(){
        mainViewModel.getStudentsByCourseId(args.courseId).observe(viewLifecycleOwner){ status->
            if (status is MainViewModel.Status.Success) try {
                val students = ((status.result as MainViewModel.Result.StudentList).value)
                if(students.isNotEmpty()) {
                    studentList = students as MutableList<Student>
                }else{
                    binding.root.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to obtain students using course id", e)
                notifyError("Failed to obtain students using course id")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to students using course id ${args.courseId}", status.e)
                notifyError("Failed to fetch students using course id")
            }
        }
    }

    fun verifyActivity() {
        val currentDateandTime = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

        val activity = Activity(
            id = 0,
            name = binding.newactivityTitle.text?.trim().toString(),
            description = binding.newactivityDescrption.text?.trim().toString(),
            creation = currentDateandTime,
            deadline = Activity.parseDate(binding.deadline.text.toString()),
            courseId = args.courseId,
            students = null
        )

        if (activity.name.length < 4) {
            binding.newactivityTitle.error = "Nome da atividade muito curto"
            binding.newactivityTitle.showSoftInputOnFocus
        } else {
            addActivity(activity)
        }
    }

    fun addActivity(activity: Activity){
        mainViewModel.isLoading.value = true
        mainViewModel.addActivity(activity).observe(this) { status ->
            when (status) {
                is MainViewModel.Status.Success -> {
                    val insertedId = (status.result as MainViewModel.Result.Id).value
                    registryStudentActivities(insertedId)
                    TeacherNewActivityFragmentDirections.actionTeacherNewActivityFragmentToTeacherActivityFragment(insertedId, args.courseId).let {
                        findNavController().navigate(it,
                            navOptions {
                                popUpTo(R.id.teacherCourseFragment)
                            })
                    }
                    binding.progressHorizontal.visibility = View.INVISIBLE
                }
                is MainViewModel.Status.Loading -> {
                    binding.progressHorizontal.visibility = View.VISIBLE
                }
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", "Failed to add activity", status.e)
                    Snackbar.make(binding.root, "Failed to add activity", Snackbar.LENGTH_LONG).show()
                    binding.progressHorizontal.visibility = View.INVISIBLE
                }
            }
        }
        mainViewModel.isLoading.value = false
    }

    fun registryStudentActivities(activityId: Long){
        studentList.forEach { student ->
            mainViewModel.addStudentActivity(student.id, activityId).observe(viewLifecycleOwner){status ->
                if (status is MainViewModel.Status.Success) {
                    Log.d("FRAGMENT", "Activities registered with success")
                }
                else if (status is MainViewModel.Status.Failure) {
                    Log.e("FRAGMENT", "Failed to registry students activities", status.e)
                    Snackbar.make(binding.root, "Failed to registry students activities", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}