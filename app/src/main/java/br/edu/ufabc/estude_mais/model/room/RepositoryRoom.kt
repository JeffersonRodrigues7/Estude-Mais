package br.edu.ufabc.estude_mais.model.room

import android.app.Application
import androidx.room.Room
import br.edu.ufabc.estude_mais.model.*
import br.edu.ufabc.estude_mais.model.Activity
import br.edu.ufabc.estude_mais.model.Course
import br.edu.ufabc.estude_mais.model.Student
import br.edu.ufabc.estude_mais.model.Teacher
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class RepositoryRoom(application: Application) : Repository {
    private val db: AppDatabase by lazy {//Inicializando preguiçosa com o banco de dados na thread principal apenas na primeira vez que o app for aberto
        Room.databaseBuilder(application, AppDatabase::class.java, "estudemais").build()
    }

    override suspend fun getTeacherByTeacherId(id: Long): Teacher = withContext(Dispatchers.IO) {
        db.withTransaction {
            val teacher = db.teacherDao().getByTeacherId(id)
            val courses = db.courseDao().getByTeacherId(id)

            Teacher (
                id = teacher.id,
                email = teacher.email,
                username = teacher.username,
                name = teacher.name,
                password = teacher.password,
                biography = teacher.biography,
                courses = courses.map {it.name}
            )
        }
    }

    override suspend fun getQtdTeacherByUsername(userName: String): Long = withContext(Dispatchers.IO) {
        db.teacherDao().getQtdByUsername(userName)
    }

    override suspend fun verifyTeacherLogin(username: String, password: String): Long = withContext(Dispatchers.IO) {
        db.teacherDao().verifyLogin(username, password)
    }

    override suspend fun verifyCourseLogin(password: String, id: Long): Long = withContext(Dispatchers.IO) {
        db.courseDao().verifyLogin(password, id)
    }

    override suspend fun getStudentsByCourseId(id: Long): Students = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.studentCourseDao().getCourseStudentsById(id).students.map{ student ->
                getStudent(student)
            }.let { students ->
                students.sortedBy { it.username }
            }
        }
    }

    override suspend fun getStudentsByActivityId(id: Long): Students = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.studentActivityDao().getActivityStudentsById(id).students.map {student ->
                getStudent(student)
            }.sortedBy { it.username }
        }
    }

    override suspend fun getStudentsByUserName(userName: String): Student = withContext(Dispatchers.IO) {
        db.studentDao().getByUsername(userName).let{ student ->
            getStudent(student)
        }
    }

    override suspend fun getQtdStudentByUsername(userName: String): Long = withContext(Dispatchers.IO) {
        db.studentDao().getQtdByUsername(userName)
    }

    override suspend fun verifyStudentLogin(username: String, password: String): Long = withContext(Dispatchers.IO) {
        db.studentDao().verifyLogin(username, password)
    }

    override suspend fun getAllStudents(): Students = withContext(Dispatchers.IO) {
        db.studentDao().getAllStudents().map {student ->
            getStudent(student)
        }.sortedBy { it.username }
    }

    override suspend fun getStudentsById(id: Long): Student = withContext(Dispatchers.IO) {
        db.studentDao().getById(id).let { student ->
            getStudent(student)
        }
    }

    suspend fun getStudent(student: StudentRoom): Student  = withContext(Dispatchers.IO) {
        db.withTransaction {
            val coursesName = db.studentCourseDao()
                .getStudentCoursesById(student.id).courses.map { course -> course.name }
            val activityName = db.studentActivityDao()
                .getStudentActivitiesById(student.id).activities.map { activity -> activity.name }

            Student(
                id = student.id,
                email = student.email,
                username = student.username,
                name = student.name,
                password = student.password,
                biography = student.biography,
                experience = student.experience,
                allowRankingVisualization = student.allowRankingVisualization,
                activitiesDelivered = student.activitiesDelivered,
                coursesEntered = student.coursesEntered,
                coursesCompleted = student.coursesCompleted,
                fullMark = student.fullMark,
                topOne = student.topOne,
                courses = coursesName,
                activities = activityName
            )
        }
    }

    override suspend fun getCourseGradeByIds(studentId: Long, courseId: Long): Long = withContext(Dispatchers.IO) {
        db.studentCourseDao().getStudentGradeByIds(studentId, courseId)
    }

    override suspend fun getActivityGradeByIds(studentId: Long, activityId: Long): Long = withContext(Dispatchers.IO) {
        db.studentActivityDao().getStudentGradeByIds(studentId, activityId)
    }

    override suspend fun getCoursesByStudentId(id: Long): Courses = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.studentCourseDao().getStudentCoursesById(id).courses.map {course ->
                getCourse(course)
            }.sortedBy { it.name }
        }
    }

    override suspend fun getCoursesByTeacherId(id: Long): Courses = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.courseDao().getByTeacherId(id).map {course ->
                getCourse(course)
            }
        }
    }

    override suspend fun verifyStudentCourse(studentId: Long, courseId: Long): Long = withContext(Dispatchers.IO) {
        db.studentCourseDao().checkRegistration(studentId, courseId)
    }

    override suspend fun getAllCourses(): Courses = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.courseDao().getAll().map {course ->
                getCourse(course)
            }.sortedBy { it.name }
        }
    }

    override suspend fun getCourseByCourseName(name: String): Course = withContext(Dispatchers.IO) {
        db.courseDao().getByName(name).let { course ->
            getCourse(course)
        }
    }

    override suspend fun getCourseByCourseId(id: Long): Course = withContext(Dispatchers.IO) {
        db.courseDao().getById(id).let { course ->
            getCourse(course)
        }
    }

    suspend fun getCourse(course: CourseRoom): Course  = withContext(Dispatchers.IO) {
        db.withTransaction {
            val studentsUserName = db.studentCourseDao().getCourseStudentsById(course.id).students.map { student -> student.username }
            val activitiesName = db.activityDao().getByCourseId(course.id).map { activity -> activity.name }

            Course(
                id = course.id,
                name = course.name,
                password = course.password,
                description = course.description,
                creation = course.creation,
                teacherId = course.teacherId,
                students = studentsUserName,
                activities = activitiesName
            )
        }
    }

    override suspend fun getActivityById(id: Long): Activity = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.activityDao().getById(id).let { activity ->
                getActivity(activity)
            }
        }
    }

    override suspend fun getActivitiesByStudentId(id: Long): Activities = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.studentActivityDao().getStudentActivitiesById(id).activities.map {activity ->
                getActivity(activity)
            }
        }.sortedBy { it.deadline }
    }

    override suspend fun getEvaluatedActivitiesByStudentId(studentId: Long, courseId: Long): Activities = withContext(Dispatchers.IO) {
        db.withTransaction {
            //Pegando todas atividades do curso
            val activities = getActivitiesByCourseId(courseId)

            ///Pegando os ids das atividades completadas do curso
            val activitiesIdEvaluated: List<Long> = activities.map{
                db.studentActivityDao().getStudentEvaluatedActivityById(studentId, it.id)
            }

            //Pegando apenas o resultados que foram maiores que 0, ou seja, com ID existemte
            val activitiesIdNotNull = activitiesIdEvaluated.filter { it>0 }

            //Pegando a atividades pelos ids
            activitiesIdNotNull.map{
                    getActivityById(it)
            }.sortedBy { it.deadline }
        }
    }

    override suspend fun getNotEvaluatedActivitiesByStudentId(studentId: Long, courseId: Long): Activities = withContext(Dispatchers.IO) {
        db.withTransaction {
            //Pegando todas atividades do curso
            val activities = getActivitiesByCourseId(courseId)

            ///Pegando os ids das atividades completadas do curso
            val activitiesIdNotEvaluated: List<Long> = activities.map{
                db.studentActivityDao().getStudentNotEvaluatedActivitiesById(studentId, it.id)
            }

            //Pegando apenas o resultados que foram maiores que 0, ou seja, com ID existemte
            val activitiesIdNotNull = activitiesIdNotEvaluated.filter { it>0 }

            //Pegando a atividades pelos ids
            activitiesIdNotNull.map{
                getActivityById(it)
            }.sortedBy { it.deadline }
        }
    }

    override suspend fun getActivitiesByCourseId(id: Long): Activities = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.activityDao().getByCourseId(id).map { activity ->
                getActivity(activity)
            }
        }.sortedBy { it.deadline }
    }

    override suspend fun getCourseByActivityName(name: String): Activity = withContext(Dispatchers.IO) {
        db.activityDao().getByName(name).let { activity ->
            getActivity(activity)
        }
    }

    override suspend fun getCourseByActivityId(id: Long): Activity = withContext(Dispatchers.IO) {
        db.activityDao().getById(id).let { activity ->
            getActivity(activity)
        }
    }

    suspend fun getActivity(activity: ActivityRoom): Activity = withContext(Dispatchers.IO) {
        db.withTransaction {
            val studentsUserName = db.studentActivityDao()
                .getActivityStudentsById(activity.id).students.map { student -> student.username }

            Activity(
                id = activity.id,
                name = activity.name,
                description = activity.description,
                creation = activity.creation,
                deadline = activity.deadline,
                courseId = activity.courseId,
                students = studentsUserName
            )
        }
    }

    override suspend fun addTeacher(teacher: Teacher): Long = withContext(Dispatchers.IO) {
        db.teacherDao().insert(TeacherRoom.fromTeacher(teacher))
    }

    override suspend fun addStudent(student: Student): Long = withContext(Dispatchers.IO) {
        db.studentDao().insert(StudentRoom.fromStudent(student))
    }

    override suspend fun addCourse(course: Course): Long = withContext(Dispatchers.IO) {
        db.courseDao().insert(CourseRoom.fromCourse(course))
    }

    override suspend fun addActvitiy(activity: Activity): Long = withContext(Dispatchers.IO) {
        db.activityDao().insert(ActivityRoom.fromActivity(activity))
    }

    override suspend fun addAStudentCourse(studentId: Long, courseId: Long) = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.studentCourseDao().insert(
                StudentCourse (
                    studentId = studentId,
                    courseId = courseId,
                    activityId = 0,
                    grade = 0
                )
            )

            val activities = getActivitiesByCourseId(courseId)
            activities.forEach {
                addAStudentActivity(studentId, it.id)
            }

        }

    }

    override suspend fun addAStudentActivity(studentId: Long, activityId: Long) = withContext(Dispatchers.IO) {
       db.withTransaction {
           db.studentActivityDao().insert(
               StudentActivity (
                   studentId = studentId,
                   activityId = activityId,
                   grade = 0,
                   completed = false
               )
            )
       }
    }

    override suspend fun updateTeacher(teacher: Teacher) = withContext(Dispatchers.IO) {
        db.teacherDao().updateBiography(teacher.biography, teacher.id)
    }

    override suspend fun updateStudent(student: Student) = withContext(Dispatchers.IO) {
        db.studentDao().updateBiography(student.biography, student.id)
    }

    override suspend fun updateStudentData(name: String, biography: String, id: Long) = withContext(Dispatchers.IO) {
        db.studentDao().updateData(name, biography, id)
    }

    override suspend fun updateTeacherData(name: String, id: Long) = withContext(Dispatchers.IO) {
        db.teacherDao().updateData(name, id)
    }

    override suspend fun updateCourseData(name: String, description: String, id: Long) = withContext(Dispatchers.IO) {
        db.courseDao().updateData(name, description, id)
    }

    override suspend fun updateActivityData(name: String, description: String, deadline: Date, id: Long) = withContext(Dispatchers.IO) {
        db.activityDao().updateData(name, description, deadline, id)
    }

    override suspend fun updateCourse(course: Course) = withContext(Dispatchers.IO) {
        db.courseDao().update(course.description, course.id)
    }

    override suspend fun updateActivity(activity: Activity) = withContext(Dispatchers.IO) {
        db.activityDao().updateDescription(activity.description, activity.id)
    }

    override suspend fun updateActivityGrade(studentId: Long, activityId: Long, grade: Long) = withContext(Dispatchers.IO) {
        db.studentActivityDao().updateStudentGradeByIds(studentId, activityId, grade)
    }

    override suspend fun updateGrades(studentId: Long, activityId: Long, courseId: Long, newActivityGrade: Long): Students = withContext(Dispatchers.IO) {
        db.withTransaction {
            val oldActivityGrade: Long = db.studentActivityDao().getStudentGradeByIds(studentId, activityId)
            val oldCourseGrade: Long = db.studentCourseDao().getStudentGradeByIds(studentId, courseId)
            val oldStudentExperience: Long = db.studentDao().getExperienceById(studentId)

            val differenceActivityGrade: Long = newActivityGrade - oldActivityGrade
            val newCourseGrade: Long = oldCourseGrade + differenceActivityGrade
            val newStudentExperience: Long = oldStudentExperience + differenceActivityGrade

            db.studentActivityDao().updateStudentGradeByIds(studentId, activityId, newActivityGrade)
            db.studentCourseDao().updateStudentGradeByIds(studentId, courseId, newCourseGrade)
            db.studentDao().updateExperienceGradeById(studentId, newStudentExperience)

            getStudentsByActivityId(activityId)
        }
    }

    override suspend fun getStudentExperience(studentId: Long): Long = withContext(Dispatchers.IO){
        db.studentDao().getExperienceById(studentId)
    }

    override suspend fun removeCourseById(id: Long) = withContext(Dispatchers.IO) {
        db.withTransaction {
            val activities = db.activityDao().getByCourseId(id)
            activities.forEach {
                removeActvitiyById(it.id)
            }

            db.courseDao().deleteById(id)
            db.studentCourseDao().deleteCourse(id)
        }
    }

    override suspend fun removeActvitiyById(id: Long) = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.activityDao().deleteById(id)
            db.studentActivityDao().deleteActivity(id)
        }
    }

    override suspend fun removeStudentCourseRegistration(studentId: Long, courseId: Long) = withContext(Dispatchers.IO) {
        db.studentCourseDao().deleteById(studentId, courseId)
    }

    override suspend fun removeStudentActivityRegistration(studentId: Long, activityId: Long) = withContext(Dispatchers.IO) {
        db.studentActivityDao().deleteById(studentId, activityId)
    }
}