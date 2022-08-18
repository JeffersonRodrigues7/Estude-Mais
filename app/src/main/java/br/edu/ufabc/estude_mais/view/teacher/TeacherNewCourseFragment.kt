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
import br.edu.ufabc.estude_mais.databinding.TeacherFragmentNewCourseBinding
import br.edu.ufabc.estude_mais.model.Course
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class TeacherNewCourseFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: TeacherFragmentNewCourseBinding
    private val args: TeacherNewCourseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TeacherFragmentNewCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        bindingEvents()
    }

    fun bindingEvents(){
        binding.newcourseCreatebutton.setOnClickListener {
            verifyCourse()
        }
    }

    fun verifyCourse(){
        mainViewModel.getTeacherById(args.teacherId).observe(viewLifecycleOwner) { status ->
            if (status is MainViewModel.Status.Success) try {
                val teacherUsername = (status.result as MainViewModel.Result.SingleTeacher).value.username

                if(binding.newcourseTitle.text?.trim().toString().length < 4){
                    binding.newcourseTitle.error = "Nome do curso muito curto"
                    binding.newcourseTitle.showSoftInputOnFocus
                }else{
                    addCourse(teacherUsername)
                }

            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to obtain course teacher username", e)
                notifyError("Failed to obtain course teacher username")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to obtain teacher with id ${args.teacherId}", status.e)
                notifyError("Failed to fetch teacher by id")
            }
        }
    }

    fun addCourse(teacherUsername: String){
        val currentDateandTime = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

        val couseName =  binding.newcourseTitle.text?.trim().toString() + " - " + teacherUsername

        val course = Course (
            id = 0,
            name = couseName,
            password = binding.newcoursePassword.text?.trim().toString(),
            description = binding.newcourseDescription.text?.trim().toString(),
            creation = currentDateandTime,
            teacherId = args.teacherId,
            students = null,
            activities = null
        )

        mainViewModel.isLoading.value = true
        mainViewModel.addCourse(course).observe(this) { status ->
            when (status) {
                is MainViewModel.Status.Success -> {
                    val insertedId = (status.result as MainViewModel.Result.Id).value
                    TeacherNewCourseFragmentDirections.actionTeacherNewCourseFragmentToTeacherCourseFragment(insertedId).let {
                        findNavController().navigate(it,
                            navOptions {
                                popUpTo(R.id.teacher_nav_courses)
                            })
                    }
                    binding.progressHorizontal.visibility = View.INVISIBLE
                }
                is MainViewModel.Status.Loading -> {
                    binding.progressHorizontal.visibility = View.VISIBLE
                }
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", "Failed to add course", status.e)
                    Snackbar.make(binding.root, "Failed to add course", Snackbar.LENGTH_LONG).show()
                    binding.progressHorizontal.visibility = View.INVISIBLE
                }
            }
        }
        mainViewModel.isLoading.value = false
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }



}