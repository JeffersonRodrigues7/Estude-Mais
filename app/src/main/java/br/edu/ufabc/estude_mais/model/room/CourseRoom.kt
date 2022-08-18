package br.edu.ufabc.estude_mais.model.room

import androidx.room.PrimaryKey
import androidx.room.Entity
import androidx.room.ForeignKey
import br.edu.ufabc.estude_mais.model.Course

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TeacherRoom::class,
            childColumns = ["teacherId"],
            parentColumns = ["id"],
        )
    ]
)
data class CourseRoom (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val password: String,
    val description: String,
    val creation: String,
    val teacherId: Long
) {
    companion object {
        fun fromCourse(course: Course) = CourseRoom(
            id = course.id,
            name = course.name,
            password = course.password,
            description = course.description,
            creation = course.creation,
            teacherId = course.teacherId,
        )
    }
}