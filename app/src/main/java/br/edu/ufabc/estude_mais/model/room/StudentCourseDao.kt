package br.edu.ufabc.estude_mais.model.room

import androidx.room.*

/**
 * The StudentCourse (join table) DAO.
 */
@Dao
interface StudentCourseDao {
    /**
     * Get a student by its id.
     * @return the student with corresponding courses.
     */
    @Transaction
    @Query("SELECT * FROM studentroom WHERE id=:id")
    fun getStudentCoursesById(id: Long): StudentWithCourses

    /**
     * Get a course by its id.
     * @return the courses with corresponding students.
     */
    @Transaction
    @Query("SELECT * FROM courseroom WHERE id=:id")
    fun getCourseStudentsById(id: Long): CourseWithStudents

    /**
     * Get a student grade by its ids.
     * @return the student with corresponding courses.
     */
    @Transaction
    @Query("SELECT grade FROM studentcourse WHERE studentId=:studentId AND courseId=:courseId")
    fun getStudentGradeByIds(studentId: Long, courseId: Long): Long

    /**
     * verify if a student is registred in a course.
     * @return if he is registred.
     */
    @Transaction
    @Query("SELECT count(studentId) FROM studentcourse WHERE studentId=:studentId AND courseId=:courseId")
    fun checkRegistration(studentId: Long, courseId: Long): Long

    /**
     * Update a student grade by its ids.
     * @param studentId the id
     */
    @Transaction
    @Query("UPDATE studentcourse SET grade = :grade  WHERE studentId=:studentId AND courseId=:courseId")
    fun updateStudentGradeByIds(studentId: Long, courseId: Long, grade: Long)

    /**
     * Delete a course given its id.
     * @param studentId the id
     */
    @Query("DELETE FROM studentcourse WHERE studentId=:studentId AND courseId=:courseId")
    fun deleteById(studentId: Long, courseId: Long)

    /**
     * Delete all student-course given activity.
     * @param courseId the id
     */
    @Query("DELETE FROM studentcourse WHERE courseId=:courseId")
    fun deleteCourse(courseId: Long)

    /**
     * Insert a pair of student and course.
     * @param studentCourse the pair of student and course.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(studentCourse: StudentCourse)
}