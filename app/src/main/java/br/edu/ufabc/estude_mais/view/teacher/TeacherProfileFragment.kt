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
import br.edu.ufabc.estude_mais.R
import br.edu.ufabc.estude_mais.databinding.TeacherFragmentProfileBinding
import br.edu.ufabc.estude_mais.model.Student
import br.edu.ufabc.estude_mais.model.Teacher
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import br.edu.ufabc.estude_mais.viewmodel.SessionViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class TeacherProfileFragment : Fragment() {

    private lateinit var binding: TeacherFragmentProfileBinding
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private var teacherId: Long = 0
    private lateinit var teacherData: Teacher

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TeacherFragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        teacherId = sessionViewModel.getSessionId()
        getProfile()
        bindingEvents()
    }

    fun bindingEvents(){
        binding.updateTeacherProfile.setOnClickListener {
            val inputName = EditText(it.context)
            inputName.setText(teacherData.name)

            MaterialAlertDialogBuilder(
                it.context,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_FullWidthButtons
            )
                .setMessage("Altere seu nome aqui")
                .setView(inputName)
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Alterar") { _, _ ->
                    mainViewModel.updateTeacherData(inputName.text.toString(), teacherId).observe(viewLifecycleOwner) { status ->
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
        mainViewModel.getTeacherById(teacherId).observe(this) { status ->
            if (status is MainViewModel.Status.Success) try {
                teacherData =(status.result as MainViewModel.Result.SingleTeacher).value
                binding.teacherProfileName.text = teacherData.name
                binding.teacherProfileUsername.text = teacherData.username
                binding.teacherProfileEmail.text = teacherData.email

            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to obtain teacher to use on profile", e)
                notifyError("Failed to obtain teacher to use on profile")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to obtain teacher with id $teacherId", status.e)
                notifyError("Failed to fetch teacher to use on profile")
            }
        }
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}