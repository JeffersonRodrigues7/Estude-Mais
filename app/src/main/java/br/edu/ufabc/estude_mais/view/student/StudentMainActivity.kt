package br.edu.ufabc.estude_mais.view.student

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.edu.ufabc.estude_mais.R
import br.edu.ufabc.estude_mais.databinding.StudentActivityMainBinding
import br.edu.ufabc.estude_mais.model.Student
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import br.edu.ufabc.estude_mais.viewmodel.SessionViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


class StudentMainActivity : AppCompatActivity() {
    private val sessionViewModel: SessionViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: StudentActivityMainBinding
    private var studentId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = StudentActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(//Opções da NavView, basicamente os destinos
            setOf(R.id.student_nav_courses, R.id.ranking_nav_courses, R.id.student_nav_profile), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)//Sets up the ActionBar for use with a NavController.
        navView.setupWithNavController(navController)

        studentId = intent.getLongExtra("student_id", 0)
        sessionViewModel.setSessionId(studentId)

        bindingEvents()
    }

    fun bindingEvents() {
        mainViewModel.getStudentById(studentId).observe(this) { status ->
            if (status is MainViewModel.Status.Success) try {
                val student: Student = (status.result as MainViewModel.Result.SingleStudent).value
                val level: Long = student.experience/100
                val experience: Long = student.experience%100

                val navbarLevel = findViewById<View>(R.id.navbar_student_level) as TextView
                val navbarExperience = findViewById<View>(R.id.navbar_student_experience) as TextView
                val navbarName = findViewById<View>(R.id.navbar_student_name_textview) as TextView
                val navbarEmail = findViewById<View>(R.id.navbar_student_email_textview) as TextView
                val navbarPorgress = findViewById<View>(R.id.progressBar) as ProgressBar


                val levelText = "Level " + level.toString()
                val experienceText = experience.toString()+"/100"

                navbarLevel.text = levelText
                navbarExperience.text = experienceText
                navbarPorgress.progress = experience.toInt()
                navbarName.text = student.name
                navbarEmail.text = student.email
            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to obtain student to use on navbar", e)
                notifyError("Failed to obtain student to use on navbar")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to obtain student with id $studentId", status.e)
                notifyError("Failed to fetch student to use on navbar")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}