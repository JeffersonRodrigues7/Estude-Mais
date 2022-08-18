package br.edu.ufabc.estude_mais.view.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.estude_mais.databinding.StudentFragmentSearchCousesBinding
import br.edu.ufabc.estude_mais.databinding.StudentItemCoursesBinding
import br.edu.ufabc.estude_mais.model.Course
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class CoursesSearchFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: StudentFragmentSearchCousesBinding
    var courseList: MutableList<Course> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StudentFragmentSearchCousesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        bindingEvents()
    }

    private fun bindingEvents() {
        createCourseAdapter()
        searchView()
    }

    private fun searchView(){
        val searchView: SearchView = binding.searchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                filterCourses(text)
                return true
            }
        })
    }

    fun filterCourses(text: String?){
        val filteredCourses: MutableList<Course> = mutableListOf()

        courseList.map { course ->
            if (text != null) {
                if(course.name.lowercase().contains(text.lowercase())){
                    filteredCourses.add(course)
                }
            }
        }

        binding.recyclerviewSearchCoursesList.apply {
            adapter = CourseAdapter(filteredCourses)
        }
    }

    private inner class CourseAdapter(var courses: List<Course>) :
        RecyclerView.Adapter<CourseAdapter.CourseHolder>() {

        private inner class CourseHolder(itemBinding: StudentItemCoursesBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {

            val name = itemBinding.courseStudentName

            init {
                itemBinding.root.setOnClickListener {
                    CoursesSearchFragmentDirections.
                    actionCoursesSearchFragmentToCourseFragment(getItemId(bindingAdapterPosition))
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

        override fun getItemId(position: Int): Long = courses[position].id

        override fun getItemCount(): Int = courses.size
    }

    private fun createCourseAdapter(){
        activity?.let {
            binding.recyclerviewSearchCoursesList.apply {
                mainViewModel.getAllCourses().observe(viewLifecycleOwner) { status ->
                    if (status is MainViewModel.Status.Success) try {
                        val courses = ((status.result as MainViewModel.Result.CourseList).value)
                        if(courses.isNotEmpty()) {
                            courseList = courses as MutableList<Course>
                            adapter = CourseAdapter(courseList)
                            addItemDecoration(
                                DividerItemDecoration(
                                    this.context,
                                    DividerItemDecoration.VERTICAL
                                )
                            )
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