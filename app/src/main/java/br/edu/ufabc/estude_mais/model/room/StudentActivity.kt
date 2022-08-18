package br.edu.ufabc.estude_mais.model.room

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = StudentRoom::class,
            childColumns = ["studentId"],
            parentColumns = ["id"],
        ),
        ForeignKey(
            entity = ActivityRoom::class,
            childColumns = ["activityId"],
            parentColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["studentId", "activityId"]
)
data class StudentActivity(
    val studentId: Long,
    val activityId: Long,
    val grade: Long,
    val completed: Boolean
)