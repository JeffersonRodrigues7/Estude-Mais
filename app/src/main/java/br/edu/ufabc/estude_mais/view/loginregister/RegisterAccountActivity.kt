package br.edu.ufabc.estude_mais.view.loginregister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.activity.viewModels
import br.edu.ufabc.estude_mais.databinding.ActivityRegisterAccountBinding
import br.edu.ufabc.estude_mais.model.Student
import br.edu.ufabc.estude_mais.model.Teacher
import br.edu.ufabc.estude_mais.view.student.StudentMainActivity
import br.edu.ufabc.estude_mais.view.teacher.TeacherMainActivity
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar


class RegisterAccountActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityRegisterAccountBinding
    private lateinit var studentRegister: RadioButton
    private lateinit var teacherRegister: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginAccount()
        studentRegister = binding.radioButtonStudent
        teacherRegister = binding.radioButtonTeacher
        getMainActivity()
    }

    private fun loginAccount(){
        binding.registerBackButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getMainActivity(){
        binding.buttonRegister.setOnClickListener {
            if(studentRegister.isChecked){
                verifyStudent()
            }else if(teacherRegister.isChecked){
                verifyTeacher()
            }
        }
    }

    private fun verifyTeacher() {
        val teacher = Teacher(
            id = 0,
            name = binding.nameComplete.text?.trim().toString(),
            username = binding.username.text?.trim().toString(),
            email = binding.email.text?.trim().toString(),
            password = binding.password.text?.trim().toString(),
            biography = "",
            courses = null
        )

        viewModel.verifyTeacherExistByUsername(teacher.username).observe(this) { status ->
            if (status is MainViewModel.Status.Success) try {
                val exist = (status.result as MainViewModel.Result.Qtd).value

                if (exist > 0){
                    binding.username.error = "Nome de usuário já em uso"
                    binding.username.showSoftInputOnFocus
                } else if (teacher.name.isEmpty()) {
                    binding.nameComplete.error = "Seu nome não pode ficar em branco"
                    binding.nameComplete.showSoftInputOnFocus
                } else if (teacher.username.length < 5) {
                    binding.username.error = "Nome de usuário deve ter ao menos 5 caracteres"
                    binding.username.showSoftInputOnFocus
                } else if (teacher.email.isEmpty()) {
                    binding.email.error = "E-mail não pode ficar em branco"
                    binding.email.showSoftInputOnFocus
                } else if (teacher.password.length < 4) {
                    binding.password.error = "Senha deve conter ao menos 4 caracteres"
                    binding.password.showSoftInputOnFocus
                } else if (teacher.password.length < 4) {
                    binding.password.error = "Senha deve conter ao menos 4 caracteres"
                    binding.password.showSoftInputOnFocus
                } else if (teacher.password != binding.passwordRepeat.text?.trim().toString()) {
                    binding.passwordRepeat.error = "Senhas não coincidem"
                    binding.passwordRepeat.showSoftInputOnFocus
                } else {
                    addTeacher(teacher)
                }
            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to obtain teacher username", e)
                notifyError("Failed to verify teacher username")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to obtain teacher with username ${teacher.username}", status.e)
                notifyError("Failed to fetch teacher by username")
            }
        }
    }

    private fun addTeacher(teacher: Teacher) {
        viewModel.isLoading.value = true
        viewModel.addTeacher(teacher).observe(this) { status ->
            when (status) {
                is MainViewModel.Status.Success -> {
                    val insertedId = (status.result as MainViewModel.Result.Id).value
                    val intent = Intent(this, TeacherMainActivity::class.java)
                    intent.putExtra("teacher_id", insertedId)
                    startActivity(intent)
                    binding.progressHorizontal.visibility = View.INVISIBLE
                }
                is MainViewModel.Status.Loading -> {
                    binding.progressHorizontal.visibility = View.VISIBLE
                }
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", "Failed to add teacher", status.e)
                    Snackbar.make(binding.root, "Failed to add student", Snackbar.LENGTH_LONG).show()
                    binding.progressHorizontal.visibility = View.INVISIBLE
                }
            }
        }
        viewModel.isLoading.value = false
    }

    private fun verifyStudent() {
        val student = Student(
            id = 0,
            name = binding.nameComplete.text?.trim().toString(),
            username = binding.username.text?.trim().toString(),
            email = binding.email.text?.trim().toString(),
            password = binding.password.text?.trim().toString(),
            biography = "",
            experience = 0,
            allowRankingVisualization = true,
            activitiesDelivered = 0,
            coursesEntered = 0,
            coursesCompleted = 0,
            fullMark = 0,
            topOne = 0,
            courses = null,
            activities = null
        )

        viewModel.verifyStudentExistByUsername(student.username).observe(this) { status ->
            if (status is MainViewModel.Status.Success) try {
                val exist = (status.result as MainViewModel.Result.Qtd).value

                if (exist > 0){
                    binding.username.error = "Nome de usuário já em uso"
                    binding.username.showSoftInputOnFocus
                } else if (student.name.isEmpty()) {
                    binding.nameComplete.error = "Seu nome não pode ficar em branco"
                    binding.nameComplete.showSoftInputOnFocus
                } else if (student.username.length < 5) {
                    binding.username.error = "Nome de usuário deve ter ao menos 5 caracteres"
                    binding.username.showSoftInputOnFocus
                } else if (student.email.isEmpty()) {
                    binding.email.error = "E-mail não pode ficar em branco"
                    binding.email.showSoftInputOnFocus
                } else if (student.password.length < 4) {
                    binding.password.error = "Senha deve conter ao menos 4 caracteres"
                    binding.password.showSoftInputOnFocus
                } else if (student.password.length < 4) {
                    binding.password.error = "Senha deve conter ao menos 4 caracteres"
                    binding.password.showSoftInputOnFocus
                } else if (student.password != binding.passwordRepeat.text?.trim().toString()) {
                    binding.passwordRepeat.error = "Senhas não coincidem"
                    binding.passwordRepeat.showSoftInputOnFocus
                } else {
                    addStudent(student)
                }
            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to obtain student username", e)
                notifyError("Failed to verify student username")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to obtain student with username ${student.username}", status.e)
                notifyError("Failed to fetch student by username")
            }
        }
    }

    private fun addStudent(student: Student) {
        viewModel.isLoading.value = true
        viewModel.addStudent(student).observe(this) { status ->
            when (status) {
                is MainViewModel.Status.Success -> {
                    val insertedId = (status.result as MainViewModel.Result.Id).value
                    val intent = Intent(this, StudentMainActivity::class.java)
                    intent.putExtra("student_id", insertedId)
                    startActivity(intent)
                    binding.progressHorizontal.visibility = View.INVISIBLE
                }
                is MainViewModel.Status.Loading -> {
                    binding.progressHorizontal.visibility = View.VISIBLE
                }
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", "Failed to add student", status.e)
                    Snackbar.make(binding.root, "Failed to add student", Snackbar.LENGTH_LONG).show()
                    binding.progressHorizontal.visibility = View.INVISIBLE
                }
            }
        }
        viewModel.isLoading.value = false
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}