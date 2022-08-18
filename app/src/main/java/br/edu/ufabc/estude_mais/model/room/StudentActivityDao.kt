package br.edu.ufabc.estude_mais.model.room

import androidx.room.*

/**
 * The StudentActivity (join table) DAO.
 */
@Dao
interface StudentActivityDao {
    /**
     * Get a student by its id.
     * @return the student with corresponding activities.
     */
    @Transaction
    @Query("SELECT * FROM studentroom WHERE id=:id")
    fun getStudentActivitiesById(id: Long): StudentWithActivities

    /**
     * Get a student by its id.
     * @return the student with corresponding evaluated activities.
     */
    @Transaction
    @Query("SELECT activityId FROM studentactivity WHERE studentId=:studentId AND activityId=:activityId AND completed = 1")
    fun getStudentEvaluatedActivityById(studentId: Long, activityId: Long): Long

    /**
     * Get a student by its id.
     * @return the student with corresponding not evaluated activities.
     */
    @Transaction
    @Query("SELECT activityId FROM studentactivity WHERE studentId=:studentId AND activityId=:activityId AND completed = 0")
    fun getStudentNotEvaluatedActivitiesById(studentId: Long, activityId: Long): Long

    /**
     * Get a activity by its id.
     * @return the activity with corresponding students.
     */
    @Transaction
    @Query("SELECT * FROM activityroom WHERE id=:id")
    fun getActivityStudentsById(id: Long): ActivityWithStudents

    /**
     * Get a student grade by its ids.
     * @return the student with corresponding courses.
     */
    @Transaction
    @Query("SELECT grade FROM studentactivity WHERE studentId=:studentId AND activityId=:activityId")
    fun getStudentGradeByIds(studentId: Long, activityId: Long): Long

    /**
     * Update a student grade by its ids.
     * @param studentId the studentId
     */
    @Transaction
    @Query("UPDATE studentactivity SET grade = :grade, completed = 1 WHERE studentId=:studentId AND activityId=:activityId")
    fun updateStudentGradeByIds(studentId: Long, activityId: Long, grade: Long)

    /**
     * Delete a student-activity given its id.
     * @param studentId the id
     */
    @Query("DELETE FROM studentactivity WHERE studentId=:studentId AND activityId=:activityId")
    fun deleteById(studentId: Long, activityId: Long)

    /**
     * Delete all student-activity given activity.
     * @param activityId the id
     */
    @Query("DELETE FROM studentactivity WHERE activityId=:activityId")
    fun deleteActivity(activityId: Long)

    /**
     * Insert a pair of student and activity.
     * @param studentActivity the pair of student and activity.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(studentActivity: StudentActivity)
}