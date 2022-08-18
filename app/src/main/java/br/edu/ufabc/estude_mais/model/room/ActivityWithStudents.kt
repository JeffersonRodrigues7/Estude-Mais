package br.edu.ufabc.estude_mais.model.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Relationship between a task and its tags.
 * @property activityRoom the student
 */
data class ActivityWithStudents(
    @Embedded
    val activityRoom: ActivityRoom,

    @Relation(
        associateBy = Junction(
            StudentActivity::class,
            parentColumn = "activityId",
            entityColumn = "studentId"
        ),
        parentColumn = "id",
        entityColumn = "id",
        entity = StudentRoom::class
    )
    val students: List<StudentRoom>
)