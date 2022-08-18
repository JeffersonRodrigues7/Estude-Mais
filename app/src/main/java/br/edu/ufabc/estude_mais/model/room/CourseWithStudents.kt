package br.edu.ufabc.estude_mais.model.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Relationship between a task and its tags.
 * @property courseRoom the student
 * @property StudentRoom the students
 */
data class CourseWithStudents(
    @Embedded
    val courseRoom: CourseRoom,

    @Relation(
        associateBy = Junction(
            StudentCourse::class,
            parentColumn = "courseId",
            entityColumn = "studentId"
        ),
        parentColumn = "id",
        entityColumn = "id",
        entity = StudentRoom::class
    )
    val students: List<StudentRoom>
)