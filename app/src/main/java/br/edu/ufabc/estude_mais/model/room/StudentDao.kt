package br.edu.ufabc.estude_mais.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface StudentDao {
    /**
     * Insert a new task.
     * @param studentRoom the student
     * @return the inserted id
     */
    @Insert
    fun insert(studentRoom: StudentRoom): Long

    /**
     * Get a student by its username.
     * @return the student
     */
    @Transaction
    @Query("SELECT * FROM studentroom WHERE username=:username")
    fun getByUsername(username: String): StudentRoom

    /**
     * Get a qtd of student by its username.
     * @return the qtd
     */
    @Transaction
    @Query("SELECT count(username) FROM studentroom WHERE username=:username")
    fun getQtdByUsername(username: String): Long

    /**
     * Get a student by its username and password.
     * @return the student id
     */
    @Transaction
    @Query("SELECT id FROM studentroom WHERE username=:username AND password=:password")
    fun verifyLogin(username: String, password: String): Long

    /**
     * Get a teacher by its id.
     * @return the student
     */
    @Transaction
    @Query("SELECT * FROM studentroom WHERE id=:id")
    fun getById(id: Long): StudentRoom

    /**
     * Get a teacher by its id.
     * @return the student
     */
    @Transaction
    @Query("SELECT * FROM studentroom")
    fun getAllStudents(): List<StudentRoom>

    /**
     * Get a experience by its id.
     * @return the experience
     */
    @Transaction
    @Query("SELECT experience FROM studentroom WHERE id=:id")
    fun getExperienceById(id: Long): Long

    /**
     * Update a biography student given its id.
     * @param id the id
     */
    @Query("UPDATE studentroom SET biography = :biography WHERE  id=:id")
    fun updateBiography(biography: String, id: Long)

    /**
     * Update a biography and name student given its id.
     * @param id the id
     */
    @Query("UPDATE studentroom SET biography = :biography, name = :name WHERE  id=:id")
    fun updateData(name: String, biography: String, id: Long)

    /**
     * Update a experience student given its id.
     * @param id the id
     */
    @Query("UPDATE studentroom SET experience = :experience WHERE  id=:id")
    fun updateExperienceGradeById(id: Long, experience: Long)

}