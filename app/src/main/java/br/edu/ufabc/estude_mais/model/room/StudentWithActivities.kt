package br.edu.ufabc.estude_mais.model.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Relationship between a task and its tags.
 * @property studentRoom the student
 * @property ActivityRoom the activities
 */
data class StudentWithActivities(
    @Embedded
    val studentRoom: StudentRoom,

    @Relation(
        associateBy = Junction(
            StudentCourse::class,
            parentColumn = "studentId",
            entityColumn = "activityId"
        ),
        parentColumn = "id",
        entityColumn = "id",
        entity = ActivityRoom::class
    )
    val activities: List<ActivityRoom>
)