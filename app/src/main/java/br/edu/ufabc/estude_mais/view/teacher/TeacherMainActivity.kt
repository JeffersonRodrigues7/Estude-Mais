package br.edu.ufabc.estude_mais.view.teacher

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.edu.ufabc.estude_mais.R
import br.edu.ufabc.estude_mais.databinding.TeacherActivityMainBinding
import br.edu.ufabc.estude_mais.model.Teacher
import br.edu.ufabc.estude_mais.viewmodel.MainViewModel
import br.edu.ufabc.estude_mais.viewmodel.SessionViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


class TeacherMainActivity : AppCompatActivity() {
    private val sessionViewModel: SessionViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: TeacherActivityMainBinding
    private var teacherId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = TeacherActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.teacherAppBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.teacherNavView
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.teacher_nav_host_fragment_content_main) as NavHostFragment
        val navController =
            navHostFragment.navController


        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.teacher_nav_courses, R.id.ranking_nav_courses, R.id.teacher_nav_profile),
            drawerLayout
        )

        setupActionBarWithNavController(
            navController,
            appBarConfiguration
        )
        navView.setupWithNavController(navController)

        teacherId = intent.getLongExtra("teacher_id", 0)
        sessionViewModel.setSessionId(teacherId)
        sessionViewModel.isTeacher = true

        bindingEvents()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.teacher_nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun bindingEvents() {
        mainViewModel.getTeacherById(teacherId).observe(this) { status ->
            if (status is MainViewModel.Status.Success) try {
                val teacher: Teacher =
                    (status.result as MainViewModel.Result.SingleTeacher).value

                val navbarName =
                    findViewById<View>(R.id.navbar_teacher_name_textview) as TextView
                val navbarEmail = findViewById<View>(R.id.nav_header_teacher_email) as TextView

                navbarName.text = teacher.name
                navbarEmail.text = teacher.email
            } catch (e: Exception) {
                Log.e("FRAGMENT", "Failed to obtain teacher to use on navbar", e)
                notifyError("Failed to obtain teacher to use on navbar")
            }
            else if (status is MainViewModel.Status.Failure) {
                Log.e("VIEW", "Failed to obtain teacher with id $teacherId", status.e)
                notifyError("Failed to fetch teacher to use on navbar")
            }
        }
    }


    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}