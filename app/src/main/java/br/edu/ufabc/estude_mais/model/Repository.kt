package br.edu.ufabc.estude_mais.model

import java.util.*

/**
 * General API for repositories.
 */
interface Repository {

    /**
     * get teacher name by course
     * @param id the course
     * @return the Teacher
     */
    suspend fun getTeacherByTeacherId(id: Long): Teacher

    /**
     * @param userName the teacher
     * @return qtd of teacher
     */
    suspend fun getQtdTeacherByUsername(userName: String): Long

    /**
     * @param username and password the teacher
     * @return teacher id
     */
    suspend fun verifyTeacherLogin(username: String, password: String): Long

    /**
     * Fetch a list of students that contain a course.
     * @param id the course
     * @return the list of Students
     */
    suspend fun getStudentsByCourseId(id: Long): Students

    /**
     * Fetch a list of students that contain a activity.
     * @param id the activity
     * @return the list of Students
     */
    suspend fun getStudentsByActivityId(id: Long): Students

    /**
     * @param userName the student
     * @return Student
     */
    suspend fun getStudentsByUserName(userName: String): Student

    /**
     * @param userName the student
     * @return qtd of student
     */
    suspend fun getQtdStudentByUsername(userName: String): Long

    /**
     * @param username and password the student
     * @return teacher id
     */
    suspend fun verifyStudentLogin(username: String, password: String): Long

    /**
     * @param id the student
     * @return Student
     */
    suspend fun getStudentsById(id: Long): Student

    /**
     * @param studentId the student
     * @return Student experience
     */
    suspend fun getStudentExperience(studentId: Long): Long

    /**
     * @param
     * @return all Student
     */
    suspend fun getAllStudents(): Students

    /**
     * @param password used
     * @return if course exist
     */
    suspend fun verifyCourseLogin(password: String, id: Long): Long

    /**
     * @param studentId and courseId
     * @return the grade
     */
    suspend fun getCourseGradeByIds(studentId: Long, courseId: Long): Long

    /**
     * @param activityId and studentId
     * @return the grade
     */
    suspend fun getActivityGradeByIds(studentId: Long, activityId: Long): Long

    /**
     * Fetch a list of courses that contain a student.
     * @param id the student
     * @return the list of Courses
     */
    suspend fun getCoursesByStudentId(id: Long): Courses

    /**
     * Fetch a list of courses that contain a student.
     * @param id the student
     * @return the list of Courses
     */
    suspend fun getCoursesByTeacherId(id: Long): Courses

    /**
     * Fetch a list of courses
     * @param name the course
     * @return the list of Courses
     */
    suspend fun getCourseByCourseName(name: String): Course

    /**
     * Fetch a list of courses
     * @param id the course
     * @return the list of Courses
     */
    suspend fun getCourseByCourseId(id: Long): Course

    /**
     * @param studentId and course id
     * @return if he is registred
     */
    suspend fun verifyStudentCourse(studentId: Long, courseId: Long): Long

    /**
     * Fetch a list of courses
     * @return the list of Courses
     */
    suspend fun getAllCourses(): Courses

    /**
     * Fetch a list of activities that contain a student.
     * @param id the student
     * @return the list of activities
     */
    suspend fun getActivitiesByStudentId(id: Long): Activities

    /**
     * Fetch a list of evaluated activities that contain a student.
     * @param studentId the student
     * @return the list of activities
     */
    suspend fun getEvaluatedActivitiesByStudentId(studentId: Long, courseId: Long): Activities

    /**
     * Fetch a list of not evaluated activities that contain a student.
     * @param studentId the student
     * @return the list of activities
     */
    suspend fun getNotEvaluatedActivitiesByStudentId(studentId: Long, courseId: Long): Activities

    /**
     * Fetch a list of activities that contain a course.
     * @param id the activity
     * @return the list of activities
     */
    suspend fun getActivitiesByCourseId(id: Long): Activities

    /**
     * @param id the activity
     * @return Activity
     */
    suspend fun getActivityById(id: Long): Activity

    /**
     * Fetch a list of activity
     * @param name the activity
     * @return the list of activity
     */
    suspend fun getCourseByActivityName(name: String): Activity

    /**
     * Fetch a list of activity
     * @param id the activity
     * @return the list of activity
     */
    suspend fun getCourseByActivityId(id: Long): Activity

    /**
     * Add a new Teacher.
     * @param teacher the teacher
     * @return the id of the added item
     */
    suspend fun addTeacher(teacher: Teacher): Long

    /**
     * Add a new student.
     * @param student the student
     * @return the id of the added item
     */
    suspend fun addStudent(student: Student): Long

    /**
     * Add a new Course.
     * @param course the course
     * @return the id of the added item
     */
    suspend fun addCourse(course: Course): Long

    /**
     * Add a new Activity.
     * @param activity the Activity
     * @return the id of the added item
     */
    suspend fun addActvitiy(activity: Activity): Long

    /**
     * Add a new Course and student.
     * @param studentId and student
     * @return nothing
     */
    suspend fun addAStudentCourse(studentId: Long, courseId: Long)

    /**
     * Add a new Activity and student.
     * @param studentId and student
     * @return nothing
     */
    suspend fun addAStudentActivity(studentId: Long, activityId: Long)

    /**
     * Update a Teacher.
     * @param teacher a Teacher with the same id as a stored Teacher.
     */
    suspend fun updateTeacher(teacher: Teacher)

    /**
     * Update a Student.
     * @param student a Student with the same id as a stored Student.
     */
    suspend fun updateStudent(student: Student)

    /**
     * Update a Student.
     * @param name a Student
     */
    suspend fun updateStudentData(name: String, biography: String, id: Long)

    /**
     * Update a teacher.
     * @param name a teacher
     */
     suspend fun updateTeacherData(name: String, id: Long)

    /**
     * Update a course.
     * @param name a course
     */
    suspend fun updateCourseData(name: String, description: String, id: Long)

    /**
     * Update a activity.
     * @param name a activity
     */
    suspend fun updateActivityData(name: String, description: String, deadline: Date, id: Long)

    /**
     * Update a Course.
     * @param course a Course with the same id as a stored Student.
     */
    suspend fun updateCourse(course: Course)

    /**
     * Update a Activity.
     * @param activity a Activity with the same id as a stored Student.
     */
    suspend fun updateActivity(activity: Activity)

    /**
     * Update a Activity grdde of a student.
     * @param studentId, acitivityID e grade
     */
    suspend fun updateActivityGrade(studentId: Long, activityId: Long, grade: Long)

    /**
     * Update activity, course and student grades/experience
     * @param studentId, acitivityID, courseId and newActivityId
     */
    suspend fun updateGrades(studentId: Long, activityId: Long, courseId: Long, newActivityGrade: Long): Students

    /**
     * Remove a Course by id.
     * @param id the id
     */
    suspend fun removeCourseById(id: Long)

    /**
     * Remove a Actvitiy by id.
     * @param id the id
     */
    suspend fun removeActvitiyById(id: Long)

    /**
     * Remove a a student registration
     * @param studentId and course id
     */
    suspend fun removeStudentCourseRegistration(studentId: Long, courseId: Long)

    /**
     * Remove a student registration
     * @param studentId and course id
     */
    suspend fun removeStudentActivityRegistration(studentId: Long, activityId: Long)

}
