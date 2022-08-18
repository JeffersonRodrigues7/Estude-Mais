package br.edu.ufabc.estude_mais.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import java.util.*

@Dao
interface ActivityDao {
    /**
     * Insert a new course.
     * @return the inserted id
     */
    @Insert
    fun insert(activityRoom: ActivityRoom): Long

    /**
     * Get activites by its name.
     * @return the list of activites with corresponding name.
     */
    @Transaction
    @Query("SELECT * FROM activityroom WHERE name=:name")
    fun getByName(name: String): ActivityRoom

    /**
     * Get activites by its id.
     * @return the list of activites with corresponding id.
     */
    @Transaction
    @Query("SELECT * FROM activityroom WHERE id=:id")
    fun getById(id: Long): ActivityRoom

    /**
     * Update a activity given its id.
     * @param id the id
     */
    @Query("UPDATE activityroom SET description = :description WHERE  id=:id")
    fun updateDescription(description: String, id: Long)

    /**
     * Update a activity given its id.
     * @param id the id
     */
    @Query("UPDATE activityroom SET name = :name, description = :description, deadline = :deadline WHERE  id=:id")
    fun updateData(name: String, description: String, deadline: Date, id: Long)

    /**
     * Get activities by course id.
     * @return the list of activities with corresponding course id.
     */
    @Transaction
    @Query("SELECT * FROM activityroom WHERE courseId=:id")
    fun getByCourseId(id: Long): List<ActivityRoom>

    /**
     * Delete a activity given its id.
     * @param id the id
     */
    @Query("DELETE FROM activityroom WHERE id=:id")
    fun deleteById(id: Long)
}