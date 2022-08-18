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
            entity = CourseRoom::class,
            childColumns = ["courseId"],
            parentColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["studentId", "courseId"]
)
data class StudentCourse(
    val studentId: Long,
    val courseId: Long,
    val activityId: Long,
    val grade: Long
)