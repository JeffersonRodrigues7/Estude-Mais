package br.edu.ufabc.estude_mais.model.room

import androidx.room.PrimaryKey
import androidx.room.Entity
import androidx.room.Index
import br.edu.ufabc.estude_mais.model.Student

@Entity(    indices = [
    Index(value = ["email"], unique = true),
    Index(value = ["username"], unique = true),
])
data class StudentRoom (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val email: String,
    val username: String,
    val name: String,
    val password: String,
    val biography: String,
    val experience: Long,
    val allowRankingVisualization: Boolean,
    val activitiesDelivered: Long,
    val coursesEntered: Long,
    val coursesCompleted: Long,
    val fullMark: Long,
    val topOne: Long
){
    companion object {
        fun fromStudent(student: Student) = StudentRoom(
            id = student.id,
            email = student.email,
            username = student.username,
            name = student.name,
            password = student.password,
            biography = student.biography,
            experience = student.experience,
            allowRankingVisualization = student.allowRankingVisualization,
            activitiesDelivered = student.activitiesDelivered,
            coursesEntered = student.coursesEntered,
            coursesCompleted = student.coursesCompleted,
            fullMark = student.fullMark,
            topOne = student.topOne,
        )
    }
}