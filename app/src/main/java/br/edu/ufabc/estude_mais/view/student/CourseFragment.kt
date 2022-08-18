package br.edu.ufabc.estude_mais.view.student

import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.estude_mais.R
import br.edu.ufabc.estude_mais.databinding.StudentFragmentCourseBinding
import br.edu.ufabc.estude_mais.databinding.StudentItemCourseActivitiesBinding
import br.edu.ufabc.estude_mais.model.Activity
import br.edu.ufabc.estude_mais.model.Course
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import br.edu.ufabc.estude_mais.viewmodel.SessionViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class CourseFragment : Fragment() {
    private val args: CourseFragmentArgs by navArgs()
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: StudentFragmentCourseBinding
    private var studentId: Long = 0
    var activityList: MutableList<Activity> = mutableListOf()
    lateinit var course: Course


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StudentFragmentCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        binding.root.visibility = View.INVISIBLE
        super.onStart()
        studentId = sessionViewModel.getSessionId()
        getCourseView()
        verifyCourseRegistration()
        bindingEvents()
        createActivityAdapter()
    }

    fun getCourseView() {
        mainViewModel.getCourseById(args.courseId).observe(viewLifecycleOwner) { status ->
            if (status is MainViewModel.Status.Success) try {
                course = (status.result as MainViewModel.Result.SingleCourse).value
                setCourseView(course)
            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to obtain course", e)
                notifyError("Failed to obtain course")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to obtain course with id ${args.courseId}", status.e)
                notifyError("Failed to fetch course by id")
            }
        }
    }

    fun verifyCourseRegistration(){
        mainViewModel.verifyStudentCourseRegistration(studentId, args.courseId).observe(viewLifecycleOwner){ status ->
            if (status is MainViewModel.Status.Success) try {
                val registration = (status.result as MainViewModel.Result.Qtd).value

                val leaveCourseText = "deixar"
                val enterCourseText = "Entrar"

                if (registration > 0){//Usuário já registrado
                    binding.enterLeaveCourseButton.text = leaveCourseText
                    binding.enterLeaveCourseButton.setBackgroundColor(RED)
                } else {//Usuário não cadastrado
                    binding.enterLeaveCourseButton.text = enterCourseText
                    binding.enterLeaveCourseButton.setBackgroundColor(GREEN)
                }
            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to verify student course registration", e)
                notifyError("Failed to verify verify student course registration")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to verify student course registration ${studentId}, ${args.courseId}", status.e)
                notifyError("Failed to verify student course registration")
            }
        }
    }

    fun setCourseView(course: Course){
        val courseTeacher = "Professor(a): " + course.name.split("-")[1].trim()
        val courseInitDate = "Criado em: " + course.creation

        binding.courseName.text = course.name.split("-")[0].trim()
        binding.courseTeacher.text = courseTeacher
        binding.studentCourseDescription.text = course.description
        binding.courseInitDate.text = courseInitDate
    }

    fun bindingEvents(){

        binding.radioButtonAllActivities.setOnClickListener {
            createActivityAdapter()
        }

        binding.radioButtonEvaluatedActivities.setOnClickListener {
            binding.recyclerviewCourseActivitiesList.apply {
                mainViewModel.getEvaluatedActivitiesByStudentId(studentId, args.courseId)
                    .observe(viewLifecycleOwner) { status ->
                        if (status is MainViewModel.Status.Success) try {
                            val activities = (status.result as MainViewModel.Result.ActivityList).value
                            if(activities.isNotEmpty()){
                                activityList = activities as MutableList<Activity>
                                adapter = ActivityAdapter(activityList)
                            }else{
                                adapter = ActivityAdapter(emptyList())
                                notifyError("Nenhuma atividade avaliada")
                            }

                        } catch (e: Exception) {
                            Log.e("FRAGMENT", "Failed to obtain activities using course id", e)
                            notifyError("Failed to obtain activities by course id")
                        }
                        else if (status is MainViewModel.Status.Failure) {
                            Log.e(
                                "VIEW",
                                "Failed to obtain activities with course id ${args.courseId}",
                                status.e
                            )
                            notifyError("Failed to fetch activities using course id")
                        }
                    }
            }
        }

        binding.radioButtonNotEvaluatedActivities.setOnClickListener {
            binding.recyclerviewCourseActivitiesList.apply {
                mainViewModel.getNotEvaluatedActivitiesByStudentId(studentId, args.courseId)
                    .observe(viewLifecycleOwner) { status ->
                        if (status is MainViewModel.Status.Success) try {
                            val activities = (status.result as MainViewModel.Result.ActivityList).value
                            if(activities.isNotEmpty()){
                                activityList = activities as MutableList<Activity>
                                adapter = ActivityAdapter(activityList)
                            }else{
                                adapter = ActivityAdapter(emptyList())
                            }
                        } catch (e: Exception) {
                            Log.e("FRAGMENT", "Failed to obtain activities using course id", e)
                            notifyError("Failed to obtain activities by course id")
                        }
                        else if (status is MainViewModel.Status.Failure) {
                            Log.e(
                                "VIEW",
                                "Failed to obtain activities with course id ${args.courseId}",
                                status.e
                            )
                            notifyError("Failed to fetch activities using course id")
                        }
                    }
            }
        }

        binding.courseRankingButton.setOnClickListener {
            CourseFragmentDirections.actionCourseFragmentToNavRanking(args.courseId)
                .let{
                    findNavController().navigate(it)
                }
        }

        binding.enterLeaveCourseButton.setOnClickListener {
            mainViewModel.verifyStudentCourseRegistration(studentId, args.courseId).observe(viewLifecycleOwner){ status ->
                if (status is MainViewModel.Status.Success) try {
                    val registration = (status.result as MainViewModel.Result.Qtd).value

                    if (registration > 0){//Usuário já registrado, vamos cancelar a inscrição
                        deleteRegistryStudentCourse()
                    } else {//Usuário não cadastrado, vamos cadastrá-lo
                        if(course.password.isNotEmpty()){
                            verifyCoursePassword()
                        } else{
                            registryStudentCourse()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("FRAGMENT", "Failed to verify student course registration", e)
                    notifyError("Failed to verify student course registration")
                }
                else if (status is MainViewModel.Status.Failure) {
                    Log.e("VIEW", "Failed to verify student course registration ${studentId}, ${args.courseId}", status.e)
                    notifyError("Failed to verify student course registration")
                }
            }
        }
    }

    fun verifyCoursePassword(){
        val input = EditText(this.context)

        MaterialAlertDialogBuilder(
            this.requireContext(),
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_FullWidthButtons
        )

            .setMessage("Digite a senha do curso")
            .setView(input)
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Entrar") { dialog, _ ->
                mainViewModel.verifyCoursePassword(input.text.toString(), args.courseId).observe(this) { status ->
                    if (status is MainViewModel.Status.Success) try {
                        val courseId = (status.result as MainViewModel.Result.Qtd).value

                        if (courseId > 0){
                            registryStudentCourse()
                            dialog.dismiss()
                        } else {
                            notifyError("Senha inválida")
                        }
                    } catch (e: Exception) {
                        Log.e("FRAGMENT", "Failed to verify course password", e)
                        notifyError("Failed to verify course password")
                    }
                    else if (status is MainViewModel.Status.Failure) {
                        Log.e("VIEW", "Failed to verify course password", status.e)
                        notifyError("Failed to verify course password")
                    }
                }
            }
            .show()
    }

    fun registryStudentCourse(){
        mainViewModel.addStudentCourseRegistration(studentId, args.courseId).observe(viewLifecycleOwner){ status ->
            if (status is MainViewModel.Status.Success) {
                val leaveCourseText = "Deixar"

                binding.enterLeaveCourseButton.text = leaveCourseText
                binding.enterLeaveCourseButton.setBackgroundColor(RED)
                Snackbar.make(binding.root, "Você foi inscrito no curso com sucesso", Snackbar.LENGTH_LONG).show()
                registryStudentActivities()
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("FRAGMENT", "Failed to registry student course", status.e)
                Snackbar.make(binding.root, "Failed to registry student course", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    fun registryStudentActivities(){
        activityList.forEach { activity ->
            mainViewModel.addStudentActivity(studentId, activity.id).observe(viewLifecycleOwner){status ->
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

    fun deleteRegistryStudentCourse(){
        mainViewModel.removeStudentCourseRegistration(studentId, args.courseId).observe(viewLifecycleOwner){ status ->
            if (status is MainViewModel.Status.Success) {
                val enterCourseText = "Entrar"

                binding.enterLeaveCourseButton.text = enterCourseText
                binding.enterLeaveCourseButton.setBackgroundColor(GREEN)
                Snackbar.make(binding.root, "Você saiu do curso", Snackbar.LENGTH_LONG).show()
                deleteRegistryStudentActivities()
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("FRAGMENT", "Failed to delete student course", status.e)
                Snackbar.make(binding.root, "Failed to delete student course", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    fun deleteRegistryStudentActivities(){
        activityList.forEach { activity ->
            mainViewModel.removeStudentActivityRegistration(studentId, activity.id)
                .observe(viewLifecycleOwner) { status ->
                    if (status is MainViewModel.Status.Success) {
                        Log.d("FRAGMENT", "Activities deleted with success")
                    }
                    else if (status is MainViewModel.Status.Failure) {
                        Log.e("FRAGMENT", "Failed to delete students activities", status.e)
                        Snackbar.make(binding.root, "Failed to delete students activities", Snackbar.LENGTH_LONG).show()
                    }
                }
        }
    }

    private inner class ActivityAdapter(val activities: List<Activity>) :
        RecyclerView.Adapter<ActivityAdapter.ActivityHolder>() {

        private inner class ActivityHolder(itemBinding: StudentItemCourseActivitiesBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
            val name = itemBinding.courseActivitiesItemTitle
            val deadline = itemBinding.courseActivitiesItemDeadline
            val grade = itemBinding.courseActivitiesItemGrade

            init {
                itemBinding.root.setOnClickListener {

                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ActivityHolder =
            ActivityHolder(
                StudentItemCourseActivitiesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
            val activity = activities[position]

            mainViewModel.getActivityGrade(studentId, activity.id).observe(viewLifecycleOwner){ status ->
                if (status is MainViewModel.Status.Success) try {
                    val grade = (status.result as MainViewModel.Result.Grade).value

                    val deadlineText = "Deadline: " + activity.formattedDeadline()
                    val gradeText = "Pontuação: " + grade.toString()

                    holder.name.text = activity.name
                    holder.deadline.text = deadlineText
                    holder.grade.text = gradeText

                } catch (e: Exception) {
                    Log.e("FRAGMENT", "Failed to get activity grade using these id's", e)
                    notifyError("Failed to get activity grade using these id's")
                }
                else if (status is MainViewModel.Status.Failure) {
                    Log.e("VIEW", "Failed to get activity grade using these id's $studentId e ${activity.id}", status.e)
                    notifyError("Failed to fetch activity grade using these id's")
                }
            }
        }

        override fun onViewRecycled(holder: ActivityHolder) {
            super.onViewRecycled(holder)
            Log.d("APP", "Recycled holder at position ${holder.bindingAdapterPosition}")
        }

        override fun onViewAttachedToWindow(holder: ActivityHolder) {
            super.onViewAttachedToWindow(holder)
            binding.root.visibility = View.VISIBLE
        }

        override fun getItemId(position: Int): Long = activities[position].id

        override fun getItemCount(): Int = activities.size
    }

    private fun createActivityAdapter(){
        activity?.let {
            binding.recyclerviewCourseActivitiesList.apply {
                mainViewModel.getActivitiesByCourseId(args.courseId).observe(viewLifecycleOwner) { status ->
                    if (status is MainViewModel.Status.Success) try {
                        val activities = (status.result as MainViewModel.Result.ActivityList).value
                        if(activities.isNotEmpty()) {
                            val activityList = activities as MutableList<Activity>
                            if(activityList.size == 0)  binding.root.visibility = View.VISIBLE
                            adapter = ActivityAdapter(activityList)
                        }else{
                            binding.root.visibility = View.VISIBLE
                        }
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