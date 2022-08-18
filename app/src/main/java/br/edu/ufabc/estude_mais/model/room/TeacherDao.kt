package br.edu.ufabc.estude_mais.model.room

import androidx.room.*

@Dao
interface TeacherDao {

    /**
     * Insert a new task.
     * @param teacherRoom the task
     * @return the inserted id
     */
    @Insert
    fun insert(teacherRoom: TeacherRoom): Long

    /**
     * Get a teacher by its username.
     * @return the teacher
     */
    @Transaction
    @Query("SELECT count(username) FROM teacherroom WHERE username=:username")
    fun getQtdByUsername(username: String): Long

    /**
     * Get a teacher by its username.
     * @return the teacher
     */
    @Transaction
    @Query("SELECT id FROM teacherroom WHERE username=:username AND password=:password")
    fun verifyLogin(username: String, password: String): Long

    /**
     * Update a teacher given its id.
     * @param id the id
     */
    @Query("UPDATE teacherroom SET biography = :biography WHERE  id=:id")
    fun updateBiography(biography: String, id: Long)

    /**
     * Update a biography and name student given its id.
     * @param id the id
     */
    @Query("UPDATE teacherroom SET name = :name WHERE  id=:id")
    fun updateData(name: String, id: Long)

    /**
     * Get a teacher by its id.
     * @return the teacher
     */
    @Transaction
    @Query("SELECT * FROM teacherRoom WHERE id=:id")
    fun getByTeacherId(id: Long): TeacherRoom

}