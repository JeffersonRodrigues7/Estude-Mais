package br.edu.ufabc.estude_mais.view.student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.estude_mais.R
import br.edu.ufabc.estude_mais.databinding.StudentFragmentProfileBinding
import br.edu.ufabc.estude_mais.model.Student
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import br.edu.ufabc.estude_mais.viewmodel.SessionViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


class ProfileFragment : Fragment() {

    private lateinit var binding: StudentFragmentProfileBinding
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private var studentId: Long = 0
    private lateinit var studentData: Student

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StudentFragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        studentId = sessionViewModel.getSessionId()
        bindingEvents()
        getProfile()
    }

    fun bindingEvents(){
        binding.updateStudentProfile.setOnClickListener {
            val layout = LinearLayout(this.context)
            layout.orientation = LinearLayout.VERTICAL

            val inputName = EditText(it.context)
            val inputBiography = EditText(it.context)
            inputName.setText(studentData.name)
            inputBiography.setText(studentData.biography)

            layout.addView(inputName)
            layout.addView(inputBiography)

            MaterialAlertDialogBuilder(
                it.context,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_FullWidthButtons
            )
                .setMessage("Digite respectivamente seu nome e sua biogragia")
                .setView(layout)
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Alterar") { _, _ ->
                    mainViewModel.updateStudentData(inputName.text.toString(), inputBiography.text.toString(), studentId).observe(viewLifecycleOwner) { status ->
                        if (status is MainViewModel.Status.Success) try {
                            getProfile()
                            notifyError("Perfil atualizado com sucesso")
                        } catch (e: java.lang.Exception) {
                            Log.e("FRAGMENT", "Failed to update profile", e)
                            notifyError("Failed to update profile")
                        }
                        else if (status is MainViewModel.Status.Failure) {
                            Log.e("VIEW", "Failed to update profile", status.e)
                            notifyError("Failed to update profile")
                        }
                    }
                }
                .show()
        }
    }

    fun getProfile(){
        mainViewModel.getStudentById(studentId).observe(this) { status ->
            if (status is MainViewModel.Status.Success) try {
                studentData =(status.result as MainViewModel.Result.SingleStudent).value

                val level: Long = studentData.experience/100
                val experience: Long = studentData.experience%100

                val profileLevel = "Level $level"
                val profileExperience = "$experience/100"

                binding.profileProgressbar.progress = experience.toInt()
                binding.profileLevel.text = profileLevel
                binding.profileExperienceObtained.text = profileExperience
                binding.studentProfileName.text = studentData.name
                binding.studentProfileUsername.text = studentData.username
                binding.studentProfileEmail.text = studentData.email
                binding.studentProfileBiography.text = studentData.biography

            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to obtain student to use on profile", e)
                notifyError("Failed to obtain student to use on profile")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to obtain student with id $studentId", status.e)
                notifyError("Failed to fetch student to use on profile")
            }
        }
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}