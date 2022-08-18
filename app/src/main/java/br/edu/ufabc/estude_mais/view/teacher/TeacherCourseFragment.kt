package br.edu.ufabc.estude_mais.view.teacher

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.estude_mais.R
import br.edu.ufabc.estude_mais.databinding.TeacherFragmentCourseBinding
import br.edu.ufabc.estude_mais.databinding.TeacherItemCourseActivitiesBinding
import br.edu.ufabc.estude_mais.model.Activity
import br.edu.ufabc.estude_mais.model.Course
import br.edu.ufabc.estude_mais.model.Teacher
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import br.edu.ufabc.estude_mais.viewmodel.SessionViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class TeacherCourseFragment : Fragment() {
    private lateinit var binding: TeacherFragmentCourseBinding
    private val args: TeacherCourseFragmentArgs by navArgs()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private lateinit var courseData: Course
    private lateinit var teacher: Teacher

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TeacherFragmentCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        binding.root.visibility = View.INVISIBLE
        super.onStart()
        getTeacher()
        getCourse()
        bindingEvents()
        createCourseAdapter()
    }

    private fun getTeacher(){
        mainViewModel.getTeacherById(sessionViewModel.getSessionId()).observe(viewLifecycleOwner) { status ->
            if (status is MainViewModel.Status.Success) try {
                teacher = (status.result as MainViewModel.Result.SingleTeacher).value
            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to obtain teacher", e)
                notifyError("Failed to obtain teacher")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to obtain teacher with id ${sessionViewModel.getSessionId()}", status.e)
                notifyError("Failed to fetch teacher")
            }
        }
    }

    private fun bindingEvents() {

        binding.updateCourseProfile.setOnClickListener {
            val layout = LinearLayout(this.context)
            layout.orientation = LinearLayout.VERTICAL

            val inputName = EditText(it.context)
            val inputDescription = EditText(it.context)
            inputName.setText(courseData.name.split("-")[0])
            inputDescription.setText(courseData.description)

            layout.addView(inputName)
            layout.addView(inputDescription)

            MaterialAlertDialogBuilder(
                it.context,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_FullWidthButtons
            )
                .setMessage("Digite respectivamente o nome e a descrição do curso")
                .setView(layout)
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Alterar") { _, _ ->
                    mainViewModel.updateCourseData(inputName.text.toString() + " - " + teacher.username, inputDescription.text.toString(), args.courseId).observe(viewLifecycleOwner) { status ->
                        if (status is MainViewModel.Status.Success) try {
                            getCourse()
                            notifyError("Curso atualizado com sucesso")
                        } catch (e: Exception) {
                            Log.e("FRAGMENT", "Failed to course profile", e)
                            notifyError("Failed to update profile")
                        }
                        else if (status is MainViewModel.Status.Failure) {
                            Log.e("VIEW", "Failed to course profile", status.e)
                            notifyError("Failed to update profile")
                        }
                    }
                }
                .show()
        }

        binding.coursesDeleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(
                it.context,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_FullWidthButtons
            )
                .setMessage("Realmente desejar excluir esse curso?")
                .setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Sim") { _, _ ->
                        mainViewModel.removeCourse(args.courseId).observe(viewLifecycleOwner) { status ->
                            if (status is MainViewModel.Status.Success) try {
                                TeacherCourseFragmentDirections.actionTeacherCourseFragmentToTeacherNavCourses()
                                    .let { navDirection ->
                                        findNavController().navigate(navDirection,
                                            navOptions {
                                                popUpTo(R.id.teacher_nav_courses)
                                            })
                                    }
                                notifyError("Curso removido com sucesso")
                            } catch (e: Exception) {
                                Log.e("FRAGMENT", "Failed to remove course", e)
                                notifyError("Failed to remove course")
                            }
                            else if (status is MainViewModel.Status.Failure) {
                                Log.e("VIEW", "Failed to remove course", status.e)
                                notifyError("Failed to remove course")
                            }
                        }
                }
                .show()
        }

        binding.courseRankingButton.setOnClickListener {
            TeacherCourseFragmentDirections.actionTeacherCourseFragmentToRankingFragment(args.courseId)
                .let{
                    findNavController().navigate(it)
                }
        }

        binding.coursesNewActivityTextview.setOnClickListener {
            TeacherCourseFragmentDirections.actionTeacherCourseFragmentToTeacherNewActivityFragment(args.courseId)
                .let {
                    findNavController().navigate(it)
                }
        }
    }

    fun getCourse() {
        mainViewModel.getCourseById(args.courseId).observe(viewLifecycleOwner) { status ->
            if (status is MainViewModel.Status.Success) try {
                courseData = (status.result as MainViewModel.Result.SingleCourse).value
                setCourse(courseData)
            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to obtain course  username", e)
                notifyError("Failed to obtain course")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to obtain course with id ${args.courseId}", status.e)
                notifyError("Failed to fetch course by id")
            }
        }
    }

    fun setCourse(course: Course){
        val teacherCourseInitDate = "Curso criado em: " + course.creation

        binding.teacherCourseName.text = course.name.split("-")[0]
        binding.teacherCourseDescription.text = course.description
        binding.teacherCourseInitDate.text = teacherCourseInitDate
    }

    private inner class ActivityAdapter(val activities: List<Activity>) :
        RecyclerView.Adapter<ActivityAdapter.ActivityHolder>() {

        private inner class ActivityHolder(itemBinding: TeacherItemCourseActivitiesBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
            val name = itemBinding.teacherCourseActivitiesItemTitle
            val deadline = itemBinding.teacherCourseActivitiesItemDeadline

            init {
                itemBinding.root.setOnClickListener {
                    TeacherCourseFragmentDirections.
                    actionTeacherCourseFragmentToTeacherActivityFragment(getItemId(bindingAdapterPosition), args.courseId)
                        .let {
                            findNavController().navigate(it)
                        }
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ActivityHolder =
            ActivityHolder(
                TeacherItemCourseActivitiesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
            val activity = activities[position]
            val deadlineText = "Deadline: " + activity.formattedDeadline()

            holder.name.text = activity.name
            holder.deadline.text = deadlineText
        }

        override fun onViewRecycled(holder: ActivityHolder) {
            super.onViewRecycled(holder)
            Log.d("APP", "Recycled holder at position ${holder.bindingAdapterPosition}")
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            binding.root.visibility = View.VISIBLE
        }

        override fun getItemId(position: Int): Long = activities[position].id

        override fun getItemCount(): Int = activities.size
    }

    private fun createCourseAdapter(){
        activity?.let {
            binding.recyclerviewTeacherCourseActivitiesList.apply {
                mainViewModel.getActivitiesByCourseId(args.courseId).observe(viewLifecycleOwner) { status ->
                    if (status is MainViewModel.Status.Success) try {
                        val activities = (status.result as MainViewModel.Result.ActivityList).value
                        adapter = ActivityAdapter(activities)
                    } catch (e: Exception) {
                        Log.e("FRAGMENT", "Failed to obtain activities using course id", e)
                        notifyError("Failed to obtain activities by course id")
                    }
                    else if (status is MainViewModel.Status.Failure) {
                        Log.e("VIEW", "Failed to obtain activities with course id ${args.courseId}", status.e)
                        notifyError("Failed to fetch activities using course id")
                    }
                }
            }
        }
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}