package br.edu.ufabc.estude_mais.view.teacher

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.estude_mais.R
import br.edu.ufabc.estude_mais.databinding.TeacherFragmentActivityBinding
import br.edu.ufabc.estude_mais.databinding.TeacherItemActivityStudentsBinding
import br.edu.ufabc.estude_mais.model.Activity
import br.edu.ufabc.estude_mais.model.Student
import br.edu.ufabc.estude_mais.view.DatePickerFragment
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class TeacherActivityFragment : Fragment() {
    private val args: TeacherActivityFragmentArgs by navArgs()
    private lateinit var binding: TeacherFragmentActivityBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var activityData: Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TeacherFragmentActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        binding.root.visibility = View.INVISIBLE
        super.onStart()
        bindingEvents()
        getCourseActivity()
        createActivityAdapter()
    }

    fun bindingEvents(){

        binding.updateActivityProfile.setOnClickListener {
            val layout = LinearLayout(this.context)
            layout.orientation = LinearLayout.VERTICAL

            val inputName = EditText(it.context)
            val inputDescription = EditText(it.context)
            val inputDeadline = TextView(it.context)
            inputName.setText(activityData.name)
            inputDescription.setText(activityData.description)
            inputDeadline.setText(activityData.formattedDeadline())

            layout.addView(inputName)
            layout.addView(inputDescription)
            layout.addView(inputDeadline)

            inputDeadline.setOnClickListener {
                DatePickerFragment(inputDeadline).show(
                    requireActivity().supportFragmentManager,
                    null
                )
            }

            MaterialAlertDialogBuilder(
                it.context,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_FullWidthButtons
            )
                .setMessage("Altere respectivamente o nome, a descrição e a deadline da atividade")
                .setView(layout)
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Alterar") { _, _ ->
                    mainViewModel.updateActivityData(inputName.text.toString(), inputDescription.text.toString(), Activity.parseDate(inputDeadline.text.toString())!!, args.activityId).observe(viewLifecycleOwner) { status ->
                        if (status is MainViewModel.Status.Success) try {
                            getCourseActivity()
                            notifyError("Atividade atualizado com sucesso")
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

        binding.activityDeleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(
                it.context,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_FullWidthButtons
            )
                .setMessage("Realmente desejar excluir essa atividade?")
                .setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Sim") { _, _ ->
                    mainViewModel.removeActivity(args.activityId).observe(viewLifecycleOwner) { status ->
                        if (status is MainViewModel.Status.Success) try {
                            TeacherActivityFragmentDirections.actionTeacherActivityFragmentToTeacherCourseFragment(args.courseId)
                                .let { view ->
                                    findNavController().navigate(view,
                                        navOptions {
                                            popUpTo(R.id.teacherCourseFragment)
                                        })
                                }
                            notifyError("Atividade removida com sucesso")
                        } catch (e: Exception) {
                            Log.e("FRAGMENT", "Failed to remove activity", e)
                            notifyError("Failed to remove activity")
                        }
                        else if (status is MainViewModel.Status.Failure) {
                            Log.e("VIEW", "Failed to remove activity", status.e)
                            notifyError("Failed to remove activity")
                        }
                    }
                }
                .show()
        }
    }


    fun getCourseActivity(){
        mainViewModel.getActivityById(args.activityId).observe(viewLifecycleOwner) {status ->
            if (status is MainViewModel.Status.Success) try {
                activityData = (status.result as MainViewModel.Result.SingleActivity).value
                setActivity(activityData)
            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to obtain activity", e)
                notifyError("Failed to obtain activity")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to obtain course with id ${args.activityId}", status.e)
                notifyError("Failed to fetch activity by id")
            }
        }
    }

    fun setActivity(activity: Activity){
        val teacherActivityInitDate = "Criado em: " + activity.creation
        val teacherActivityDeadlineDate = "Deadline: " + activity.formattedDeadline()

        binding.teacherActivityName.text = activity.name
        binding.teacherActivityDescription.text = activity.description
        binding.teacherActivityInitDate.text = teacherActivityInitDate
        binding.teacherActivityDeadlineDate.text = teacherActivityDeadlineDate
    }

    private inner class StudentAdapter(val students: List<Student>) :
        RecyclerView.Adapter<StudentAdapter.StudentHolder>() {

        private inner class StudentHolder(itemBinding: TeacherItemActivityStudentsBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
            val studentName = itemBinding.activityStudentsItemName
            val grade = itemBinding.activityStudentsItemGrade

            init {
                itemBinding.root.setOnClickListener {
                    val input = EditText(it.context)
                    input.inputType = InputType.TYPE_CLASS_NUMBER

                    MaterialAlertDialogBuilder(
                        it.context,
                        R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_FullWidthButtons
                    )
                        .setMessage("Digite a pontuação do aluno")
                        .setView(input)
                        .setNegativeButton("Cancelar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("Marcar como completa") { _, _ ->
                            if(input.text.toString().toLong() < 0 ||  input.text.toString().toLong() > 100){
                                notifyError("A nota deve ser entre 0 e 100")
                            }else{
                                mainViewModel.updateGrades(getItemId(bindingAdapterPosition), args.activityId, args.courseId, input.text.toString().toLong()).observe(viewLifecycleOwner) { status ->
                                    if (status is MainViewModel.Status.Success) try {
                                        val students = (status.result as MainViewModel.Result.StudentList).value
                                        binding.recyclerviewTeacherActivityStudentsList.apply {
                                            adapter = StudentAdapter(students)
                                        }
                                        notifyError("Notas atualizadas com sucesso")
                                    } catch (e: Exception) {
                                        Log.e("FRAGMENT", "Failed to update grades", e)
                                        notifyError("Failed to update grades")
                                    }
                                    else if (status is MainViewModel.Status.Failure) {
                                        Log.e("VIEW", "Failed to update grades", status.e)
                                        notifyError("Failed to update grades")
                                    }
                                }
                            }
                        }
                        .show()
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): StudentHolder =
            StudentHolder(
                TeacherItemActivityStudentsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: StudentHolder, position: Int) {
            val student = students[position]

            mainViewModel.getActivityGrade(student.id, args.activityId).observe(viewLifecycleOwner){ status ->
                if (status is MainViewModel.Status.Success) try {
                    val grade = (status.result as MainViewModel.Result.Grade).value

                    val gradetext = "Nota: " + grade.toString()

                    holder.studentName.text = student.name
                    holder.grade.text = gradetext

                } catch (e: Exception) {
                    Log.e("FRAGMENT", "Failed to get activity grade using these id's", e)
                    notifyError("Failed to get activity grade using these id's")
                }
                else if (status is MainViewModel.Status.Failure) {
                    Log.e("VIEW", "Failed to get activity grade using these id's ${student.id} e ${args.activityId}", status.e)
                    notifyError("Failed to fetch activity grade using these id's")
                }
            }
        }

        override fun onViewRecycled(holder: StudentHolder) {
            super.onViewRecycled(holder)
            Log.d("APP", "Recycled holder at position ${holder.bindingAdapterPosition}")
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            binding.root.visibility = View.VISIBLE
        }

        override fun getItemId(position: Int): Long = students[position].id

        override fun getItemCount(): Int = students.size
    }

    private fun createActivityAdapter(){
        activity?.let {
            binding.recyclerviewTeacherActivityStudentsList.apply {
                mainViewModel.getStudentsByActivityId(args.activityId).observe(viewLifecycleOwner) { status ->
                    if (status is MainViewModel.Status.Success) try {
                        val students = (status.result as MainViewModel.Result.StudentList).value
                        adapter = StudentAdapter(students)
                    } catch (e: Exception) {
                        Log.e("FRAGMENT", "Failed to obtain students using activity id", e)
                        notifyError("Failed to obtain students using activity id")
                    }
                    else if (status is MainViewModel.Status.Failure) {
                        Log.e("VIEW", "Failed to obtain students using activity id ${args.activityId}", status.e)
                        notifyError("Failed to fetch students using activity id")
                    }
                }
            }
        }
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}