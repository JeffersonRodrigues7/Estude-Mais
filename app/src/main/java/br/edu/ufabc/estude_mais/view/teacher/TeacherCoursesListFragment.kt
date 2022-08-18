package br.edu.ufabc.estude_mais.view.teacher

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.estude_mais.databinding.TeacherFragmentCoursesListBinding
import br.edu.ufabc.estude_mais.databinding.TeacherItemCoursesBinding
import br.edu.ufabc.estude_mais.model.Course
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import br.edu.ufabc.estude_mais.viewmodel.SessionViewModel
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class TeacherCoursesListFragment : Fragment() {
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: TeacherFragmentCoursesListBinding
    private var teacherSessionId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TeacherFragmentCoursesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        binding.root.visibility = View.INVISIBLE
        super.onStart()
        bindingEvents()
    }

    private fun bindingEvents() {
        teacherSessionId = sessionViewModel.getSessionId()

        binding.coursesNewCourseTextview.setOnClickListener {
            TeacherCoursesListFragmentDirections.actionToTeacherNewCourseFragment(teacherSessionId)
                .let {
                    findNavController().navigate(it)
                }
        }

        createCourseAdapter()
    }

    private inner class CourseAdapter(val courses: List<Course>) :
        RecyclerView.Adapter<CourseAdapter.CourseHolder>() {

        private inner class CourseHolder(itemBinding: TeacherItemCoursesBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
            val name = itemBinding.courseTeacherName

            init {
                itemBinding.root.setOnClickListener {
                    TeacherCoursesListFragmentDirections.
                    actionTeacherNavCoursesToTeacherCourseFragment(getItemId(bindingAdapterPosition))
                        .let {
                            findNavController().navigate(it)
                        }
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CourseHolder =
            CourseHolder(
                TeacherItemCoursesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: CourseHolder, position: Int) {
            val course = courses[position]

            holder.name.text = course.name.split("-")[0].trim()
        }

        override fun onViewRecycled(holder: CourseHolder) {
            super.onViewRecycled(holder)
            Log.d("APP", "Recycled holder at position ${holder.bindingAdapterPosition}")
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            binding.root.visibility = View.VISIBLE
        }

        override fun getItemId(position: Int): Long = courses[position].id

        override fun getItemCount(): Int = courses.size
    }

    private fun createCourseAdapter(){
        activity?.let {
            binding.recyclerviewCoursesList.apply {
                mainViewModel.getCoursesByTeacherId(teacherSessionId).observe(viewLifecycleOwner) { status ->
                    if (status is MainViewModel.Status.Success) try {
                        val courses = (status.result as MainViewModel.Result.CourseList).value
                        adapter = CourseAdapter(courses)
                        addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
                    } catch (e: Exception) {
                        Log.e("FRAGMENT", "Failed to obtain courses using teacher id", e)
                        notifyError("Failed to obtain courses by teacher id")
                    }
                    else if (status is MainViewModel.Status.Failure) {
                        Log.e("VIEW", "Failed to obtain courses with  teacher id $teacherSessionId", status.e)
                        notifyError("Failed to fetch courses using teacher id")
                    }
                }
            }
        }
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}