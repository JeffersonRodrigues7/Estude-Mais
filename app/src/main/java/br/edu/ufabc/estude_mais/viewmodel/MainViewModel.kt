package br.edu.ufabc.estude_mais.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import br.edu.ufabc.estude_mais.model.*
import java.util.*

/**
 * The shared view model.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RepositoryFactory(application).create()

    val isLoading = MutableLiveData(false)

    sealed class Status {
        class Failure(val e: Exception) : Status()
        class Success(val result: Result) : Status()
        object Loading : Status()
    }

    sealed class Result {
        data class Id(
            val value: Long
        ) : Result()

        data class Qtd(//Verifica se professor existe
            val value: Long
        ) : Result()

        data class Grade(
            val value: Long
        ) : Result()

        data class SingleStudent(
            val value: Student
        ) : Result()

        data class SingleTeacher(
            val value: Teacher
        ) : Result()

        data class SingleCourse(
            val value: Course
        ) : Result()

        data class SingleActivity(
            val value: Activity
        ) : Result()

        data class CourseList(
            val value: Courses
        ) : Result()

        data class ActivityList(
            val value: Activities
        ) : Result()

        data class StudentList(
            val value: Students
        ) : Result()

        object EmptyResult : Result()
    }

    fun addTeacher(teacher: Teacher) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Id(repository.addTeacher(teacher))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to add teacher element", e)))
        }
    }

    fun addStudent(student: Student) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Id(repository.addStudent(student))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to add student element", e)))
        }
    }

    fun addCourse(course: Course) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Id(repository.addCourse(course))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to add course element", e)))
        }
    }

    fun addStudentCourseRegistration(studentId: Long, courseId: Long) = liveData {
        try{
            repository.addAStudentCourse(studentId, courseId)
            emit(Status.Success(Result.EmptyResult))
        }catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to add student course element", e)))
        }
    }

    fun addStudentActivity(studentId: Long, activityId: Long) = liveData {
        try{
            repository.addAStudentActivity(studentId, activityId)
            emit(Status.Success(Result.EmptyResult))
        }catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to add student activity element", e)))
        }
    }

    fun addActivity(activity: Activity) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Id(repository.addActvitiy(activity))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to add activity element", e)))
        }
    }

    fun verifyTeacherExistByUsername(username: String)  = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Qtd(repository.getQtdTeacherByUsername(username))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to verify teacher name", e)))
        }
    }

    fun verifyTeacherLogin(username: String, password: String)  = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Id(repository.verifyTeacherLogin(username, password))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to verify teacher login", e)))
        }
    }

    fun verifyCoursePassword(password: String, id: Long)  = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Qtd(repository.verifyCourseLogin(password, id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to verify course login", e)))
        }
    }

    fun verifyStudentExistByUsername(username: String)  = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Qtd(repository.getQtdStudentByUsername(username))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to verify student name", e)))
        }
    }

    fun verifyStudentLogin(username: String, password: String)  = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Id(repository.verifyStudentLogin(username, password))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to verify student login", e)))
        }
    }

    fun verifyStudentCourseRegistration(studentId: Long, courseId: Long)  = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Qtd(repository.verifyStudentCourse(studentId, courseId))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to verify student course registration", e)))
        }
    }

    fun getStudentById(id: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.SingleStudent(repository.getStudentsById(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get Student by id", e)))
        }
    }

    fun getAllStudent() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.StudentList(repository.getAllStudents())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get all students", e)))
        }
    }

    fun getStudentExperience(studentId: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Grade(repository.getStudentExperience(studentId))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get experience of student", e)))
        }
    }

    fun getStudentsByCourseId(id: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.StudentList(repository.getStudentsByCourseId(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get Students by course id", e)))
        }
    }

    fun getStudentsByActivityId(id: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.StudentList(repository.getStudentsByActivityId(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get Students using activity id", e)))
        }
    }

    fun getTeacherById(id: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.SingleTeacher(repository.getTeacherByTeacherId(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get Teacher by id", e)))
        }
    }

    fun getCourseById(id: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.SingleCourse(repository.getCourseByCourseId(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get Course by id", e)))
        }
    }

    fun getCoursesByTeacherId(id: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.CourseList(repository.getCoursesByTeacherId(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get Courses by teacher id", e)))
        }
    }

    fun getCoursesByStudentId(id: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.CourseList(repository.getCoursesByStudentId(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get Courses by student id", e)))
        }
    }

    fun getAllCourses() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.CourseList(repository.getAllCourses())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get allCourses", e)))
        }
    }

    fun getActivityById(id: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.SingleActivity(repository.getActivityById(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get Activity by id", e)))
        }
    }

    fun getActivitiesByCourseId(id: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.ActivityList(repository.getActivitiesByCourseId(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get activities by course id", e)))
        }
    }

    fun getEvaluatedActivitiesByStudentId(studentId: Long, courseId: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.ActivityList(repository.getEvaluatedActivitiesByStudentId(studentId, courseId))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get activities by course id", e)))
        }
    }

    fun getNotEvaluatedActivitiesByStudentId(studentId: Long, courseId: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.ActivityList(repository.getNotEvaluatedActivitiesByStudentId(studentId, courseId))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get activities by course id", e)))
        }
    }

    fun getActivityGrade(studentId: Long, activityId: Long) = liveData {
        try {
            emit(Status.Success(Result.Grade(repository.getActivityGradeByIds(studentId, activityId))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get activity grade using these id's", e)))
        }
    }

    fun getCourseGrade(studentId: Long, courseId: Long) = liveData {
        try {
            emit(Status.Success(Result.Grade(repository.getCourseGradeByIds(studentId, courseId))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get course grade using these id's", e)))
        }
    }

    fun updateGrades(studentId: Long, activityId: Long, courseId: Long, newActivityGrade: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.StudentList(repository.updateGrades(studentId, activityId, courseId, newActivityGrade))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to update grades", e)))
        }
    }

    fun updateStudentData(name: String, biography: String, id: Long) = liveData {
        try {
            emit(Status.Loading)
            repository.updateStudentData(name, biography, id)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to update student data", e)))
        }
    }

    fun updateTeacherData(name: String, id: Long) = liveData {
        try {
            emit(Status.Loading)
            repository.updateTeacherData(name, id)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to update teacher data", e)))
        }
    }

    fun updateCourseData(name: String, description: String, id: Long) = liveData {
        try {
            emit(Status.Loading)
            repository.updateCourseData(name, description, id)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to update course data", e)))
        }
    }

    fun updateActivityData(name: String, description: String, deadline: Date, id: Long) = liveData {
        try {
            emit(Status.Loading)
            repository.updateActivityData(name, description, deadline, id)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to update activity data", e)))
        }
    }

    fun removeStudentCourseRegistration(studentId: Long, courseId: Long) = liveData {
        try {
            emit(Status.Loading)
            repository.removeStudentCourseRegistration(studentId, courseId)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to student course registration element", e)))
        }
    }

    fun removeStudentActivityRegistration(studentId: Long, activity: Long) = liveData {
        try {
            emit(Status.Loading)
            repository.removeStudentActivityRegistration(studentId, activity)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed delete to student activity registration element", e)))
        }
    }

    fun removeCourse(courseId: Long) = liveData {
        try {
            emit(Status.Loading)
            repository.removeCourseById(courseId)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to delete course", e)))
        }
    }

    fun removeActivity(acitivity: Long) = liveData {
        try {
            emit(Status.Loading)
            repository.removeActvitiyById(acitivity)
            emit(Status.Success(Result.EmptyResult))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to delete activity", e)))
        }
    }


}
