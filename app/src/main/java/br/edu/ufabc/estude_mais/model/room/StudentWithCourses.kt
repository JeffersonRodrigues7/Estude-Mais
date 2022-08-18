package br.edu.ufabc.estude_mais.model.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Relationship between a task and its tags.
 * @property studentRoom the student
 * @property courses the courses
 */
data class StudentWithCourses(
    @Embedded
    val studentRoom: StudentRoom,

    @Relation(
        associateBy = Junction(
            StudentCourse::class,
            parentColumn = "studentId",
            entityColumn = "courseId"
        ),
        parentColumn = "id",
        entityColumn = "id",
        entity = CourseRoom::class
    )
    val courses: List<CourseRoom>
)