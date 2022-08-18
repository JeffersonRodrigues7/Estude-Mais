package br.edu.ufabc.estude_mais.view.loginregister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import androidx.activity.viewModels
import br.edu.ufabc.estude_mais.databinding.ActivityLoginBinding
import br.edu.ufabc.estude_mais.view.student.StudentMainActivity
import br.edu.ufabc.estude_mais.view.teacher.TeacherMainActivity
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var studentLogin: RadioButton
    private lateinit var teacherLogin: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)//layoutInflater traz basicamente o xml
        setContentView(binding.root)//passando o layout xml pra View

        studentLogin = binding.radioButtonStudent
        teacherLogin = binding.radioButtonTeacher
        createAccount()
        getMainActivity()
    }

    private fun createAccount(){
        binding.createAccountButton.setOnClickListener {

            val intent = Intent(this, RegisterAccountActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun getMainActivity(){
        binding.buttonLogin.setOnClickListener {
            if(studentLogin.isChecked){
                verifyStudent()
            }else if(teacherLogin.isChecked){
                verifyTeacher()
            }

        }
    }

    private fun verifyTeacher() {
        val username = binding.usernameUsernameText.text?.trim().toString()
        val password = binding.passwordText.text?.trim().toString()

        viewModel.verifyTeacherLogin(username, password).observe(this) { status ->
            if (status is MainViewModel.Status.Success) try {
                val teacherId = (status.result as MainViewModel.Result.Id).value

                if (teacherId > 0){
                    val intent = Intent(this, TeacherMainActivity::class.java)
                    intent.putExtra("teacher_id", teacherId)
                    startActivity(intent)
                } else {
                    notifyError("Usuário Inexistente")
                }
            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to verify teacher login", e)
                notifyError("Failed to verify teacher")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to verify teacher with username $username", status.e)
                notifyError("Failed to verify teacher by username and password")
            }
        }
    }

    private fun verifyStudent() {
        val username = binding.usernameUsernameText.text?.trim().toString()
        val password = binding.passwordText.text?.trim().toString()

        viewModel.verifyStudentLogin(username, password).observe(this) { status ->
            if (status is MainViewModel.Status.Success) try {
                val studentId = (status.result as MainViewModel.Result.Id).value

                if (studentId > 0){
                    val intent = Intent(this, StudentMainActivity::class.java)
                    intent.putExtra("student_id", studentId)
                    startActivity(intent)
                } else {
                    notifyError("Usuário Inexistente")
                }
            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to verify student login", e)
                notifyError("Failed to verify student")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to verify student with username $username", status.e)
                notifyError("Failed to verify student by username and password")
            }
        }
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}