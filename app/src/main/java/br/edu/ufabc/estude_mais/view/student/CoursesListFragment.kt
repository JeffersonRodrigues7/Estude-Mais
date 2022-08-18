package br.edu.ufabc.estude_mais.view.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.estude_mais.databinding.StudentFragmentCoursesListBinding
import br.edu.ufabc.estude_mais.databinding.StudentItemCoursesBinding
import br.edu.ufabc.estude_mais.model.Course
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import br.edu.ufabc.estude_mais.viewmodel.SessionViewModel
import com.google.android.material.snackbar.Snackbar

class CoursesListFragment : Fragment() {
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: StudentFragmentCoursesListBinding
    var courseList: MutableList<Course> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StudentFragmentCoursesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        binding.root.visibility = View.INVISIBLE
        super.onStart()
        bindingEvents()
    }

    private fun bindingEvents() {
        createCourseAdapter()
        findingNewCourse()
    }

    private fun findingNewCourse(){
        binding.searchNewCourseButton.setOnClickListener {
            CoursesListFragmentDirections.actionStudentNavCoursesToCoursesSearchFragment()
                .let {
                    findNavController().navigate(it)
                }
        }

    }

    private inner class CourseAdapter(var courses: List<Course>) :
        RecyclerView.Adapter<CourseAdapter.CourseHolder>() {

        private inner class CourseHolder(itemBinding: StudentItemCoursesBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {

            val name = itemBinding.courseStudentName

            init {
                itemBinding.root.setOnClickListener {
                    CoursesListFragmentDirections.
                    actionStudentNavCoursesToCourseFragment(getItemId(bindingAdapterPosition))
                        .let{
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
                StudentItemCoursesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: CourseHolder, position: Int) {
            val course = courses[position]

            holder.name.text = course.name
        }

        override fun onViewRecycled(holder: CourseHolder) {
            super.onViewRecycled(holder)
            Log.d("APP", "Recycled holder at position ${holder.bindingAdapterPosition}")
        }

        override fun onViewAttachedToWindow(holder: CourseHolder) {
            super.onViewAttachedToWindow(holder)
            binding.root.visibility = View.VISIBLE
        }

        override fun getItemId(position: Int): Long = courses[position].id

        override fun getItemCount(): Int = courses.size
    }

    private fun createCourseAdapter(){
        activity?.let {
            binding.recyclerviewCoursesList.apply {
                mainViewModel.getCoursesByStudentId(sessionViewModel.getSessionId()).observe(viewLifecycleOwner) { status ->
                    if (status is MainViewModel.Status.Success) try {
                        val courses = ((status.result as MainViewModel.Result.CourseList).value)
                        if(courses.isNotEmpty()){
                            courseList = courses as MutableList<Course>
                            if(courseList.size == 0) binding.root.visibility = View.VISIBLE
                            adapter = CourseAdapter(courseList)
                            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
                        }else{
                            binding.root.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        Log.e("FRAGMENT", "Failed to obtain courses", e)
                        notifyError("Failed to obtain courses")
                    }
                    else if (status is MainViewModel.Status.Failure) {
                        Log.e("VIEW", "Failed to obtain courses", status.e)
                        notifyError("Failed to fetch courses")
                    }
                }
            }
        }
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}