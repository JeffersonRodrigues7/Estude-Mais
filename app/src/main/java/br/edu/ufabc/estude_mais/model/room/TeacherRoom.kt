package br.edu.ufabc.estude_mais.model.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import br.edu.ufabc.estude_mais.model.Teacher

@Entity(    indices = [
    Index(value = ["email"], unique = true),
    Index(value = ["username"], unique = true),
])
data class TeacherRoom (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val email: String,
    val username: String,
    val name: String,
    val password: String,
    val biography: String
){
    companion object {
        fun fromTeacher(teacher: Teacher) = TeacherRoom(
            id = teacher.id,
            email = teacher.email,
            username = teacher.username,
            name = teacher.name,
            password = teacher.password,
            biography = teacher.biography
        )
    }
}