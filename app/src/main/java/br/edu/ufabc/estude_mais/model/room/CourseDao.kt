package br.edu.ufabc.estude_mais.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CourseDao {
    /**
     * Insert a new course.
     * @param courseRoom the task
     * @return the inserted id
     */
    @Insert
    fun insert(courseRoom: CourseRoom): Long

    /**
     * Update a course given its id.
     * @param id the id
     */
    @Query("UPDATE courseroom SET description = :description WHERE  id=:id")
    fun update(description: String, id: Long)

    /**
     * Update a course given its id.
     * @param id the id
     */
    @Query("UPDATE courseroom SET name = :name, description = :description WHERE  id=:id")
    fun updateData(name: String, description: String, id: Long)

    /**
     * verify course password
     * @return the course
     */
    @Transaction
    @Query("SELECT count(id) FROM courseroom WHERE password=:password AND id=:id")
    fun verifyLogin(password: String, id: Long): Long

    /**
     * Get courses by teacher id.
     * @return the list of courses with corresponding id.
     */
    @Transaction
    @Query("SELECT * FROM courseroom WHERE teacherId=:id")
    fun getByTeacherId(id: Long): List<CourseRoom>

    /**
     * Get courses.
     * @return the list of courses.
     */
    @Transaction
    @Query("SELECT * FROM courseroom")
    fun getAll(): List<CourseRoom>

    /**
     * Get courses by its name.
     * @return the list of courses with corresponding name.
     */
    @Transaction
    @Query("SELECT * FROM courseroom WHERE name=:name")
    fun getByName(name: String): CourseRoom

    /**
     * Get courses by its id.
     * @return the list of courses with corresponding name.
     */
    @Transaction
    @Query("SELECT * FROM courseroom WHERE id=:id")
    fun getById(id: Long): CourseRoom

    /**
     * Delete a course given its id.
     * @param id the id
     */
    @Query("DELETE FROM courseroom WHERE id=:id")
    fun deleteById(id: Long)
}